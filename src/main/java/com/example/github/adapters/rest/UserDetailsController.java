package com.example.github.adapters.rest;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.github.application.UserDetailsService;
import com.example.github.domain.CalculationsException;
import com.example.github.domain.User;

import java.net.URL;
import java.time.OffsetDateTime;

@RestController
@RequestMapping(value = "/users", produces = MediaType.APPLICATION_JSON_VALUE)
//todo: swagger
//@Api(tags = "", value = "")
@AllArgsConstructor
class UserDetailsController {

    private final UserDetailsService userDetailsService;

    //    todo: add exception handler

    //    todo:swagger
    //    @ApiOperation(value = "")
    @GetMapping(value = "/{login}")
    public UserDetailsResponse getUserDetails(@PathVariable final String login) throws CalculationsException {
        final User userDetails = userDetailsService.getUserDetails(login);
        return UserDetailsResponse.valueOf(userDetails);
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

