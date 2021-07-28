package com.empik.github.adapters.rest.client;

import lombok.Getter;
import lombok.NoArgsConstructor;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.net.URL;
import java.time.OffsetDateTime;

@Getter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class GithubUserDetailsResponse {

    private Long id;
    private String login;
    private String type;
    private String name;
    @JsonProperty("avatar_url")
    private URL avatarUrl;
    @JsonProperty("created_at")
    private OffsetDateTime createdAt;
    private Integer followers;
    @JsonProperty("public_repos")
    private Integer publicRepos;
}
