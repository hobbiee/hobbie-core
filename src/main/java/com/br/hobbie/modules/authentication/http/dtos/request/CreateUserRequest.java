package com.br.hobbie.modules.authentication.http.dtos.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import org.keycloak.representations.idm.CredentialRepresentation;

import java.util.List;

public record CreateUserRequest(
        @NotBlank(message = "first name is required")
        String firstName,
        String lastName,
        @NotBlank(message = "email is required")
        @Email(message = "email must be valid")
        String email,
        @NotBlank(message = "password is required")
        @Min(value = 6, message = "password must be at least 6 characters long")
        String password

) {
    public List<CredentialRepresentation> credentials() {
        var credential = new CredentialRepresentation();
        credential.setType(CredentialRepresentation.PASSWORD);
        credential.setValue(password);
        credential.setTemporary(false);
        return List.of(credential);
    }
}
