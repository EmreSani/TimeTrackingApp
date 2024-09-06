package com.dev02.TimeTrackingApp.payload.mappers;

import com.dev02.TimeTrackingApp.entity.Course;
import com.dev02.TimeTrackingApp.payload.request.CourseRequest;
import com.dev02.TimeTrackingApp.payload.response.CourseResponse;
import org.springframework.stereotype.Component;

@Component
public class CourseMapper {

    public Course mapCourseRequestToCourse (CourseRequest courseRequest){

      return Course.builder().courseName(courseRequest.getCourseName())
                .description(courseRequest.getDescription()).build();

    }

    public CourseResponse mapCourseToCourseResponse (Course course){

        return CourseResponse.builder().courseName(course.getCourseName())
                .description(course.getDescription()).timeEntry(course.getTimeEntries()).build();

    }

}
