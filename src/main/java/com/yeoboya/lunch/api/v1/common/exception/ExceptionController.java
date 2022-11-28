package com.yeoboya.lunch.api.v1.common.exception;

import com.yeoboya.lunch.config.util.Helper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolationException;

import static org.springframework.http.HttpStatus.*;

@Slf4j
@RestControllerAdvice
public class ExceptionController {

    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ExceptionResponse methodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.error("MethodArgumentNotValidException", e);
        return ExceptionResponse.builder()
                .code(BAD_REQUEST.value())
                .message(BAD_REQUEST.getReasonPhrase())
                .validation(Helper.refineErrors(e))
                .build();
    }

    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler(value = ConstraintViolationException.class)
    protected ExceptionResponse constraintViolationException(ConstraintViolationException e) {
        log.error("ConstraintViolationException", e);
        return ExceptionResponse.builder()
                .code(BAD_REQUEST.value())
                .message(BAD_REQUEST.getReasonPhrase())
                .validation(Helper.refineErrors(e))
                .build();
    }


    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    protected ExceptionResponse httpMessageNotReadableException(HttpMessageNotReadableException e) {
        log.error("HttpMessageNotReadableException", e);
        return ExceptionResponse.builder()
                .code(BAD_REQUEST.value())
                .message(BAD_REQUEST.getReasonPhrase())
                .note("JSON 확인")
                .build();
    }

    @ResponseStatus(NOT_FOUND)
    @ExceptionHandler(EmptyResultDataAccessException.class)
    protected ExceptionResponse emptyResultDataAccessException(EmptyResultDataAccessException e){
        log.error("EmptyResultDataAccessException", e);
        return ExceptionResponse.builder()
                .code(NOT_FOUND.value())
                .message(NOT_FOUND.getReasonPhrase())
                .build();
    }

    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    protected ExceptionResponse httpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        log.error("HttpRequestMethodNotSupportedException", e);
        return ExceptionResponse.builder()
                .code(METHOD_NOT_ALLOWED.value())
                .message(METHOD_NOT_ALLOWED.getReasonPhrase())
                .build();
    }

    @ResponseStatus(CONFLICT)
    @ExceptionHandler(value = {DataIntegrityViolationException.class})
    protected ExceptionResponse dataIntegrityViolationException(DataIntegrityViolationException e) {
        log.error("DataIntegrityViolationException", e);
        return ExceptionResponse.builder()
                .code(CONFLICT.value())
                .message(CONFLICT.getReasonPhrase())
                .note(e.getMostSpecificCause().getMessage())
                .build();
    }

    @ExceptionHandler(LunchException.class)
    public ResponseEntity<ExceptionResponse> lunchException(LunchException e) {
        int statusCode = e.getStatusCode();

        ExceptionResponse body = ExceptionResponse.builder()
                .code(statusCode)
                .message(e.getMessage())
                .build();

        return ResponseEntity.status(statusCode).body(body);
    }

}