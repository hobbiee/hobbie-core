package com.br.hobbie.shared.config;

import org.flywaydb.core.Flyway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import javax.sql.DataSource;

@Configuration
public class SharedBeansConfiguration {

    @Bean
    @Profile({"default", "dev"})
    public Flyway flyway(DataSource dataSource) {
        var flyway = Flyway.configure()
                .dataSource(dataSource)
                .locations("classpath:db/migration")
                .load();
        flyway.repair();
        flyway.migrate();
        return flyway;
    }
}
