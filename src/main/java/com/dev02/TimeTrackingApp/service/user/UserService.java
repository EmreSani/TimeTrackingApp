package com.dev02.TimeTrackingApp.service.user;

import com.dev02.TimeTrackingApp.entity.User;
import com.dev02.TimeTrackingApp.entity.enums.RoleType;
import com.dev02.TimeTrackingApp.exception.BadRequestException;
import com.dev02.TimeTrackingApp.payload.mappers.UserMapper;
import com.dev02.TimeTrackingApp.payload.messages.ErrorMessages;
import com.dev02.TimeTrackingApp.payload.messages.SuccessMessages;
import com.dev02.TimeTrackingApp.payload.request.LoginRequest;
import com.dev02.TimeTrackingApp.payload.request.UserRequest;
import com.dev02.TimeTrackingApp.payload.request.UserRequestForRegister;
import com.dev02.TimeTrackingApp.payload.response.LoginResponse;
import com.dev02.TimeTrackingApp.payload.response.ResponseMessage;
import com.dev02.TimeTrackingApp.payload.response.UserResponse;
import com.dev02.TimeTrackingApp.repository.UserRepository;
import com.dev02.TimeTrackingApp.repository.UserRoleRepository;
import com.dev02.TimeTrackingApp.security.jwt.JwtUtils;
import com.dev02.TimeTrackingApp.security.service.UserDetailsImpl;
import com.dev02.TimeTrackingApp.service.helper.MethodHelper;
import com.dev02.TimeTrackingApp.service.helper.PageableHelper;
import com.dev02.TimeTrackingApp.service.validator.UniquePropertyValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final MethodHelper methodHelper;
    public final JwtUtils jwtUtils;
    public final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final UniquePropertyValidator uniquePropertyValidator;
    private final UserRepository userRepository;
    private final UserRoleService userRoleService;
    private final UserMapper userMapper;
    private final PageableHelper pageableHelper;

    public ResponseEntity<LoginResponse> authenticateUser(LoginRequest loginRequest) {
        String username = loginRequest.getUsername();
        String password = loginRequest.getPassword();

        Authentication authentication = authenticationManager.
                authenticate(new UsernamePasswordAuthenticationToken(username, password));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = "Bearer " + jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();


        Set<String> roles = userDetails.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet());

        Optional<String> role = roles.stream().findFirst();

        LoginResponse.LoginResponseBuilder loginResponse = LoginResponse.builder();
        loginResponse.username(userDetails.getUsername());
        loginResponse.token(token.substring(7));
        loginResponse.name(userDetails.getName());
        loginResponse.ssn(userDetails.getSsn());
        // !!! role bilgisi varsa response nesnesindeki degisken setleniyor
        role.ifPresent(loginResponse::role);
        // !!! AuthResponse nesnesi ResponseEntity ile donduruyoruz
        return ResponseEntity.ok(loginResponse.build());

    }

    public ResponseEntity<UserResponse> register(UserRequestForRegister userRequestForRegister) {

        //!!! username - ssn- phoneNumber unique mi kontrolu ??
        uniquePropertyValidator.checkDuplicate(userRequestForRegister.getUsername(), userRequestForRegister.getSsn(),
                userRequestForRegister.getPhoneNumber(), userRequestForRegister.getEmail());
        //!!! DTO --> POJO
        User user = userMapper.mapUserRequestToUser(userRequestForRegister);

        user.setUserRole(userRoleService.getUserRole(RoleType.USER));

        user.setBuilt_in(Boolean.FALSE);

        user.setPassword(passwordEncoder.encode(userRequestForRegister.getPassword()));

        user.setCreateTime(LocalDateTime.now()); // Automatically set on create

        User savedUser = userRepository.save(user);

        return ResponseEntity.ok(userMapper.mapUserToUserResponse(savedUser));

    }

    public ResponseEntity<UserResponse> getUserById(Long userId) {

        User user = methodHelper.isUserExist(userId);

        return ResponseEntity.ok(userMapper.mapUserToUserResponse(user));

    }

    public ResponseMessage<UserResponse> getAuthenticatedUser(HttpServletRequest httpServletRequest) {

        String username = (String) httpServletRequest.getAttribute("username");

        User foundUser = userRepository.findByUsername(username);

        return ResponseMessage.<UserResponse>builder().message(SuccessMessages.USER_FOUND)
                .httpStatus(HttpStatus.OK)
                .object(userMapper.mapUserToUserResponse(foundUser)).build();

    }


    public ResponseMessage<Page<UserResponse>> getAllUsersByPage(int page, int size, String sort, String type) {
        Pageable pageable = pageableHelper.getPageableWithProperties(page, size, sort, type);

        return ResponseMessage.<Page<UserResponse>>builder().message(SuccessMessages.USERS_FOUND)
                .httpStatus(HttpStatus.OK)
                .object(userRepository.findAll(pageable).map(userMapper::mapUserToUserResponse))
                .build();

    }

    public ResponseEntity<UserResponse> deleteUserById(Long userId) {
        User user = methodHelper.isUserExist(userId);

        methodHelper.checkBuiltIn(user);

        userRepository.delete(user);

        return ResponseEntity.ok(userMapper.mapUserToUserResponse(user));
    }


    // Not: updateUserForUser() **********************************************************
    public ResponseEntity<String> updateUserForUsers(UserRequest userRequest,
                                                     HttpServletRequest request) {
        String userName = (String) request.getAttribute("username");
        User user = userRepository.findByUsername(userName);

        // !!! bulit_in kontrolu
        methodHelper.checkBuiltIn(user) ;

        // !!! unique kontrolu
        uniquePropertyValidator.checkUniqueProperties(user, userRequest);
        // !!! DTO --> pojo donusumu burada yazildi
        user.setUsername(userRequest.getUsername());
        user.setEmail(userRequest.getEmail());
        user.setPhoneNumber(userRequest.getPhoneNumber());
        user.setFirstName(userRequest.getFirstName());
        user.setLastName(userRequest.getLastName());
        user.setSsn(userRequest.getSsn());

        if(!passwordEncoder.matches(userRequest.getCurrentPassword(),user.getPassword())) {
            throw new BadRequestException(ErrorMessages.PASSWORD_NOT_MATCHED);
        }
        // !!! yeni sifre hashlenerek Kaydediliyor
        String hashedPassword=  passwordEncoder.encode(userRequest.getNewPassword());

        user.setPassword(hashedPassword);

        userRepository.save(user);

        String message = SuccessMessages.USER_UPDATE;

        return ResponseEntity.ok(message);
    }

    public ResponseMessage<UserResponse> saveUser(UserRequestForRegister userRequest, String userRole) {

        //!!! username - ssn- phoneNumber unique mi kontrolu ??
        uniquePropertyValidator.checkDuplicate(userRequest.getUsername(),userRequest.getSsn(),
                userRequest.getPhoneNumber(),userRequest.getEmail());
        //!!! DTO --> POJO
        User user = userMapper.mapUserRequestToUser(userRequest);
        // !!! Rol bilgisi setleniyor
        if(userRole.equalsIgnoreCase(RoleType.ADMIN.name())){
            if(Objects.equals(userRequest.getUsername(),"Admin")){
                user.setBuilt_in(true);
            }
            user.setUserRole(userRoleService.getUserRole(RoleType.ADMIN));
        }
        // !!! password encode ediliyor
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        // Advisor degil

        User savedUser = userRepository.save(user);

        return ResponseMessage.<UserResponse>builder()
                .message(SuccessMessages.USER_CREATE)
                .object(userMapper.mapUserToUserResponse(savedUser))
                .build() ;
    }

    public long countAllAdmins(){
        return userRepository.countAdmin(RoleType.ADMIN);
    }
}
