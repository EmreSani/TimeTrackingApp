package com.dev02.TimeTrackingApp.payload.request;

import com.dev02.TimeTrackingApp.entity.TimeEntry;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CourseRequest {

    @NotNull(message = "please provide a course name.")
    private String courseName;

    private String description;

}
