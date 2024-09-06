package com.dev02.TimeTrackingApp.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
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

    @ManyToOne
    private User user;

    @OneToOne
    private Course course;

    private double hours; //0.0

    private Long durationInMinutes;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime startDateTime;

    private double dailyHours; //35.5
    private double weeklyHours;
    private double monthlyHours;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime endDateTime;

}
