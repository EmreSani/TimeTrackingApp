package com.dev02.TimeTrackingApp.service.business;

import com.dev02.TimeTrackingApp.entity.Course;
import com.dev02.TimeTrackingApp.entity.TimeEntry;
import com.dev02.TimeTrackingApp.entity.User;
import com.dev02.TimeTrackingApp.payload.messages.SuccessMessages;
import com.dev02.TimeTrackingApp.payload.request.TimeEntryRequest;
import com.dev02.TimeTrackingApp.payload.response.CourseResponse;
import com.dev02.TimeTrackingApp.payload.response.ResponseMessage;
import com.dev02.TimeTrackingApp.payload.response.TimeResponse;
import com.dev02.TimeTrackingApp.repository.TimeEntryRepository;
import com.dev02.TimeTrackingApp.service.helper.MethodHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Service
@RequiredArgsConstructor
public class TimeEntryService {

    private final TimeEntryRepository timeEntryRepository;
    private final MethodHelper methodHelper;

    // Updated method to return TimeResponse
    public ResponseMessage<TimeResponse> addNewTimeEntry(TimeEntryRequest timeEntryRequest, HttpServletRequest httpRequest) {
        // Retrieve user
        String username = (String) httpRequest.getAttribute("username");
        User user = methodHelper.findByUsername(username);

        // Retrieve course
        Course course = methodHelper.isCourseExist(timeEntryRequest.getCourseId());

        // Get start and end times
        LocalDateTime startDateTime = timeEntryRequest.getStartDateTime();
        LocalDateTime endDateTime = timeEntryRequest.getEndDateTime();

        // Calculate and save time entries
        long totalMinutes = calculateAndSaveTimeEntries(startDateTime, endDateTime, user, course);

        // Create TimeResponse
        TimeResponse timeResponse = new TimeResponse(
                course.getCourseId(),
                course.getCourseName(),
                totalMinutes,
                startDateTime,
                endDateTime
        );

        return ResponseMessage.<TimeResponse>builder().message(SuccessMessages.TIME_ADDED)
                .httpStatus(HttpStatus.OK)
                .object(timeResponse).build();
    }

    // Updated to return total minutes worked
    private long calculateAndSaveTimeEntries(LocalDateTime startDateTime, LocalDateTime endDateTime, User user, Course course) {
        long totalMinutes = 0;
        if (startDateTime.toLocalDate().equals(endDateTime.toLocalDate())) {
            // Same day work
            totalMinutes = saveTimeEntry(startDateTime, endDateTime, user, course);
        } else {
            // Spread across multiple days
            LocalDateTime endOfDay = startDateTime.toLocalDate().atTime(LocalTime.MAX);
            totalMinutes += saveTimeEntry(startDateTime, endOfDay, user, course);

            LocalDateTime startOfNextDay = endDateTime.toLocalDate().atStartOfDay();
            totalMinutes += saveTimeEntry(startOfNextDay, endDateTime, user, course);
        }
        return totalMinutes;
    }

    // Updated to return the duration in minutes
    private long saveTimeEntry(LocalDateTime startDateTime, LocalDateTime endDateTime, User user, Course course) {
        Duration duration = Duration.between(startDateTime, endDateTime);
        long durationInMinutes = duration.toMinutes();

        TimeEntry timeEntry = new TimeEntry();
        timeEntry.setUser(user);
        timeEntry.setCourse(course);
        timeEntry.setStartDateTime(startDateTime);
        timeEntry.setEndDateTime(endDateTime);
        timeEntry.setDurationInMinutes(durationInMinutes);

        timeEntryRepository.save(timeEntry);

        return durationInMinutes;
    }



}



