package com.br.hobbie.shared.config;

import org.flywaydb.core.Flyway;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import javax.sql.DataSource;

@Configuration
public class SharedBeansConfiguration {

    @Value("${hobbie.authentication.keycloak.secret}")
    private String keycloakSecret;
    @Value("${hobbie.authentication.keycloak.client-id}")
    private String clientId;
    @Value("${hobbie.authentication.keycloak.realm}")
    private String realm;
    @Value("${hobbie.authentication.keycloak.server-url}")
    private String serverUrl;

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
        return KeycloakBuilder.builder()
                .serverUrl(serverUrl)
                .realm(realm)
                .grantType(OAuth2Constants.CLIENT_CREDENTIALS)
                .clientId(clientId)
                .clientSecret(keycloakSecret)
                .build();
    }
}