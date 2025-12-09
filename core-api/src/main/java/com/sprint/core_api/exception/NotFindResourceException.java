package com.sprint.core_api.exception;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class NotFindResourceException extends RuntimeException {
    public NotFindResourceException(String message) {
        super(message);
    }
}
