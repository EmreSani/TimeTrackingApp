package com.dev02.TimeTrackingApp.service.business;

import com.dev02.TimeTrackingApp.entity.Course;
import com.dev02.TimeTrackingApp.entity.TimeEntry;
import com.dev02.TimeTrackingApp.entity.User;
import com.dev02.TimeTrackingApp.entity.enums.TimePeriod;
import com.dev02.TimeTrackingApp.payload.mappers.TimeEntryMapper;
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
    private final TimeEntryMapper timeEntryMapper;

    // Updated method to return TimeResponse
    //todo: mapper ile yapmayı düşün.
    public ResponseMessage<TimeResponse> addNewTimeEntry(TimeEntryRequest timeEntryRequest, HttpServletRequest httpRequest) {
        // Retrieve user
        User user = getUserFromRequest(httpRequest);

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

    public ResponseMessage<List<TimeResponse>> getDailyTimeEntriesByUser(HttpServletRequest request) {
        return getTimeEntriesByPeriod(request, TimePeriod.DAY);
    }

    public ResponseMessage<List<TimeResponse>> getPreviousDayTimeEntriesByUser(HttpServletRequest request) {
        return getTimeEntriesByPeriod(request, TimePeriod.PREVIOUS_DAY);
    }

    public ResponseMessage<List<TimeResponse>> getWeeklyTimeEntriesByUser(HttpServletRequest request) {
        return getTimeEntriesByPeriod(request, TimePeriod.WEEK);
    }

    public ResponseMessage<List<TimeResponse>> getPreviousWeekTimeEntriesByUser(HttpServletRequest request) {
        return getTimeEntriesByPeriod(request, TimePeriod.PREVIOUS_WEEK);
    }

    public ResponseMessage<List<TimeResponse>> getMonthlyTimeEntriesByUser(HttpServletRequest request) {
        return getTimeEntriesByPeriod(request, TimePeriod.MONTH);
    }

    public ResponseMessage<List<TimeResponse>> getPreviousMonthTimeEntriesByUser(HttpServletRequest request) {
        return getTimeEntriesByPeriod(request, TimePeriod.PREVIOUS_MONTH);
    }


    private User getUserFromRequest(HttpServletRequest request) {
        String username = (String) request.getAttribute("username");
        return methodHelper.findByUsername(username);
    }

    private ResponseMessage<List<TimeResponse>> getTimeEntriesByPeriod(HttpServletRequest request, TimePeriod period) {
        User user = getUserFromRequest(request);
        return getTimeEntriesForPeriod(user, period, getSuccessMessageForPeriod(period));
    }

    private ResponseMessage<List<TimeResponse>> getTimeEntriesForPeriod(User user, TimePeriod period, String message) {
        LocalDateTime[] dateRange = calculateDateRange(period);
        return getTimeEntriesForDateRange(user, dateRange[0], dateRange[1], message);
    }

    private String getSuccessMessageForPeriod(TimePeriod period) {
        switch (period) {
            case PREVIOUS_DAY:
                return SuccessMessages.PREVIOUS_TIME_GET;
            case WEEK:
                return SuccessMessages.WEEKLY_TIME_GET;
            case PREVIOUS_WEEK:
                return SuccessMessages.PREVIOUS_WEEK_TIME_GET;
            case MONTH:
                return SuccessMessages.MONTHLY_TIME_GET;
            case PREVIOUS_MONTH:
                return SuccessMessages.PREVIOUS_MONTHLY_TIME_GET;
            case DAY:
            default:
                return SuccessMessages.DAILY_TIME_GET;
        }
    }

    private LocalDateTime[] calculateDateRange(TimePeriod period) {
        LocalDateTime startDateTime;
        LocalDateTime endDateTime;

        switch (period) {
            case PREVIOUS_DAY:
                startDateTime = LocalDate.now().minusDays(1).atStartOfDay();
                endDateTime = startDateTime.plusDays(1);
                break;
            case WEEK:
                startDateTime = LocalDate.now().with(DayOfWeek.MONDAY).atStartOfDay();
                endDateTime = startDateTime.plusWeeks(1);
                break;
            case PREVIOUS_WEEK:
                startDateTime = LocalDate.now().minusWeeks(1).with(DayOfWeek.MONDAY).atStartOfDay();
                endDateTime = startDateTime.plusWeeks(1);
                break;
            case MONTH:
                startDateTime = LocalDate.now().withDayOfMonth(1).atStartOfDay();
                endDateTime = startDateTime.plusMonths(1);
                break;
            case PREVIOUS_MONTH:
                startDateTime = LocalDate.now().minusMonths(1).withDayOfMonth(1).atStartOfDay();
                endDateTime = startDateTime.plusMonths(1);
                break;
            case DAY:
            default:
                startDateTime = LocalDate.now().atStartOfDay();
                endDateTime = startDateTime.plusDays(1);
                break;
        }

        return new LocalDateTime[]{startDateTime, endDateTime};
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


    public ResponseMessage<List<TimeResponse>> getAllTimeEntries(HttpServletRequest request) {
        User user = getUserFromRequest(request);

        List<TimeEntry> timeEntryList = timeEntryRepository.findAll();

        List<TimeResponse> timeResponses = timeEntryList.stream()
                .map(timeEntry -> new TimeResponse(
                        timeEntry.getCourse().getCourseId(),
                        timeEntry.getCourse().getCourseName(),
                        timeEntry.getDurationInMinutes(),
                        timeEntry.getStartDateTime(),
                        timeEntry.getEndDateTime()))
                .toList();

        return ResponseMessage.<List<TimeResponse>>builder().message
                        (SuccessMessages.TIME_FETCHED)
                .object(timeResponses)
                .httpStatus(HttpStatus.OK)
                .build();
    }


    public ResponseMessage<TimeResponse> updateTimeEntry(TimeEntryRequest timeEntryRequest, Long timeEntryId, HttpServletRequest request) {
        User user = getUserFromRequest(request);

        TimeEntry timeEntryToUpdate = methodHelper.isEntryTimeExist(timeEntryId);

        // Retrieve course
        Course course = methodHelper.isCourseExist(timeEntryRequest.getCourseId());

        // Get start and end times
        LocalDateTime startDateTime = timeEntryRequest.getStartDateTime();
        LocalDateTime endDateTime = timeEntryRequest.getEndDateTime();

        // Calculate duration
        Duration duration = Duration.between(startDateTime, endDateTime);
        long durationInMinutes = duration.toMinutes();

        // Update or create time entries
        long totalMinutes = 0;

        if (startDateTime.toLocalDate().equals(endDateTime.toLocalDate())) {
            // Update existing entry if within the same day
            timeEntryToUpdate.setCourse(course);
            timeEntryToUpdate.setStartDateTime(startDateTime);
            timeEntryToUpdate.setEndDateTime(endDateTime);
            timeEntryToUpdate.setDurationInMinutes(durationInMinutes);
            totalMinutes = durationInMinutes;
        } else {
            // Handle multiple days
            LocalDateTime endOfDay = startDateTime.toLocalDate().atTime(LocalTime.MAX);
            long firstDayMinutes = Duration.between(startDateTime, endOfDay).toMinutes();

            LocalDateTime startOfNextDay = endDateTime.toLocalDate().atStartOfDay();
            long lastDayMinutes = Duration.between(startOfNextDay, endDateTime).toMinutes();

            // Update existing or create new entries
            timeEntryToUpdate.setCourse(course);
            timeEntryToUpdate.setStartDateTime(startDateTime);
            timeEntryToUpdate.setEndDateTime(endOfDay);
            timeEntryToUpdate.setDurationInMinutes(firstDayMinutes);
            timeEntryRepository.save(timeEntryToUpdate);

            TimeEntry secondEntry = new TimeEntry();
            secondEntry.setUser(user);
            secondEntry.setCourse(course);
            secondEntry.setStartDateTime(startOfNextDay);
            secondEntry.setEndDateTime(endDateTime);
            secondEntry.setDurationInMinutes(lastDayMinutes);
            timeEntryRepository.save(secondEntry);

            totalMinutes = firstDayMinutes + lastDayMinutes;
        }


        TimeResponse timeResponse = timeEntryMapper.mapTimeEntryToTimeResponse(timeEntryToUpdate, totalMinutes);

        return ResponseMessage.<TimeResponse>builder()
                .message(SuccessMessages.TIME_UPDATE)
                .httpStatus(HttpStatus.OK)
                .object(timeResponse)
                .build();
    }

}



