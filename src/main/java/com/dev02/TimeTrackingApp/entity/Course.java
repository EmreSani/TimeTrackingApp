package com.dev02.TimeTrackingApp.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity

@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class Course {

    //bu bir yorum satırıdır...


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long courseId;

    @Column(nullable = false)
    private String courseName;

    private String description;

    @OneToMany(mappedBy = "course", cascade = CascadeType.REMOVE)
    private List<TimeEntry> timeEntries;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDateTime courseCreateTime;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDateTime courseLastUpdateTime;

}
