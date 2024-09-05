package com.dev02.TimeTrackingApp.payload.mappers;

import com.dev02.TimeTrackingApp.entity.User;
import com.dev02.TimeTrackingApp.payload.request.UserRequestForRegister;
import com.dev02.TimeTrackingApp.payload.response.UserResponse;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public User mapUserRequestToUser(UserRequestForRegister userRequestForRegister) {

        return User.builder()
                .username(userRequestForRegister.getUsername())
                .firstName(userRequestForRegister.getFirstName())
                .lastName(userRequestForRegister.getLastName())
                .password(userRequestForRegister.getPassword())
                .ssn(userRequestForRegister.getSsn())
                .phoneNumber(userRequestForRegister.getPhoneNumber())
                .email(userRequestForRegister.getEmail())
                .build();
    }

    public UserResponse mapUserToUserResponse(User user) {
        return UserResponse.builder().id(user.getUserId())
                .username(user.getUsername())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .phone(user.getPhoneNumber())
                .email(user.getEmail())
                .ssn(user.getSsn())
                .email(user.getEmail())
                .userRole(user.getUserRole().getRoleType().name())
                .build();
    }



}
