package com.hkakar.projecttracking.exceptions;

import org.springframework.http.HttpStatus;

public class UserAlreadyExistsException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    
    private int status;
    
    private String statusMessage;
    
    private String message;
    
    public UserAlreadyExistsException() {}

    public UserAlreadyExistsException(int status, String statusMessage, String message) {
        super();
        this.status = status;
        this.statusMessage = statusMessage;
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getStatusMessage() {
        return statusMessage;
    }

    public void setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
