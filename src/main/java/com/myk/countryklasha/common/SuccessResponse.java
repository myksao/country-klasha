package com.myk.countryklasha.common;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@Getter
@ToString
public class SuccessResponse {
    public boolean success;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss")
    private LocalDateTime timeStamp;
    public String message;
    public Object data;

    public SuccessResponse(){
        timeStamp = LocalDateTime.now();
    }
}
