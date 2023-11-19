package com.br.hobbie.modules.authentication.http.controllers;

import com.br.hobbie.modules.authentication.http.dtos.request.AuthenticationRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/v1/api/auth")
@RequiredArgsConstructor
public class UsernamePasswordAuthenticationController {
    private final Keycloak keycloak;
    @Value("${hobbie.authentication.keycloak.uri-scheme}")
    private String keycloakUriScheme;
    @Value("${hobbie.authentication.keycloak.client-id}")
    private String clientId;

    @PostMapping("/username-password")
    public ResponseEntity<?> handle(@Valid @RequestBody AuthenticationRequest request) {
        final String URL = keycloakUriScheme + "/protocol/openid-connect/token";
        new HttpHeaders().setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        var authRequest = new LinkedMultiValueMap<String, String>();
        authRequest.add("client_id", clientId);
        authRequest.add("username", request.email());
        authRequest.add("password", request.password());
        authRequest.add("grant_type", OAuth2Constants.PASSWORD);

        var httpClient = new RestTemplate();

        try {
            var response = httpClient.postForEntity(URL, authRequest, String.class);
            return ResponseEntity.status(response.getStatusCode()).body(response.getBody());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

}
