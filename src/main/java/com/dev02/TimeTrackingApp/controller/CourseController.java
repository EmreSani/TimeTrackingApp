package com.dev02.TimeTrackingApp.controller;

import com.dev02.TimeTrackingApp.payload.request.CourseRequest;
import com.dev02.TimeTrackingApp.payload.response.CourseResponse;
import com.dev02.TimeTrackingApp.payload.response.ResponseMessage;
import com.dev02.TimeTrackingApp.service.business.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

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

}
