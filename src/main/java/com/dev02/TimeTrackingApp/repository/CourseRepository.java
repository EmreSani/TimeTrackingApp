package com.dev02.TimeTrackingApp.repository;

import com.dev02.TimeTrackingApp.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseRepository extends JpaRepository<Course, Long> {
}
