package com.dev02.TimeTrackingApp.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity

@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class TimeEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long timeEntryId;

    @ManyToOne//todo: json ignore
    @JsonIgnore
    //@JsonManagedReference
    private User user;

    @ManyToOne//todo: json ignore
    @JsonIgnore
    private Course course;


    private double hours; //0.0

    private Long durationInMinutes;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime startDateTime;

    private double dailyHours; //35.5
    private double weeklyHours;
    private double monthlyHours;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime endDateTime;

}
