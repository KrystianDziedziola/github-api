package com.empik.github;

import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

import javax.sql.DataSource;

@Configuration
@Profile("test")
class TestDatabaseConfiguration {

    @Bean
    public PostgreSQLContainer postgreSQLContainer() {
        final DockerImageName imageName = DockerImageName.parse("postgres");
        final PostgreSQLContainer postgreSQLContainer = new PostgreSQLContainer(imageName).withUsername("postgres")
                                                                                          .withPassword("postgres")
                                                                                          .withDatabaseName("github");
        postgreSQLContainer.start();
        return postgreSQLContainer;
    }

    @Bean
    public DataSource dataSource(final PostgreSQLContainer postgreSQLContainer) {
        final DataSourceBuilder ds = DataSourceBuilder.create();
        ds.url(postgreSQLContainer.getJdbcUrl());
        ds.username(postgreSQLContainer.getUsername());
        ds.password(postgreSQLContainer.getPassword());
        ds.driverClassName(postgreSQLContainer.getDriverClassName());
        return ds.build();
    }
}
