package com.br.hobbie.modules.authentication.http.controllers;

import com.br.hobbie.modules.authentication.http.dtos.request.AuthenticationRequest;
import com.br.hobbie.modules.authentication.http.dtos.response.LoginResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;

@RestController
@RequestMapping("/v1/api/auth")
@RequiredArgsConstructor
@Slf4j
public class UsernamePasswordAuthenticationController {
    private final Keycloak keycloak;
    @Value("${hobbie.authentication.keycloak.uri-scheme}")
    private String keycloakUriScheme;
    @Value("${hobbie.authentication.keycloak.auth-client-id}")
    private String clientId;

    @PostMapping("/username-password")
    public ResponseEntity<?> handle(@Valid @RequestBody AuthenticationRequest request) {
        final String URL = keycloakUriScheme + "/protocol/openid-connect/token";
        log.info("URL: {}", URL);
        new HttpHeaders().setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        var authRequest = new LinkedMultiValueMap<String, String>();
        authRequest.add("client_id", clientId);
        authRequest.add("username", request.email());
        authRequest.add("password", request.password());
        authRequest.add("grant_type", OAuth2Constants.PASSWORD);
        var httpClient = new RestTemplate();

        log.info("Request: {}", authRequest);

        try {
            var response = httpClient.postForEntity(URL, authRequest, KeycloakResponse.class);
            log.info("Response: {}", response);

            final var body = Objects.requireNonNull(response.getBody());
            return ResponseEntity.ok(new LoginResponse(body.access_token(), body.refresh_token()));
        } catch (Exception e) {
            log.error("Error: {}", e.getMessage());
            throw new UsernameNotFoundException("Invalid Credentials");
        }
    }

}

record KeycloakResponse(
        String access_token,
        String refresh_token
) {
}
