package com.projects.lms_server.advices;

import com.projects.lms_server.exceptions.ResourceAlreadyExistsException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {


    @ExceptionHandler(Exception.class)
    public ResponseEntity<APIResponse<?>> handler(Exception ex) {
        APIError err = APIError.builder()
                .message(ex.getMessage())
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .build();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new APIResponse<>(err));
    }

    @ExceptionHandler(ResourceAlreadyExistsException.class)
    public ResponseEntity<APIResponse<?>> conflictHandler(ResourceAlreadyExistsException ex) {
        APIError err = APIError.builder()
                .message(ex.getMessage())
                .status(HttpStatus.CONFLICT)
                .build();
    return ResponseEntity.status(HttpStatus.CONFLICT).body(new APIResponse<>(err));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<APIResponse<?>> inputNotValidHandler(MethodArgumentNotValidException ex) {
        APIError err = APIError.builder()
                .message(ex.getMessage())
                .status(HttpStatus.BAD_REQUEST)
                .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new APIResponse<>(err));
    }


}
