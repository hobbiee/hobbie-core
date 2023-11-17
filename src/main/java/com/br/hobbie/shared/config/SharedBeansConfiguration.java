package com.br.hobbie.shared.config;

import org.flywaydb.core.Flyway;
import org.keycloak.admin.client.Keycloak;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import javax.sql.DataSource;

@Configuration
public class SharedBeansConfiguration {

    @Bean
    @Profile({"prod", "dev"})
    public Flyway flyway(DataSource dataSource) {
        var flyway = Flyway.configure()
                .dataSource(dataSource)
                .locations("classpath:db/migration")
                .load();
        flyway.repair();
        flyway.migrate();
        return flyway;
    }

    @Bean
    public Keycloak keycloak() {
        return Keycloak.getInstance(
                "http://localhost:8080/",
                "hobbie-realm",
                "admin",
                "admin",
                "hobbie-server");
    }
}