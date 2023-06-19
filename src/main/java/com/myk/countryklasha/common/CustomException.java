package com.myk.countryklasha.common;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

public class CustomException extends Exception {
    public HttpStatus status;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    public LocalDateTime timeStamp;
    public String message;
    public String debugMessage;
    public CustomException(String message, HttpStatus status, String debugMessage) {
        super(message);
        this.message = message;
        this.status = status;
        this.debugMessage = debugMessage;
        timeStamp = LocalDateTime.now();
    }
}
