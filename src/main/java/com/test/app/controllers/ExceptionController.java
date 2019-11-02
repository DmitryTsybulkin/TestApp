package com.test.app.controllers;

import com.test.app.dtos.ErrorDTO;
import com.test.app.excpetions.EntryDuplicateException;
import com.test.app.excpetions.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.format.DateTimeParseException;

@RestControllerAdvice
public class ExceptionController {

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public ErrorDTO resourceNotFoundExceptionHandler(Exception e) {
        return new ErrorDTO(HttpStatus.NOT_FOUND.name(), e.getLocalizedMessage());
    }

    @ExceptionHandler(EntryDuplicateException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    @ResponseBody
    public ErrorDTO entryDuplicateExceptionHandler(Exception e) {
        return new ErrorDTO(HttpStatus.BAD_REQUEST.name(), e.getLocalizedMessage());
    }

    @ExceptionHandler(IllegalStateException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorDTO illegalStateExceptionHandler(Exception e) {
        return new ErrorDTO(HttpStatus.BAD_REQUEST.name(), e.getLocalizedMessage());
    }

    @ExceptionHandler(DateTimeParseException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorDTO dateTimeParseExceptionHandler(Exception e) {
        return new ErrorDTO(HttpStatus.BAD_REQUEST.name(), e.getLocalizedMessage());
    }

}
