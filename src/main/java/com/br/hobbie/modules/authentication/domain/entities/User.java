package com.br.hobbie.modules.authentication.domain.entities;


import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private String email;
    private boolean emailVerified;
    private boolean enabled;

    /**
     * @see User#User (String, String)
     * @deprecated hibernate eyes only
     */
    @Deprecated(since = "hibernate eyes only", forRemoval = true)
    protected User() {
    }

    public User(String name, String email) {
        this.name = name;
        this.email = email;
        emailVerified = false;
        enabled = false;
    }
}
