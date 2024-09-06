package com.dev02.TimeTrackingApp.controller;

import com.dev02.TimeTrackingApp.payload.request.TimeEntryRequest;
import com.dev02.TimeTrackingApp.service.business.TimeEntryService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequestMapping("/course")
@RequiredArgsConstructor
public class TimeEntryController {

    private final TimeEntryService timeEntryService;

    //todo: günlük, aylık ve haftalık filtrelerle spesifik bi kursa ait saat bilgilerini getiren method lazım.
    //todo:update, delete

    @PostMapping("/save")
    @PreAuthorize("hasAnyAuthority('ADMIN','USER')")
    public ResponseMessage<TimeResponse> addNewTimeEntry(@RequestBody @Valid TimeEntryRequest timeEntryRequest
    , HttpServletRequest httpRequest){

        return timeEntryService.addNewTimeEntry(timeEntryRequest, httpRequest);

    }




}
