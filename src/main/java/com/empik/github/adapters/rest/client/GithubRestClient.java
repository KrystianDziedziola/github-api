package com.empik.github.adapters.rest.client;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
public class GithubRestClient {

    final RestTemplate restTemplate;

    @Value("${github.users-url}")
    private String usersUrl;

    public GithubUserDetailsResponse getUserDetails(final String login) {
        return restTemplate.getForObject(usersUrl, GithubUserDetailsResponse.class, login);
    }
}
