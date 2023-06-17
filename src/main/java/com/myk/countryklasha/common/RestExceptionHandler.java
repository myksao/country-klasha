package com.myk.countryklasha.common;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.reactive.result.method.annotation.ResponseEntityExceptionHandler;
import retrofit2.HttpException;

import java.time.LocalDateTime;
import java.util.Map;

@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice
@Slf4j
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<Object> exception(HttpServletRequest request, Exception exception) {

        if(exception instanceof HttpException httpException) {
            ErrorResponse response = new ErrorResponse();
            if(httpException.code() == 404) {
                response.setStatus(HttpStatus.valueOf(httpException.code()));
                response.setMessage("Resource not found: " + httpException.response().message());
                return buildResponseEntity(response);
            }
            response.setStatus(HttpStatus.valueOf(httpException.code()));
            return buildResponseEntity(response);
        }

        if(exception instanceof MethodArgumentTypeMismatchException methodArgumentTypeMismatchException){
            ErrorResponse response = new ErrorResponse();
            response.setStatus(HttpStatus.BAD_REQUEST);
            response.setMessage(methodArgumentTypeMismatchException.getMessage());
            return buildResponseEntity(response);
        }

        if(exception instanceof  MissingServletRequestParameterException missingServletRequestParameterException ) {
            ErrorResponse response = new ErrorResponse();
            response.setStatus(HttpStatus.BAD_REQUEST);
            response.setMessage(missingServletRequestParameterException.getMessage());
            return buildResponseEntity(response);
        }

        ErrorResponse response = new ErrorResponse();
        response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        response.setMessage("Internal server error");
        return buildResponseEntity(response);
    }

    private ResponseEntity<Object> buildResponseEntity(ErrorResponse errorResponse){
        return new ResponseEntity<Object>(errorResponse, errorResponse.getStatus());
    }

}