package com.dev02.TimeTrackingApp.repository;

import com.dev02.TimeTrackingApp.entity.Course;
import com.dev02.TimeTrackingApp.entity.TimeEntry;
import com.dev02.TimeTrackingApp.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TimeEntryRepository extends JpaRepository<TimeEntry, Long> {

    List<TimeEntry> findByUserAndStartDateTimeBetween(User user, LocalDateTime startDateTime, LocalDateTime endDateTime);


    List<TimeEntry> findByUserAndCourseAndStartDateTimeBetween(User user, Course course, LocalDateTime startDateTime, LocalDateTime endDateTime);

    List<TimeEntry> findByUser(User user);
}
