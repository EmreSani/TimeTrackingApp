package com.dev02.TimeTrackingApp.payload.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TimeResponse {

    private Long courseId;
    private String courseName;
    private long totalMinutesWorked;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
}
