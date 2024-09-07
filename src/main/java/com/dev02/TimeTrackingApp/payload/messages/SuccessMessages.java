package com.dev02.TimeTrackingApp.payload.messages;

public class SuccessMessages {

    private SuccessMessages() {
    }

    public static final String PASSWORD_CHANGED_RESPONSE_MESSAGE = "Password Successfully Changed" ;

    public static final String USER_CREATE = "User is Saved";
    public static final String USER_DELETE = "User is deleted successfully";
    public static final String USER_FOUND = "User is Found Successfully";
    public static final String USERS_FOUND = "Users are Found Successfully";
    public static final String USER_UPDATE = "your information has been updated successfully";
    public static final String USER_UPDATE_MESSAGE = "User is Updated Successfully";

    public static final String COURSE_CREATE = "Course is Saved";
    public static final String COURSES_FOUND = "Courses are Found Successfully";
    public static final String COURSE_UPDATE = "Course is updated Successfully";


    public static final String TIME_ADDED = "Time entry added successfully.";

    public static final String DAILY_TIME_GET = "Daily Time entries fetched successfully";
    public static final String PREVIOUS_TIME_GET = "Previous day Time entries fetched successfully";

    public static final String WEEKLY_TIME_GET = "Weekly time entries fetched successfully";
    public static final String PREVIOUS_WEEK_TIME_GET = "Previous Week Time entries fetched successfully";

    public static final String MONTHLY_TIME_GET = "Monthly time entries fetched successfully";
    public static final String PREVIOUS_MONTHLY_TIME_GET = "Previous monthly time entries fetched successfully";

}
