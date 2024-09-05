package com.dev02.TimeTrackingApp.service.validator;

import com.dev02.TimeTrackingApp.entity.User;
import com.dev02.TimeTrackingApp.exception.ConflictException;
import com.dev02.TimeTrackingApp.payload.messages.ErrorMessages;
import com.dev02.TimeTrackingApp.payload.request.UserRequest;
import com.dev02.TimeTrackingApp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UniquePropertyValidator {

    private final UserRepository userRepository;

    public void checkDuplicate(String username, String ssn, String phone, String email){


        if(userRepository.existsByUsername(username)){
            throw new ConflictException(String.format(ErrorMessages.ALREADY_REGISTER_MESSAGE_USERNAME, username));
        }

        if(userRepository.existsBySsn(ssn)){
            throw new ConflictException(String.format(ErrorMessages.ALREADY_REGISTER_MESSAGE_SSN, ssn));
        }

        if(userRepository.existsByPhoneNumber(phone)){
            throw new ConflictException(String.format(ErrorMessages.ALREADY_REGISTER_MESSAGE_PHONE, phone));
        }

        if(userRepository.existsByEmail(email)){
            throw new ConflictException(String.format(ErrorMessages.ALREADY_REGISTER_MESSAGE_EMAIL, email));
        }

    }

    public void checkUniqueProperties(User user, UserRequest userRequest){
        String updatedUsername = "";
        String updatedSnn = "";
        String updatedPhone = "";
        String updatedEmail = "";

        boolean isChanged = false;

        if(!user.getUsername().equalsIgnoreCase(userRequest.getUsername())){
            updatedUsername = userRequest.getUsername();
            isChanged = true;
        }
        if(!user.getSsn().equalsIgnoreCase(userRequest.getSsn())){
            updatedSnn = userRequest.getSsn();
            isChanged = true;
        }
        if(!user.getPhoneNumber().equalsIgnoreCase(userRequest.getPhoneNumber())){
            updatedPhone = userRequest.getPhoneNumber();
            isChanged = true;
        }
        if(!user.getEmail().equalsIgnoreCase(userRequest.getEmail())){
            updatedEmail = userRequest.getEmail();
            isChanged = true;
        }

        if(isChanged) {
            checkDuplicate(updatedUsername, updatedSnn, updatedPhone, updatedEmail);
        }

    }
}
