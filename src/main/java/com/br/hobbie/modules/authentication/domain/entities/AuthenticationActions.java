package com.br.hobbie.modules.authentication.domain.entities;

public enum AuthenticationActions {

    VERIFY_EMAIL("VERIFY_EMAIL");

    private final String action;

    AuthenticationActions(String action) {
        this.action = action;
    }

    public String action() {
        return action;
    }
}
