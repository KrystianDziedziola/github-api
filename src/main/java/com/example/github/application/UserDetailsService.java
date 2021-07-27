package com.example.github.application;

import lombok.AllArgsConstructor;

import org.springframework.stereotype.Service;

import com.example.github.adapters.rest.client.GithubRestClient;
import com.example.github.adapters.rest.client.GithubUserDetailsResponse;
import com.example.github.domain.User;

@Service
@AllArgsConstructor
public class UserDetailsService {

    private final GithubRestClient githubRestClient;

    public User getUserDetails(final String login) {
        final GithubUserDetailsResponse userDetails = githubRestClient.getUserDetails(login);

//        todo: increase statistics

        return User.builder()
                   .id(userDetails.getId())
                   .login(userDetails.getLogin())
                   .name(userDetails.getName())
                   .type(userDetails.getType())
                   .avatarUrl(userDetails.getAvatarUrl())
                   .createdAt(userDetails.getCreatedAt())
                   .followersNumber(userDetails.getFollowers())
                   .publicRepositoriesNumber(userDetails.getPublicRepos())
                   .build();
    }

}
