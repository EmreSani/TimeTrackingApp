package com.dev02.TimeTrackingApp.service.validator;


import com.dev02.TimeTrackingApp.exception.BadRequestException;
import org.springframework.stereotype.Component;

import java.time.LocalTime;


@Component
public class DateTimeValidator {

    public boolean checkTime(LocalTime start, LocalTime stop){
        return start.isAfter(stop) || start.equals(stop) ;
    }

    public void checkTimeWithException(LocalTime start, LocalTime stop){
        if(checkTime(start,stop)){
            throw new BadRequestException("todo: doldur.");
        }
    }


}
