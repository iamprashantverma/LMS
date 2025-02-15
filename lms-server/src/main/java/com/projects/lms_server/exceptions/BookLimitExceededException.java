package com.projects.lms_server.exceptions;

public class BookLimitExceededException extends RuntimeException {
    public BookLimitExceededException(String message) {
        super(message);
    }
}
