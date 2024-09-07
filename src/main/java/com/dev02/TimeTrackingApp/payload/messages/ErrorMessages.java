package com.dev02.TimeTrackingApp.payload.messages;

public class ErrorMessages {

    private ErrorMessages() {
    }

    public static final String NOT_PERMITTED_METHOD_MESSAGE = "You do not have any permission to do this operation";
    public static final String PASSWORD_NOT_MATCHED = "Your passwords are not matched" ;
    public static final String ALREADY_REGISTER_MESSAGE_USERNAME = "Error: User with username %s already registered" ;
    public static final String ALREADY_REGISTER_MESSAGE_SSN = "Error: User with ssn %s already registered" ;
    public static final String ALREADY_REGISTER_MESSAGE_EMAIL = "Error: User with email %s already registered" ;
    public static final String ALREADY_REGISTER_MESSAGE_PHONE = "Error: User with phone number %s already registered" ;

    public static final String ROLE_NOT_FOUND = "There is no role like that , check the database" ;
    public static final String NOT_FOUND_USER_WITH_ROLE_MESSAGE = "Error: The role information of the user with id %s is not role: %s" ;

    public static final String NOT_FOUND_USER_MESSAGE = "Error: User not found with id %s";
    public static final String NOT_FOUND_USER_MESSAGE_WITH_NAME = "Error: User not found username %s";

    public static final String NOT_FOUND_COURSE_MESSAGE = "Error: Course not found with id %s";
    public static final String NOT_PERMITTED_TO_DELETE_COURSE = "You do not have any permission to delete this course";

    public static final String NOT_FOUND_TIME_ENTRY_MESSAGE = "Error: Time Entry not found with id %s";


}
