package com.telus.dsu.libraryapi.exception;

import javax.validation.ConstraintViolationException;

import com.telus.dsu.libraryapi.util.Constants;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler({ResourceNotFoundException.class})
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    @ResponseBody
    public ResponseEntity<Object> handleHttpResourceNotFoundException(ResourceNotFoundException exception){
        log.error("ResourceNotFoundException ", exception);
        Error error = new Error();
        error.setCode(Constants.ERROR_CODE_RESOURCE_NOT_FOUND);
        error.setMessage(exception.getMessage());

        log.error("handleHttpResourceNotFoundException(): " + error);
        return new ResponseEntity<>(error,createHeader(), HttpStatus.NOT_FOUND );
    }

    @ExceptionHandler({ResourceNotCreatedException.class})
    @ResponseStatus(value = HttpStatus.NOT_IMPLEMENTED)
    @ResponseBody
    public ResponseEntity<Object> handleHttpResourceNotCreatedException(ResourceNotCreatedException exception){
        log.error("ResourceNotCreatedException ", exception);
        Error error = new Error();
        error.setCode(Constants.ERROR_CODE_RESOURCE_NOT_CREATED);
        error.setMessage(exception.getMessage());

        log.error("handleHttpResourceNotCreatedException(): " + error);
        return new ResponseEntity<>(error, createHeader(), HttpStatus.NOT_IMPLEMENTED);
    }

    private HttpHeaders createHeader() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=UTF-8");
        return headers;
    }
}
