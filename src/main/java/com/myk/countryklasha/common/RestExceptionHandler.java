package com.myk.countryklasha.common;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.UnexpectedTypeException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.reactive.result.method.annotation.ResponseEntityExceptionHandler;
import retrofit2.HttpException;

import java.util.Objects;


@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice
@Slf4j
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ConstraintViolationException.class)
    protected ResponseEntity<Object> handleConstraintViolation(ConstraintViolationException ex) {
        ErrorResponse response = new ErrorResponse();
        response.setStatus(HttpStatus.BAD_REQUEST);
        response.setMessage("Validation error: " + ex.getMessage());
        return buildResponseEntity(response);
    }

    @ExceptionHandler(CustomException.class)
    protected ResponseEntity<Object> handleCustomException(CustomException ex) {
        ErrorResponse response = new ErrorResponse();
        response.setStatus(HttpStatus.BAD_REQUEST);
        response.setMessage("Validation error: " + ex.getMessage());
        response.setDebugMessage(ex.debugMessage);
        return buildResponseEntity(response);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    protected ResponseEntity<Object> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException ex,
                                                                      WebRequest request) {
        ErrorResponse response = new ErrorResponse();
        response.setStatus(HttpStatus.BAD_REQUEST);
        response.setMessage(String.format("The parameter '%s' of value '%s' could not be converted to type '%s'", ex.getName(), ex.getValue(), ex.getRequiredType().getSimpleName()));
        response.setDebugMessage(ex.getMessage());
        return buildResponseEntity(response);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    protected ResponseEntity<Object> handleConstraintViolation(MissingServletRequestParameterException ex) {
        ErrorResponse response = new ErrorResponse();
        response.setStatus(HttpStatus.BAD_REQUEST);
        response.setMessage(ex.getMessage());
        return buildResponseEntity(response);
    }

    @ExceptionHandler(UnexpectedTypeException.class)
    protected ResponseEntity<Object> handleConstraintViolation(UnexpectedTypeException ex) {
        ErrorResponse response = new ErrorResponse();
        response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        response.setMessage(ex.getMessage());
        return buildResponseEntity(response);
    }




    @ExceptionHandler(value = HttpException.class)
    public ResponseEntity<Object> exception(HttpServletRequest request, HttpException exception) {


            ErrorResponse response = new ErrorResponse();

            switch (exception.code()){
                case 400 -> {
                    response.setStatus(HttpStatus.BAD_REQUEST);
                    response.setMessage("Bad request: " + Objects.requireNonNull(exception.response()).message());
                    return buildResponseEntity(response);
                }
                case 401 -> {
                    response.setStatus(HttpStatus.UNAUTHORIZED);
                    response.setMessage("Unauthorized: " + Objects.requireNonNull(exception.response()).message());
                    return buildResponseEntity(response);
                }
                case 403 -> {
                    response.setStatus(HttpStatus.FORBIDDEN);
                    response.setMessage("Forbidden: " + Objects.requireNonNull(exception.response()).message());
                    return buildResponseEntity(response);
                }
                case 404 -> {
                    response.setStatus(HttpStatus.NOT_FOUND);
                    response.setMessage("Resource not found: " + Objects.requireNonNull(exception.response()).message());
                    return buildResponseEntity(response);
                }
                case 405 -> {
                    response.setStatus(HttpStatus.METHOD_NOT_ALLOWED);
                    response.setMessage("Method not allowed: " + Objects.requireNonNull(exception.response()).message());
                    return buildResponseEntity(response);
                }
                case 409 -> {
                    response.setStatus(HttpStatus.CONFLICT);
                    response.setMessage("Conflict: " + Objects.requireNonNull(exception.response()).message());
                    return buildResponseEntity(response);
                }
                case 503 -> {
                    response.setStatus(HttpStatus.SERVICE_UNAVAILABLE);
                    response.setMessage("Service unavailable: " + Objects.requireNonNull(exception.response()).message());
                    return buildResponseEntity(response);
                }
                default -> {
                    response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
                    response.setMessage("Internal server error: " + Objects.requireNonNull(exception.response()).message());
                    return buildResponseEntity(response);
                }

            }

    }

    private ResponseEntity<Object> buildResponseEntity(ErrorResponse errorResponse){
        return new ResponseEntity<Object>(errorResponse, errorResponse.getStatus());
    }

}