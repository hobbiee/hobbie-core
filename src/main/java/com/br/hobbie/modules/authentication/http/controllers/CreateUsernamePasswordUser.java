package com.br.hobbie.modules.authentication.http.controllers;

import com.br.hobbie.modules.authentication.domain.entities.AuthenticationActions;
import com.br.hobbie.modules.authentication.http.dtos.request.CreateUserRequest;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/v1/api/auth")
@RequiredArgsConstructor
public class CreateUsernamePasswordUser {

    @PersistenceContext
    private final EntityManager manager;
    private final Keycloak keycloak;
    @Value("${hobbie.authentication.keycloak.realm}")
    private String realm;

    private static UserRepresentation getUserRepresentation(CreateUserRequest request) {
        var userRepresentation = new UserRepresentation();
        userRepresentation.setUsername(request.email());
        userRepresentation.setEmail(request.email());
        userRepresentation.setFirstName(request.firstName());
        userRepresentation.setLastName(request.lastName());
        userRepresentation.setEmailVerified(false);
        // users remains disabled until email verification
        userRepresentation.setEnabled(false);
        userRepresentation.setRequiredActions(List.of(AuthenticationActions.VERIFY_EMAIL.action()));
        userRepresentation.setCredentials(request.credentials());
        return userRepresentation;
    }

    @PostMapping("/users")
    @Transactional
    public ResponseEntity<?> handle(@Valid @RequestBody CreateUserRequest request) {
        var userRepresentation = getUserRepresentation(request);
        var userEntity = request.toModel();
        var response = keycloak.realm(realm).users().create(userRepresentation);

        if (response.getStatusInfo().getFamily().equals(Response.Status.Family.SUCCESSFUL)) {
            manager.persist(userEntity);
            return ResponseEntity.created(response.getLocation()).build();
        }

        return ResponseEntity.status(response.getStatus()).body(response.getStatusInfo().getReasonPhrase());
    }

}
