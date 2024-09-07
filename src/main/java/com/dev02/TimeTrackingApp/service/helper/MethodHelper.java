package com.dev02.TimeTrackingApp.service.helper;


import com.dev02.TimeTrackingApp.entity.Course;
import com.dev02.TimeTrackingApp.entity.TimeEntry;
import com.dev02.TimeTrackingApp.entity.User;
import com.dev02.TimeTrackingApp.entity.enums.RoleType;
import com.dev02.TimeTrackingApp.exception.BadRequestException;
import com.dev02.TimeTrackingApp.exception.ResourceNotFoundException;
import com.dev02.TimeTrackingApp.payload.messages.ErrorMessages;
import com.dev02.TimeTrackingApp.repository.CourseRepository;
import com.dev02.TimeTrackingApp.repository.TimeEntryRepository;
import com.dev02.TimeTrackingApp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class MethodHelper {
    private final UserRepository userRepository;
    private final CourseRepository courseRepository;
    private final TimeEntryRepository timeEntryRepository;

    // !!! isUserExist
    public User isUserExist(Long userId) {
        return userRepository.findById(userId).orElseThrow(() ->
                new ResourceNotFoundException(String.format(ErrorMessages.NOT_FOUND_USER_MESSAGE,
                        userId)));
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public Course isCourseExist(Long courseId) {
        return courseRepository.findById(courseId).orElseThrow(() ->
                new ResourceNotFoundException(String.format(ErrorMessages.NOT_FOUND_COURSE_MESSAGE,
                        courseId)));
    }

    public TimeEntry isEntryTimeExist(Long timeEntryId) {
        return timeEntryRepository.findById(timeEntryId).orElseThrow(() ->
                new ResourceNotFoundException(String.format(ErrorMessages.NOT_FOUND_TIME_ENTRY_MESSAGE,
                        timeEntryId)));
    }

    // !!! checkBuiltIn
    public void checkBuiltIn(User user) {
        if (Boolean.TRUE.equals(user.getBuilt_in())) {
            throw new BadRequestException(ErrorMessages.NOT_PERMITTED_METHOD_MESSAGE);
        }
    }

    //!!! isUserExistWithUsername
    public User isUserExistByUsername(String username) {

        User user = userRepository.findByUsernameEquals(username);
        if (user.getUserId() == null) {
            throw new ResourceNotFoundException(ErrorMessages.NOT_FOUND_USER_MESSAGE);
        }

        return user;
    }


    // !!! Rol kontrolu yapan method
    public void checkRole(User user, RoleType roleType) {
        if (!user.getUserRole().getRoleType().equals(roleType)) {
            throw new ResourceNotFoundException(
                    String.format(ErrorMessages.NOT_FOUND_USER_WITH_ROLE_MESSAGE, user.getUserId(), roleType));
        }
    }

}

