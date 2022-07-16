package com.hkakar.projecttracking.exceptions;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalControllerExceptionHandler {

    @ExceptionHandler({ UserAlreadyExistsException.class, 
                        InternalServerErrorException.class })
    public ResponseEntity<Object> userExistsException(UserAlreadyExistsException ex) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", ex.getStatus());
        body.put("statusMessage", ex.getStatusMessage());
        body.put("message", ex.getMessage());
        return new ResponseEntity<Object>(body, HttpStatus.BAD_REQUEST);
    }
}
