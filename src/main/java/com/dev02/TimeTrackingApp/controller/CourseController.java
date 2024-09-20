package com.dev02.TimeTrackingApp.controller;

import com.dev02.TimeTrackingApp.payload.request.CourseRequest;
import com.dev02.TimeTrackingApp.payload.response.CourseResponse;
import com.dev02.TimeTrackingApp.payload.response.ResponseMessage;
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
@CrossOrigin(origins = "http://localhost:5173")
public class CourseController {

    private final CourseService courseService;


    // http://localhost:8080/course/save + POST
    @PostMapping("/save")
    @PreAuthorize("hasAnyAuthority('ADMIN','USER')")
    public ResponseMessage<CourseResponse> createCourse(@RequestBody @Valid CourseRequest courseRequest, HttpServletRequest httpServletRequest){
        return courseService.createCourse(courseRequest, httpServletRequest);
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

    // http://localhost:8080/course/updateCourse/{courseId} + GET
    @PutMapping("/updateCourse/{courseId}")
    @PreAuthorize("hasAnyAuthority('ADMIN','USER')")
    public ResponseMessage<CourseResponse> updateCourseById(@PathVariable Long courseId,
                                                            HttpServletRequest request,
                                                            @RequestBody CourseRequest courseRequest){

        return courseService.updateCourseById(courseId,request,courseRequest);
    }


}
