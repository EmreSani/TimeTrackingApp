package com.dev02.TimeTrackingApp.service.business;

import com.dev02.TimeTrackingApp.entity.Course;
import com.dev02.TimeTrackingApp.payload.mappers.CourseMapper;
import com.dev02.TimeTrackingApp.payload.messages.SuccessMessages;
import com.dev02.TimeTrackingApp.payload.request.CourseRequest;
import com.dev02.TimeTrackingApp.payload.response.CourseResponse;
import com.dev02.TimeTrackingApp.payload.response.ResponseMessage;
import com.dev02.TimeTrackingApp.repository.CourseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CourseService {

    private final CourseRepository courseRepository;
    private final CourseMapper courseMapper;

    public ResponseMessage<CourseResponse> createCourse(CourseRequest courseRequest) {

     Course course =  courseMapper.mapCourseRequestToCourse(courseRequest);

        courseRepository.save(course);

        return ResponseMessage.<CourseResponse>builder().message(SuccessMessages.COURSE_CREATE)
                .httpStatus(HttpStatus.CREATED).
                object(courseMapper.mapCourseToCourseResponse(course)).build();
    }

    //todo:1-get course by Id 2-updateCourseDetails 3-DeleteCourse

}
