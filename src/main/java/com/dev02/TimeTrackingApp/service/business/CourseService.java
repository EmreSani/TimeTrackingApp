package com.dev02.TimeTrackingApp.service.business;

import com.dev02.TimeTrackingApp.entity.Course;
import com.dev02.TimeTrackingApp.entity.User;
import com.dev02.TimeTrackingApp.exception.BadRequestException;
import com.dev02.TimeTrackingApp.payload.mappers.CourseMapper;
import com.dev02.TimeTrackingApp.payload.messages.ErrorMessages;
import com.dev02.TimeTrackingApp.payload.messages.SuccessMessages;
import com.dev02.TimeTrackingApp.payload.request.CourseRequest;
import com.dev02.TimeTrackingApp.payload.response.CourseResponse;
import com.dev02.TimeTrackingApp.payload.response.ResponseMessage;
import com.dev02.TimeTrackingApp.payload.response.UserResponse;
import com.dev02.TimeTrackingApp.repository.CourseRepository;
import com.dev02.TimeTrackingApp.service.helper.MethodHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CourseService {

    private final CourseRepository courseRepository;
    private final CourseMapper courseMapper;
    private final MethodHelper methodHelper;

    public ResponseMessage<CourseResponse> createCourse(CourseRequest courseRequest) {

        Course course = courseMapper.mapCourseRequestToCourse(courseRequest);
        course.setCourseCreateTime(LocalDateTime.now());

        courseRepository.save(course);

        return ResponseMessage.<CourseResponse>builder().message(SuccessMessages.COURSE_CREATE)
                .httpStatus(HttpStatus.CREATED).
                object(courseMapper.mapCourseToCourseResponse(course)).build();
    }

    public ResponseEntity<CourseResponse> getCourseById(Long courseId) {

        Course course = methodHelper.isCourseExist(courseId);

        return ResponseEntity.ok(courseMapper.mapCourseToCourseResponse(course));

    }

    public ResponseEntity<CourseResponse> deleteCourseById(Long courseId, HttpServletRequest request) {

        String userName = (String) request.getAttribute("username");
        User user = methodHelper.findByUsername(userName);

        Course courseToDelete = methodHelper.isCourseExist(courseId);
        if (user.getCourses().contains(courseToDelete)) {
            courseRepository.delete(courseToDelete);
            return ResponseEntity.ok(courseMapper.mapCourseToCourseResponse(courseToDelete));
        } else throw new BadRequestException(ErrorMessages.NOT_PERMITTED_TO_DELETE_COURSE);

    }

    public ResponseMessage<Set<CourseResponse>> getCoursesByUser(HttpServletRequest request) {

        String username = (String) request.getAttribute("username");
        User user = methodHelper.findByUsername(username);

        Set<Course> courseList = user.getCourses();

        // Kursları CourseResponse'a dönüştür
        Set<CourseResponse> courseResponseSet = courseList.stream()
                .map(courseMapper::mapCourseToCourseResponse)
                .collect(Collectors.toSet());

        // Dönüştürülen kursları döndür
        return ResponseMessage.<Set<CourseResponse>>builder()
                .message(SuccessMessages.COURSES_FOUND)
                .object(courseResponseSet)
                .httpStatus(HttpStatus.OK)
                .build();
    }

    public ResponseMessage<CourseResponse> updateCourseById(Long courseId,
                                                            HttpServletRequest request,
                                                            CourseRequest courseRequest) {
        Course course = methodHelper.isCourseExist(courseId);

        if (!courseRequest.getCourseName().isEmpty()) {
            course.setCourseName(courseRequest.getCourseName());
        }
        if (!courseRequest.getDescription().isEmpty()) {
            course.setDescription(course.getDescription());
        }

        course.setCourseId(courseId);
        course.setCourseLastUpdateTime(LocalDateTime.now());

        Course updatedCourse = courseRepository.save(course);
        return ResponseMessage.<CourseResponse>builder().httpStatus(HttpStatus.OK).message(SuccessMessages.COURSES_FOUND)
                .object(courseMapper.mapCourseToCourseResponse(updatedCourse)).build();


    }

}


