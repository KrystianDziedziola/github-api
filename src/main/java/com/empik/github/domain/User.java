package com.empik.github.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.net.URL;
import java.time.OffsetDateTime;

@Builder
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class User {

    private final Long id;
    private final String login;
    private final String name;
    private final String type;
    private final URL avatarUrl;
    private final OffsetDateTime createdAt;
    private final Integer followersNumber;
    private final Integer publicRepositoriesNumber;

    public Double makeCalculations() throws CalculationsException {
        if (followersNumber == 0) {
            throw new CalculationsException(
                    "Calculations cannot be made. Division by zero is not possible. 'followersNumber' has '0' value");
        }
        return 6.0 / followersNumber * (2 + publicRepositoriesNumber);
    }
}
