package com.dev02.TimeTrackingApp.payload.response;

import com.dev02.TimeTrackingApp.entity.TimeEntry;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CourseResponse {

    private Long id;
    private String courseName;
    private String description;
    private List<TimeEntry> timeEntry;
}
