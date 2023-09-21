package com.br.hobbie.modules.player.domain.entities;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
public class Player {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;

    private String avatar;

    private BigDecimal matchLatitude;
    private BigDecimal matchLongitude;
    private LocalDate birthDate;

    @ManyToMany(cascade = CascadeType.PERSIST)
    private Set<Tag> interests = new LinkedHashSet<>();

    /**
     * @deprecated (since = " JPA ") Constructor for JPA use only.
     */
    @Deprecated(since = "JPA")
    protected Player() {
    }

    public Player(String name, String avatar, BigDecimal matchLatitude, BigDecimal matchLongitude, LocalDate birthDate) {
        this.name = name;
        this.avatar = avatar;
        this.matchLatitude = matchLatitude;
        this.matchLongitude = matchLongitude;
        this.birthDate = birthDate;
    }

    public void addInterest(Tag tag) {
        interests.add(tag);
    }

    public void addInterests(Tag... tags) {
        Arrays.stream(tags)
                .filter(tag -> !interests.contains(tag))
                .forEach(interests::add);
    }
}
