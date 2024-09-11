package com.dev02.TimeTrackingApp.service.validator;

import com.dev02.TimeTrackingApp.entity.TimeEntry;
import com.dev02.TimeTrackingApp.entity.User;
import com.dev02.TimeTrackingApp.exception.BadRequestException;
import com.dev02.TimeTrackingApp.payload.messages.ErrorMessages;
import com.dev02.TimeTrackingApp.repository.TimeEntryRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@AllArgsConstructor
public class TimeEntryValidator {

    private final TimeEntryRepository timeEntryRepository;

    // Checks if the start time is after or equal to stop time
    public boolean checkTime(LocalDateTime start, LocalDateTime stop) {
        return start.isAfter(stop) || start.equals(stop);
    }

    // Throws exception if start time is not valid compared to stop time
    public void checkTimeWithException(LocalDateTime start, LocalDateTime stop) {
        if (checkTime(start, stop)) {
            throw new BadRequestException(ErrorMessages.START_OR_END_TIME_CONFLICT);
        }
    }

    //NOT: Yeni time entryler daha ayrıntılı kontrollerden geçtiği için buna gerek kalmadı
//    // Check for overlapping time entries within existing user time entries
//    public boolean isOverlappingTimeEntry(User user, LocalDateTime startDateTime, LocalDateTime endDateTime, List<TimeEntry> existingEntries) {
//        for (TimeEntry entry : existingEntries) {
//            LocalDateTime existingStart = entry.getStartDateTime();
//            LocalDateTime existingEnd = entry.getEndDateTime();
//
//            // Check if there is an overlap:
//            if (startDateTime.isBefore(existingEnd) && endDateTime.isAfter(existingStart)) {
//                return true;
//            }
//        }
//        return false;
//    }

    // Method to check for time conflicts and throw an exception if conflict exists
    public void checkTimeConflicts(List<TimeEntry> existingEntries, LocalDateTime requestStart, LocalDateTime requestEnd) {
        for (TimeEntry existingEntry : existingEntries) {
            LocalDateTime existingStart = existingEntry.getStartDateTime();
            LocalDateTime existingEnd = existingEntry.getEndDateTime();

            // Full overlap or partial overlap checks
            if ((requestStart.isBefore(existingEnd) && requestEnd.isAfter(existingStart)) ||
                    requestStart.equals(existingStart) ||
                    requestEnd.equals(existingEnd)) {
                throw new BadRequestException(ErrorMessages.TIME_OVERLAPS);
            }
        }
    }

    // Main method to be called in service class
    public void validateTimeEntry(User user, LocalDateTime startDateTime, LocalDateTime endDateTime) {
        // Fetch existing entries for the user
        List<TimeEntry> existingEntries = timeEntryRepository.findByUser(user);

        // Validate the start and end times
        checkTimeWithException(startDateTime, endDateTime);

        // Check for time conflicts
        checkTimeConflicts(existingEntries, startDateTime, endDateTime);
    }
}

