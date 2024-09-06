package com.dev02.TimeTrackingApp.controller;

import com.dev02.TimeTrackingApp.payload.request.CourseRequest;
import com.dev02.TimeTrackingApp.payload.response.CourseResponse;
import com.dev02.TimeTrackingApp.payload.response.ResponseMessage;
import com.dev02.TimeTrackingApp.payload.response.UserResponse;
import com.dev02.TimeTrackingApp.service.business.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Set;

@RestController
@RequestMapping("/course")
@RequiredArgsConstructor
public class CourseController {

    private final CourseService courseService;


    @PostMapping("/save")
    @PreAuthorize("hasAnyAuthority('ADMIN','USER')")
    public ResponseMessage<CourseResponse> createCourse(@RequestBody @Valid CourseRequest courseRequest){
        return courseService.createCourse(courseRequest);
    }

    // http://localhost:8080/course/{courseId} + GET
    @GetMapping("/{courseId}")
    @PreAuthorize("hasAnyAuthority('ADMIN','USER')")
    public ResponseEntity<CourseResponse> getCourseById(@PathVariable Long courseId) {
        return courseService.getCourseById(courseId);
    }

    // http://localhost:8080/course/{courseId} + DELETE
    @DeleteMapping("/{courseId}")
    @PreAuthorize("hasAnyAuthority('ADMIN','USER')")
    public ResponseEntity<CourseResponse> deleteCourseById(@PathVariable Long courseId, HttpServletRequest request) {
        return courseService.deleteCourseById(courseId, request);
    }

    // http://localhost:8080/course/getAllUsersCourses + GET
    @GetMapping("/getAllUsersCourses")
    @PreAuthorize("hasAnyAuthority('ADMIN','USER')")
    public ResponseMessage<Set<CourseResponse>> getCoursesByUser(HttpServletRequest request) {
        return courseService.getCoursesByUser(request);
    }

    @PutMapping("/updateCourse/{courseId}")
    @PreAuthorize("hasAnyAuthority('ADMIN','USER')")
    public ResponseMessage<CourseResponse> updateCourseById(@PathVariable Long courseId,
                                                            HttpServletRequest request,
                                                            @RequestBody CourseRequest courseRequest){

        return courseService.updateCourseById(courseId,request,courseRequest);
    }


}
