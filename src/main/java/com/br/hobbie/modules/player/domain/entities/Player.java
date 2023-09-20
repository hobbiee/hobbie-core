package com.br.hobbie.modules.events.domain.entities;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;
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

    @Enumerated(EnumType.STRING)
    private Set<Category> interests;
}
