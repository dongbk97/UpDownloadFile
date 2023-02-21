package com.exceptionhandle;

import org.apache.tomcat.util.http.fileupload.impl.SizeLimitExceededException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDate;

@RestControllerAdvice
public class ExceptionHandle extends ResponseEntityExceptionHandler {


    @ExceptionHandler(SizeLimitExceededException.class)
    public ResponseEntity<?> handleException() {

        ExResponse response = new ExResponse(LocalDate.now(),"File size is too large");
        return new ResponseEntity(response,HttpStatus.PAYLOAD_TOO_LARGE);
    }

}
