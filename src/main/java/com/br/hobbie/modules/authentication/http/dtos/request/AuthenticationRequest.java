package com.br.hobbie.modules.authentication.http.dtos.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.keycloak.representations.idm.CredentialRepresentation;


public record AuthenticationRequest(
        @NotBlank(message = "Email is required")
        @Email(message = "Email must be valid")
        String email,
        @NotBlank
        @Size(min = 6, message = "Password must be at least 6 characters long")
        String password
) {
    public CredentialRepresentation credentials() {
        var credential = new CredentialRepresentation();
        credential.setType(CredentialRepresentation.PASSWORD);
        credential.setValue(password);
        credential.setTemporary(false);
        return credential;
    }
}
