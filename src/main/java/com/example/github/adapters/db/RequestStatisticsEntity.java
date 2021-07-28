package com.example.github.adapters.db;

import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@NoArgsConstructor
@Table(name = "request_statistics")
public class RequestStatisticsEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String login;
    private long requestCount;

    public static RequestStatisticsEntity first(final String login) {
        return new RequestStatisticsEntity(login, 1);
    }

    private RequestStatisticsEntity(final String login, final long requestCount) {
        this.login = login;
        this.requestCount = requestCount;
    }

    public void increaseRequestCount() {
        this.requestCount++;
    }
}
