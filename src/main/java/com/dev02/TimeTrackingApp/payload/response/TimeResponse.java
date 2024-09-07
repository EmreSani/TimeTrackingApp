package com.dev02.TimeTrackingApp.payload.response;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TimeResponse {

    private Long courseId;
    private String courseName;
    private long totalMinutesWorked;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
}
