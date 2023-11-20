package com.br.hobbie.modules.authentication.http.controllers;

import com.br.hobbie.modules.authentication.http.dtos.request.CreateUserRequest;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/v1/api/auth")
@RequiredArgsConstructor
@Slf4j
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
        userRepresentation.setEnabled(true);
        userRepresentation.setCredentials(request.credentials());
        return userRepresentation;
    }

    @PostMapping("/users")
    @Transactional
    public ResponseEntity<?> handle(@Valid @RequestBody CreateUserRequest request) {
        var userRepresentation = getUserRepresentation(request);
        var userEntity = request.toModel();
        UsersResource usersResource = keycloak.realm(realm).users();
        var response = usersResource.create(userRepresentation);

        if (response.getStatusInfo().getFamily().equals(Response.Status.Family.SUCCESSFUL)) {
            manager.persist(userEntity);
            List<UserRepresentation> userRepresentationList = usersResource.searchByEmail(request.email(), true);

            if (!CollectionUtils.isEmpty(userRepresentationList)) {
                log.info("Sending email verification to user {}", request.email());
                userRepresentationList
                        .stream()
                        .filter(user -> Objects.equals(false, user.isEmailVerified()))
                        .findFirst()
                        .ifPresent(user -> {
                            log.info("User id {}", user.getId());
                            usersResource.get(user.getId())
                                    .sendVerifyEmail();
                        });

            }


            return ResponseEntity.created(response.getLocation()).build();
        }

        return ResponseEntity.status(response.getStatus()).body(response.getStatusInfo().getReasonPhrase());
    }

}
