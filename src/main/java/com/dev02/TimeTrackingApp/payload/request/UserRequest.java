package com.dev02.TimeTrackingApp.payload.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserRequest {

    @NotNull
    @Size(min = 2, max = 30, message = "(${validatedValue}) {min} and {max} lengths allowed!")
    private String firstName;

    @NotNull
    @Size(min = 2, max = 30, message = "(${validatedValue}) {min} and {max} lengths allowed!")
    private String lastName;

    @NotNull
    //@Pattern(regexp = "^0 \\d{3} \\d{3} \\d{4}$", message = "Please enter a valid phone number in the format 999-999-9999")
    private String phoneNumber;

    @NotNull
    @Size(min = 5, max = 20, message = "(${validatedValue}) {min} and {max} lengths allowed!")
    private String username;

    @NotNull
    @Size(min = 10, max = 80, message = "(${validatedValue}) {min} and {max} lengths allowed!")
    @Email
    private String email;

    @NotNull
    private String currentPassword;

    private String newPassword;


    private String ssn;
}
