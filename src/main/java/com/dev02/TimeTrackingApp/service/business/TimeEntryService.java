package com.dev02.TimeTrackingApp.service.business;

import com.dev02.TimeTrackingApp.entity.Course;
import com.dev02.TimeTrackingApp.entity.TimeEntry;
import com.dev02.TimeTrackingApp.entity.User;
import com.dev02.TimeTrackingApp.entity.enums.TimePeriod;
import com.dev02.TimeTrackingApp.exception.BadRequestException;
import com.dev02.TimeTrackingApp.payload.mappers.TimeEntryMapper;
import com.dev02.TimeTrackingApp.payload.messages.ErrorMessages;
import com.dev02.TimeTrackingApp.payload.messages.SuccessMessages;
import com.dev02.TimeTrackingApp.payload.request.TimeEntryRequest;
import com.dev02.TimeTrackingApp.payload.response.ResponseMessage;
import com.dev02.TimeTrackingApp.payload.response.TimeResponse;
import com.dev02.TimeTrackingApp.repository.TimeEntryRepository;
import com.dev02.TimeTrackingApp.service.helper.MethodHelper;
import com.dev02.TimeTrackingApp.service.validator.TimeEntryValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.time.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
@RequiredArgsConstructor
public class TimeEntryService {

    private final TimeEntryRepository timeEntryRepository;
    private final MethodHelper methodHelper;
    private final TimeEntryMapper timeEntryMapper;
    private final TimeEntryValidator timeEntryValidator;

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

        // TimeValidator ile zaman çakışmalarını kontrol et
        timeEntryValidator.validateTimeEntry(user, startDateTime, endDateTime);

        // Zaman çakışması yoksa kaydı oluştur
       TimeEntry timeEntry = calculateAndSaveTimeEntries(startDateTime, endDateTime, user, course);

        // Check if the course has overlapping entries
        long totalMinutesForCourse = calculateTotalMinutesForCourse(course, user, startDateTime.toLocalDate(), endDateTime.toLocalDate());

        // Create TimeResponse
        TimeResponse timeResponse = new TimeResponse(
                timeEntry.getTimeEntryId(),
                course.getCourseId(),
                course.getCourseName(),
                totalMinutesForCourse,
                timeEntry.getStartDateTime()
        );

        return ResponseMessage.<TimeResponse>builder().message(SuccessMessages.TIME_ADDED)
                .httpStatus(HttpStatus.OK)
                .object(timeResponse).build();
    }


    private long calculateTotalMinutesForCourse(Course course, User user, LocalDate startDate, LocalDate endDate) {
        List<TimeEntry> entries = timeEntryRepository.findByUserAndCourseAndStartDateTimeBetween(user, course, startDate.atStartOfDay(), endDate.atTime(LocalTime.MAX));
        return entries.stream()
                .mapToLong(TimeEntry::getDurationInMinutes)
                .sum();
    }

    // Updated to return total minutes worked
//    private long calculateAndSaveTimeEntries(LocalDateTime startDateTime, LocalDateTime endDateTime, User user, Course course) {
//        long totalMinutes = 0;
//        if (startDateTime.toLocalDate().equals(endDateTime.toLocalDate())) {
//            // Same day work
//            totalMinutes = saveTimeEntry(startDateTime, endDateTime, user, course);
//        } else {
//            // Spread across multiple days
//            LocalDateTime endOfDay = startDateTime.toLocalDate().atTime(LocalTime.MAX);
//            totalMinutes += saveTimeEntry(startDateTime, endOfDay, user, course);
//
//            LocalDateTime startOfNextDay = endDateTime.toLocalDate().atStartOfDay();
//            totalMinutes += saveTimeEntry(startOfNextDay, endDateTime, user, course);
//        }
//        return totalMinutes;
//    }

    private TimeEntry calculateAndSaveTimeEntries(LocalDateTime startDateTime, LocalDateTime endDateTime, User user, Course course) {

        TimeEntry timeEntry;
        if (startDateTime.toLocalDate().equals(endDateTime.toLocalDate())) {
            // Aynı gün çalışma
            timeEntry = saveTimeEntry(startDateTime, endDateTime, user, course);
        } else {
            // Birden fazla güne yayılmış çalışma
            // Günü saat 23:59:59'da bitirmek yerine 00:00:00'da başlat
            LocalDateTime endOfDay = startDateTime.toLocalDate().atTime(23, 59, 59);
            timeEntry = saveTimeEntry(startDateTime, endOfDay, user, course);

            // Günler arasında tüm günü doldurmak için saat 00:00:00'dan itibaren başlat
            LocalDate nextDay = startDateTime.toLocalDate().plusDays(1);
            while (nextDay.isBefore(endDateTime.toLocalDate())) {
                timeEntry = saveTimeEntry(nextDay.atStartOfDay(), nextDay.atTime(23, 59, 59), user, course);
                nextDay = nextDay.plusDays(1);
            }

            // Son günü doldur
            LocalDateTime startOfLastDay = endDateTime.toLocalDate().atStartOfDay();
            timeEntry = saveTimeEntry(startOfLastDay, endDateTime, user, course);
        }
        return timeEntry;
    }


    // Updated to return the duration in minutes
    private TimeEntry saveTimeEntry(LocalDateTime startDateTime, LocalDateTime endDateTime, User user, Course course) {
        Duration duration = Duration.between(startDateTime, endDateTime);
        long durationInMinutes = duration.toMinutes();

        TimeEntry timeEntry = new TimeEntry();
        timeEntry.setUser(user);
        timeEntry.setCourse(course);
        timeEntry.setStartDateTime(startDateTime);
        timeEntry.setEndDateTime(endDateTime);
        timeEntry.setDurationInMinutes(durationInMinutes);

        timeEntryRepository.save(timeEntry);

        return timeEntry;
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
                endDateTime = startDateTime.plusDays(1).minusSeconds(1);
                break;
            case WEEK:
                startDateTime = LocalDate.now().with(DayOfWeek.MONDAY).atStartOfDay();
                endDateTime = startDateTime.plusWeeks(1).minusSeconds(1);
                break;
            case PREVIOUS_WEEK:
                startDateTime = LocalDate.now().minusWeeks(1).with(DayOfWeek.MONDAY).atStartOfDay();
                endDateTime = startDateTime.plusWeeks(1).minusSeconds(1);
                break;
            case MONTH:
                startDateTime = LocalDate.now().withDayOfMonth(1).atStartOfDay();
                endDateTime = startDateTime.plusMonths(1).minusSeconds(1);
                break;
            case PREVIOUS_MONTH:
                startDateTime = LocalDate.now().minusMonths(1).withDayOfMonth(1).atStartOfDay();
                endDateTime = startDateTime.plusMonths(1).minusSeconds(1);
                break;
            case DAY:
            default:
                startDateTime = LocalDate.now().atStartOfDay();
                endDateTime = startDateTime.plusDays(1).minusSeconds(1);
                break;
        }

        return new LocalDateTime[]{startDateTime, endDateTime};
    }


