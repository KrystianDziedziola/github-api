package com.empik.github.adapters.db;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RequestStatisticsRepository extends JpaRepository<RequestStatisticsEntity, Long> {

    Optional<RequestStatisticsEntity> findByLogin(String login);
}
