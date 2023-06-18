package com.myk.countryklasha.common;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@Getter
@ToString
public class ErrorResponse {

    private HttpStatus status;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    private LocalDateTime timeStamp;
    private String message;
    private String debugMessage;

    public ErrorResponse(){
        timeStamp = LocalDateTime.now();
    }
}