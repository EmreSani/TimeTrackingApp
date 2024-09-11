package com.dev02.TimeTrackingApp.controller;

import com.dev02.TimeTrackingApp.payload.request.TimeEntryRequest;
import com.dev02.TimeTrackingApp.payload.response.ResponseMessage;
import com.dev02.TimeTrackingApp.payload.response.TimeResponse;
import com.dev02.TimeTrackingApp.service.business.TimeEntryService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import java.util.List;

@RestController
@RequestMapping("/timeEntry")
@RequiredArgsConstructor
public class TimeEntryController {

    private final TimeEntryService timeEntryService;

    @PostMapping("/save")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public ResponseMessage<TimeResponse> addNewTimeEntry(@RequestBody @Valid TimeEntryRequest timeEntryRequest
    , HttpServletRequest httpRequest){
        return timeEntryService.addNewTimeEntry(timeEntryRequest, httpRequest);
    }

    @GetMapping("/getAllDailyTimeEntriesByUser")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public ResponseMessage<List<TimeResponse>> getAllDailyTimeEntriesByUser(HttpServletRequest request){
        return timeEntryService.getDailyTimeEntriesByUser(request);
    }

    @GetMapping("/getAllWeeklyTimeEntriesByUser")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public ResponseMessage<List<TimeResponse>> getAllWeeklyTimeEntriesByUser(HttpServletRequest request){
        return timeEntryService.getWeeklyTimeEntriesByUser(request);
    }

    @GetMapping("/getAllMonthlyTimeEntriesByUser")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public ResponseMessage<List<TimeResponse>> getAllMonthlyTimeEntriesByUser(HttpServletRequest request){
        return timeEntryService.getMonthlyTimeEntriesByUser(request);
    }

    @GetMapping("/getAllPreviousDayTimeEntriesByUser")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public ResponseMessage<List<TimeResponse>> getAllPreviousDayTimeEntriesByUser(HttpServletRequest request) {
        return timeEntryService.getPreviousDayTimeEntriesByUser(request);
    }

    @GetMapping("/getAllPreviousWeekTimeEntriesByUser")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public ResponseMessage<List<TimeResponse>> getAllPreviousWeekTimeEntriesByUser(HttpServletRequest request) {
        return timeEntryService.getPreviousWeekTimeEntriesByUser(request);
    }

    @GetMapping("/getAllPreviousMonthTimeEntriesByUser")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public ResponseMessage<List<TimeResponse>> getAllPreviousMonthTimeEntriesByUser(HttpServletRequest request) {
        return timeEntryService.getPreviousMonthTimeEntriesByUser(request);
    }

    @GetMapping("/allEntriesByUser")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public ResponseMessage<List<TimeResponse>> getAllTimeEntriesByUser(HttpServletRequest request){
        return timeEntryService.getAllTimeEntries(request);
    }

    @PutMapping("/updateTimeEntry")
    @PreAuthorize("hasAnyAuthority('ADMIN','USER')")
    public ResponseMessage<TimeResponse> updateTimeResponse(@RequestBody @Valid TimeEntryRequest timeEntryRequest,
                                                            Long timeEntryId,
                                                            HttpServletRequest request){
        return timeEntryService.updateTimeEntry(timeEntryRequest, timeEntryId, request);
    }

    @DeleteMapping("/deleteTimeEntry/{timeResponseId}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public ResponseMessage<TimeResponse> deleteTimeResponse(@PathVariable Long timeId,
                                                            HttpServletRequest httpServletRequest
                                                            ){

        return timeEntryService.deleteTimeEntry(timeId, httpServletRequest);

    }


}
