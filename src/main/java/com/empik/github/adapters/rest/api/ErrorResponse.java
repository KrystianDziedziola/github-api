package com.empik.github.adapters.rest.api;

import lombok.AllArgsConstructor;
import lombok.Getter;

import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
class ErrorResponse {

    private final HttpStatus status;
    private final String message;
}
