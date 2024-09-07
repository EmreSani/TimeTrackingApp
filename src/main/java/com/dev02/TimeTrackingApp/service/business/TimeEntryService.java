package com.dev02.TimeTrackingApp.service.business;

import com.dev02.TimeTrackingApp.entity.Course;
import com.dev02.TimeTrackingApp.entity.TimeEntry;
import com.dev02.TimeTrackingApp.entity.User;
import com.dev02.TimeTrackingApp.payload.messages.SuccessMessages;
import com.dev02.TimeTrackingApp.payload.request.TimeEntryRequest;
import com.dev02.TimeTrackingApp.payload.response.ResponseMessage;
import com.dev02.TimeTrackingApp.payload.response.TimeResponse;
import com.dev02.TimeTrackingApp.repository.TimeEntryRepository;
import com.dev02.TimeTrackingApp.service.helper.MethodHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.time.*;
import java.util.List;
import java.util.stream.Collectors;

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


    public ResponseMessage<List<TimeResponse>> getAllDailyTimeEntriesByUser(HttpServletRequest request) {
        String username = (String) request.getAttribute("username");
        User user = methodHelper.findByUsername(username);

        // Get today's date range
        LocalDateTime todayStart = LocalDate.now().atStartOfDay();
        LocalDateTime todayEnd = todayStart.plusDays(1);

        return getTimeEntriesForDateRange(user, todayStart, todayEnd, SuccessMessages.DAILY_TIME_GET);
    }

    public ResponseMessage<List<TimeResponse>> getAllPreviousDayTimeEntriesByUser(HttpServletRequest request) {
        String username = (String) request.getAttribute("username");
        User user = methodHelper.findByUsername(username);

        // Get previous day's date range
        LocalDateTime previousDayStart = LocalDate.now().minusDays(1).atStartOfDay();
        LocalDateTime previousDayEnd = previousDayStart.plusDays(1);

        return getTimeEntriesForDateRange(user, previousDayStart, previousDayEnd, SuccessMessages.PREVIOUS_TIME_GET);
    }

    private ResponseMessage<List<TimeResponse>> getTimeEntriesForDateRange(User user, LocalDateTime startDateTime, LocalDateTime endDateTime, String message) {
        List<TimeEntry> timeEntries = timeEntryRepository.findByUserAndStartDateTimeBetween(user, startDateTime, endDateTime);

        List<TimeResponse> timeResponses = timeEntries.stream()
                .map(timeEntry -> new TimeResponse(
                        timeEntry.getCourse().getCourseId(),
                        timeEntry.getCourse().getCourseName(),
                        timeEntry.getDurationInMinutes(),
                        timeEntry.getStartDateTime(),
                        timeEntry.getEndDateTime()))
                .collect(Collectors.toList());

        return ResponseMessage.<List<TimeResponse>>builder()
                .object(timeResponses)
                .message(message)
                .build();
    }


    public ResponseMessage<List<TimeResponse>> getAllWeeklyTimeEntriesByUser(HttpServletRequest request) {
        String username = (String) request.getAttribute("username");
        User user = methodHelper.findByUsername(username);

        // Get the start of the week (Monday)
        LocalDateTime startOfWeek = LocalDate.now().with(DayOfWeek.MONDAY).atStartOfDay();
        LocalDateTime endOfWeek = startOfWeek.plusWeeks(1);

        return getTimeEntriesForDateRange(user, startOfWeek, endOfWeek, SuccessMessages.WEEKLY_TIME_GET);

    }

    public ResponseMessage<List<TimeResponse>> getAllPreviousWeekTimeEntriesByUser(HttpServletRequest request) {
        String username = (String) request.getAttribute("username");
        User user = methodHelper.findByUsername(username);

        // Calculate the previous week range (start of the previous week to end of the previous week)
        LocalDateTime startOfPreviousWeek = LocalDate.now().minusWeeks(1).with(DayOfWeek.MONDAY).atStartOfDay();
        LocalDateTime endOfPreviousWeek = startOfPreviousWeek.plusWeeks(1);

        return getTimeEntriesForDateRange(user, startOfPreviousWeek, endOfPreviousWeek, SuccessMessages.WEEKLY_TIME_GET);
    }


    public ResponseMessage<List<TimeResponse>> getAllMonthlyTimeEntriesByUser(HttpServletRequest request) {
        String username = (String) request.getAttribute("username");
        User user = methodHelper.findByUsername(username);

        // Get the start of the month
        LocalDateTime startOfMonth = LocalDate.now().withDayOfMonth(1).atStartOfDay();
        LocalDateTime endOfMonth = startOfMonth.plusMonths(1);

        return getTimeEntriesForDateRange(user, startOfMonth, endOfMonth, SuccessMessages.MONTHLY_TIME_GET);
    }

    public ResponseMessage<List<TimeResponse>> getAllPreviousMonthTimeEntriesByUser(HttpServletRequest request) {
        String username = (String) request.getAttribute("username");
        User user = methodHelper.findByUsername(username);

        // Calculate the previous month range (start of the previous month to end of the previous month)
        LocalDateTime startOfPreviousMonth = LocalDate.now().minusMonths(1).withDayOfMonth(1).atStartOfDay();
        LocalDateTime endOfPreviousMonth = startOfPreviousMonth.plusMonths(1);

        return getTimeEntriesForDateRange(user, startOfPreviousMonth, endOfPreviousMonth, SuccessMessages.PREVIOUS_MONTHLY_TIME_GET);
    }


}



