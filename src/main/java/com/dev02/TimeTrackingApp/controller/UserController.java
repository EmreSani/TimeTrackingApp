package com.dev02.TimeTrackingApp.controller;

import com.dev02.TimeTrackingApp.payload.request.LoginRequest;
import com.dev02.TimeTrackingApp.payload.request.UserRequest;
import com.dev02.TimeTrackingApp.payload.request.UserRequestForRegister;
import com.dev02.TimeTrackingApp.payload.response.LoginResponse;
import com.dev02.TimeTrackingApp.payload.response.ResponseMessage;
import com.dev02.TimeTrackingApp.payload.response.UserResponse;
import com.dev02.TimeTrackingApp.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // http://localhost:8080/user/login + POST
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody @Valid LoginRequest LoginRequest) {
        return userService.authenticateUser(LoginRequest);
    }

    // http://localhost:8080/user/register + POST
    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(@RequestBody @Valid UserRequestForRegister userRequestForRegister) {
        return userService.register(userRequestForRegister);
    }

    // http://localhost:8080/user/{userId} + GET
    @GetMapping("/{userId}")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseEntity<UserResponse> getUserById(@PathVariable Long userId) {
        return userService.getUserById(userId);
    }

    // http://localhost:8080/user + POST
    @PostMapping
    @PreAuthorize("hasAnyAuthority('ADMIN','USER')")
    public ResponseMessage<UserResponse> getAuthenticatedUser(HttpServletRequest httpServletRequest) {
        return userService.getAuthenticatedUser(httpServletRequest);
    }

    // http://localhost:8080/user + Get
    @GetMapping
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseMessage<Page<UserResponse>> getAllUsers(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "20") int size,
            @RequestParam(value = "sort", defaultValue = "createDate") String sort,
            @RequestParam(value = "type", defaultValue = "desc") String type) {

        return userService.getAllUsersByPage(page, size, sort, type);
    }

    // http://localhost:8080/user/{userId} + DELETE
    @DeleteMapping("/{userId}")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseEntity<UserResponse> deleteUserById(@PathVariable Long userId) {
        return userService.deleteUserById(userId);
    }

    // Not: updateUserForUser() **********************************************************
    // !!! Kullanicinin kendisini update etmesini saglayan method
    @PatchMapping("/updateUser")   // http://localhost:8080/user/updateUser
    @PreAuthorize("hasAnyAuthority('ADMIN','USER')")
    public ResponseEntity<String>updateUser(@RequestBody @Valid
                                                UserRequest userRequest,
                                            HttpServletRequest request){
        return userService.updateUserForUsers(userRequest, request) ;
    }

}