//    private ResponseMessage<List<TimeResponse>> getTimeEntriesForDateRange(User user, LocalDateTime startDateTime, LocalDateTime endDateTime, String message) {
//
//        List<TimeEntry> timeEntries = timeEntryRepository.findByUserAndStartDateTimeBetween(user, startDateTime, endDateTime);
//
//        System.out.println("Start Date: " + startDateTime + ", End Date: " + endDateTime);
//    //    List<TimeEntry> testTimeEntries = timeEntryRepository.findByUserAndStartDateTimeBetween(user, LocalDateTime.parse("2024-09-06T00:00:00"), LocalDateTime.parse("2024-09-11T23:59:59"));
//        System.out.println("Found Time Entries: " + timeEntries.size());
//
//        List<TimeResponse> timeResponses = timeEntries.stream()
//                .map(timeEntry -> new TimeResponse(
//                        timeEntry.getCourse().getCourseId(),
//                        timeEntry.getCourse().getCourseName(),
//                        timeEntry.getDurationInMinutes(),
//                        timeEntry.getStartDateTime(),
//                        timeEntry.getEndDateTime()))
//                .collect(Collectors.toList());
//
//        return ResponseMessage.<List<TimeResponse>>builder()
//                .object(timeResponses)
//                .message(message)
//                .build();
//    }

    private ResponseMessage<List<TimeResponse>> getTimeEntriesForDateRange(User user, LocalDateTime startDateTime, LocalDateTime endDateTime, String message) {
        // Get all time entries within the specified date range
        List<TimeEntry> timeEntries = timeEntryRepository.findByUserAndStartDateTimeBetween(user, startDateTime, endDateTime);

        // Print debug information
        System.out.println("Start Date: " + startDateTime + ", End Date: " + endDateTime);
        System.out.println("Found Time Entries: " + timeEntries.size());

        // Create a map to accumulate total minutes for each course
        Map<Long, TimeResponse> courseTimeMap = new HashMap<>();

        // Process each time entry
        for (TimeEntry timeEntry : timeEntries) {
            Course course = timeEntry.getCourse();
            long courseId = course.getCourseId();
            String courseName = course.getCourseName();
            long durationInMinutes = timeEntry.getDurationInMinutes();

            // If the course is already in the map, update the total minutes
            if (courseTimeMap.containsKey(courseId)) {
                TimeResponse existingResponse = courseTimeMap.get(courseId);
                long updatedTotalMinutes = existingResponse.getTotalMinutesWorked() + durationInMinutes;
                existingResponse.setTotalMinutesWorked(updatedTotalMinutes);
            } else {
                // Otherwise, add a new entry to the map
                TimeResponse timeResponse = new TimeResponse(timeEntry.getTimeEntryId(), courseId, courseName, durationInMinutes, timeEntry.getStartDateTime());
                courseTimeMap.put(courseId, timeResponse);
            }
        }

        // Convert the map values to a list
        List<TimeResponse> timeResponses = new ArrayList<>(courseTimeMap.values());

        // Return the response message with the aggregated time entries
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
                        timeEntry.getTimeEntryId(),
                        timeEntry.getCourse().getCourseId(),
                        timeEntry.getCourse().getCourseName(),
                        timeEntry.getDurationInMinutes(),
                        timeEntry.getStartDateTime()))
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
            timeEntryRepository.save(timeEntryToUpdate);
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

    public ResponseMessage<TimeResponse> deleteTimeEntry(Long timeId, HttpServletRequest httpServletRequest) {

        User user = getUserFromRequest(httpServletRequest);

        TimeEntry timeEntryToDelete = methodHelper.isEntryTimeExist(timeId);
        if (user.getTimeEntries().contains(timeEntryToDelete)) {
            timeEntryRepository.delete(timeEntryToDelete);
            return ResponseMessage.<TimeResponse>builder().message(SuccessMessages.TIME_DELETE)
                    .httpStatus(HttpStatus.NO_CONTENT)
                    .object(timeEntryMapper.mapTimeEntryToTimeResponse(timeEntryToDelete, timeEntryToDelete.getDurationInMinutes())).build();
        } else throw new BadRequestException(ErrorMessages.NOT_PERMITTED_TO_DELETE_TIME_ENTRY);

    }
}



