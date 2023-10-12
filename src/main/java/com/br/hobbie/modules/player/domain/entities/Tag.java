package com.br.hobbie.modules.player.domain.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;

import java.util.Objects;

/**
 * @author <a href="https://github.com/queirozlc">@queirozlc</a>
 * <h2>
 * Value Objects - Tag
 * </h2>
 *
 * <li>
 * This Tag class represents the concept of a value object on domain driven design
 * and it's used to represent the interests of a player when searching for a match in the player context.
 * </li>
 *
 * <li>
 * The reason for the @Entity annotation is that i choose to couple the value object with the Spring Data implementation, cause i do not see any reason to deal with this on separated way.
 * </li>
 *
 * <li>
 * This tag implementaiton could be changed on the future, but for now i think this is some way to provide a good solution for interests problem in players context, which gives to the player the possibility to choose the interests with more freedom.
 * </li>
 */
@Entity
public class Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Getter
    private Long id;
    @Getter
    private String name;


    /**
     * @deprecated (since = " JPA ") Constructor for JPA use only.
     */
    @Deprecated(since = "JPA")
    protected Tag() {
    }

    public Tag(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Tag tag)) return false;
        return Objects.equals(id, tag.id) && Objects.equals(name, tag.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}
