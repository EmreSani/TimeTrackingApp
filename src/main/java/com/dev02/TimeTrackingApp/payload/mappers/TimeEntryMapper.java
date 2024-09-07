package com.dev02.TimeTrackingApp.payload.mappers;

import com.dev02.TimeTrackingApp.entity.TimeEntry;
import com.dev02.TimeTrackingApp.payload.response.TimeResponse;
import org.springframework.stereotype.Component;

@Component
public class TimeEntryMapper {

    public TimeResponse mapTimeEntryToTimeResponse(TimeEntry timeEntry, Long totalMinutes){

      return  TimeResponse.builder().endDateTime(timeEntry.getEndDateTime())
              .startDateTime(timeEntry.getStartDateTime())
              .courseName(timeEntry.getCourse().getCourseName())
              .courseId(timeEntry.getCourse().getCourseId())
              .totalMinutesWorked(totalMinutes).build();
    }
}
