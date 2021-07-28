package com.empik.github.adapters.rest.api;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;

import com.empik.github.application.UserDetailsService;
import com.empik.github.domain.CalculationsException;
import com.empik.github.domain.User;

import java.net.URL;
import java.time.OffsetDateTime;

@RestController
@RequestMapping(value = "/users", produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
class UserDetailsController {

    private final UserDetailsService userDetailsService;

    @GetMapping(value = "/{login}")
    public UserDetailsResponse getUserDetails(@PathVariable final String login) throws CalculationsException {
        final User userDetails = userDetailsService.getUserDetails(login);
        return UserDetailsResponse.valueOf(userDetails);
    }

    @ExceptionHandler(value = CalculationsException.class)
    ResponseEntity<ErrorResponse> handleCalculationsException(final CalculationsException exception) {
        final HttpStatus statusCode = HttpStatus.INTERNAL_SERVER_ERROR;
        final ErrorResponse errorResponse = new ErrorResponse(statusCode, exception.getMessage());
        return new ResponseEntity<>(errorResponse, statusCode);
    }

    @ExceptionHandler(value = HttpClientErrorException.class)
    ResponseEntity<ErrorResponse> handleHttpClientErrorException(final HttpClientErrorException exception) {
        final HttpStatus statusCode = exception.getStatusCode();
        final ErrorResponse errorResponse = new ErrorResponse(statusCode, exception.getMessage());
        return new ResponseEntity<>(errorResponse, statusCode);
    }

    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    @Builder
    @Getter
    static class UserDetailsResponse {

        private final Long id;
        private final String login;
        private final String name;
        private final String type;
        private final URL avatarUrl;
        private final OffsetDateTime createdAt;
        private final Double calculations;

        static UserDetailsResponse valueOf(final User user) throws CalculationsException {
            return UserDetailsResponse.builder()
                                      .id(user.getId())
                                      .login(user.getLogin())
                                      .name(user.getName())
                                      .type(user.getType())
                                      .avatarUrl(user.getAvatarUrl())
                                      .createdAt(user.getCreatedAt())
                                      .calculations(user.makeCalculations())
                                      .build();
        }
    }
}

