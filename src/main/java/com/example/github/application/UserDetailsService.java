package com.example.github.application;

import lombok.AllArgsConstructor;

import org.springframework.stereotype.Service;

import com.example.github.adapters.db.RequestStatisticsEntity;
import com.example.github.adapters.db.RequestStatisticsRepository;
import com.example.github.adapters.rest.client.GithubRestClient;
import com.example.github.adapters.rest.client.GithubUserDetailsResponse;
import com.example.github.domain.User;

import java.util.function.Consumer;

@Service
@AllArgsConstructor
public class UserDetailsService {

    private final GithubRestClient githubRestClient;
    private final RequestStatisticsRepository requestStatisticsRepository;

    public User getUserDetails(final String login) {
        final GithubUserDetailsResponse userDetails = githubRestClient.getUserDetails(login);

        increaseStatistics(login);

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

    private void increaseStatistics(final String login) {
        requestStatisticsRepository.findByLogin(login)
                                   .ifPresentOrElse(increaseRequestStatistics(), saveFirstRequestStatistic(login));
    }

    private Consumer<RequestStatisticsEntity> increaseRequestStatistics() {
        return entity -> {
            entity.increaseRequestCount();
            requestStatisticsRepository.save(entity);
        };
    }

    private Runnable saveFirstRequestStatistic(final String login) {
        return () -> {
            final RequestStatisticsEntity first = RequestStatisticsEntity.first(login);
            requestStatisticsRepository.save(first);
        };
    }
}
