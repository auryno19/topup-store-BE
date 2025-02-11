package com.example.fufastore.util;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleGlobalException(Exception error) {
        return ResponseUtil.generateErrorResponse("An unexcepted error occured", error.getMessage(),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
