package com.br.hobbie.modules.authentication.domain.entities;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "users")
public class User {

    @Id
    private Long id;
    private String name;
    private String email;
    private boolean emailVerified;


    /**
     * @see User#User (String, String, boolean)
     * @deprecated hibernate eyes only
     */
    @Deprecated(since = "hibernate eyes only", forRemoval = true)
    protected User() {
    }

    public User(String name, String email, boolean emailVerified) {
        this.name = name;
        this.email = email;
        this.emailVerified = emailVerified;
    }
}
