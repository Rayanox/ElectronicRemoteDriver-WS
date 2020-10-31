package main.controllers;

import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

import org.apache.tomcat.websocket.AuthenticationException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import main.controllers.common.AbstractController;
import main.model.ErrorBuilder;

@RestController
public class UtilsController extends AbstractController{


    @GetMapping(value = "/getSecondsFromHourTime")
    public String getSecondsFromHourTime(@RequestParam(value = "token") String token, @RequestParam(value = "hourTime") String hourTime) throws AuthenticationException {
    	String failedAuthMessage = processAuthorization(token);
    	if(failedAuthMessage != null)
    		return failedAuthMessage;
    	
    	try {
    		LocalTime alarmTime = LocalTime.parse(hourTime);
    		return String.valueOf(LocalTime.now().until(alarmTime, ChronoUnit.SECONDS));
    	}catch(Exception e) {
    		return ErrorBuilder.buildError(formatStringException(e));
    	}
    }
    
}
