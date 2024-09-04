package com.dev02.TimeTrackingApp.entity;

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

    private LocalDateTime startDateTime;

    private LocalDateTime endDateTime;

}
