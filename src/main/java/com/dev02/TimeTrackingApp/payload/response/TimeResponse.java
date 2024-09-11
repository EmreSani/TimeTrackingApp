package com.dev02.TimeTrackingApp.payload.response;

import lombok.*;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TimeResponse {

    private Long courseId;
    private String courseName;
    private long totalMinutesWorked;

}
