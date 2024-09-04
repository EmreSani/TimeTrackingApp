package com.dev02.TimeTrackingApp.service.helper;


import com.dev02.TimeTrackingApp.entity.User;
import com.dev02.TimeTrackingApp.entity.enums.RoleType;
import com.dev02.TimeTrackingApp.exception.BadRequestException;
import com.dev02.TimeTrackingApp.exception.ResourceNotFoundException;
import com.dev02.TimeTrackingApp.payload.messages.ErrorMessages;
import com.dev02.TimeTrackingApp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MethodHelper {
    private final UserRepository userRepository;

    // !!! isUserExist
    public User isUserExist(Long userId) {
        return userRepository.findById(userId).orElseThrow(() ->
                new ResourceNotFoundException(String.format(ErrorMessages.NOT_FOUND_USER_MESSAGE,
                        userId)));
    }

    // !!! checkBuiltIn
    public void checkBuiltIn(User user){
        if(Boolean.TRUE.equals(user.getBuilt_in())) {
            throw new BadRequestException(ErrorMessages.NOT_PERMITTED_METHOD_MESSAGE);
        }
    }

    //!!! isUserExistWithUsername
    public User isUserExistByUsername(String username){

        User user = userRepository.findByUsernameEquals(username);
        if(user.getUserId() == null){
            throw new ResourceNotFoundException(ErrorMessages.NOT_FOUND_USER_MESSAGE);
        }

        return user;
    }

    // !!! Rol kontrolu yapan method
    public void checkRole(User user, RoleType roleType){
        if (!user.getUserRole().getRoleType().equals(roleType)) {
            throw new ResourceNotFoundException(
                    String.format(ErrorMessages.NOT_FOUND_USER_WITH_ROLE_MESSAGE, user.getUserId(),roleType));
        }
    }
}

