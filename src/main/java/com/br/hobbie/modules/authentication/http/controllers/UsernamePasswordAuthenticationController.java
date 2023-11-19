package com.br.hobbie.modules.authentication.http.controllers;

import com.br.hobbie.modules.authentication.http.dtos.request.AuthenticationRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
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
    @Value("${hobbie.authentication.keycloak.auth-url}")
    private String authUrl;

    @Value("${hobbie.authentication.keycloak.client-id}")
    private String clientId;

    @Value("${hobbie.authentication.keycloak.grant-type}")
    private String grantType;

    @PostMapping("/username-password")
    public ResponseEntity<String> handle(@Valid @RequestBody AuthenticationRequest request) {
        new HttpHeaders().setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        var authRequest = new LinkedMultiValueMap<String, String>();
        authRequest.add("client_id", clientId);
        authRequest.add("username", request.email());
        authRequest.add("password", request.password());
        authRequest.add("grant_type", grantType);

        var httpClient = new RestTemplate();
        var response = httpClient.postForEntity(authUrl, authRequest, String.class);

        if (response.getStatusCode().is2xxSuccessful()) {
            return ResponseEntity.ok(response.getBody());
        }

        return ResponseEntity.status(response.getStatusCode()).body(response.getBody());
    }

}
