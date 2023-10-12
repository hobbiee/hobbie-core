package com.br.hobbie.modules.event.domain.entities;

import com.br.hobbie.modules.player.domain.entities.Player;
import com.br.hobbie.modules.player.domain.entities.Tag;
import com.br.hobbie.shared.core.errors.DomainException;
import com.br.hobbie.shared.core.errors.Either;
import jakarta.persistence.*;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Entity
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    @Getter
    private String name;
    @Getter
    private String description;
    private int capacity;
    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private String thumbnail;
    @Getter
    private boolean active = true;


    @ManyToMany(cascade = CascadeType.PERSIST)
    private Set<Tag> categories = new LinkedHashSet<>();

    @Getter
    @OneToOne
    private Player admin;

    @OneToMany
    private Set<Player> participants = new LinkedHashSet<>();


    @Deprecated(since = "JPA only")
    protected Event() {
    }

    public Event(String name, String description, int capacity, LocalDate date, LocalTime startTime, LocalTime endTime, BigDecimal latitude, BigDecimal longitude, String thumbnail, Set<Tag> categories, Player admin) {
        this.name = name;
        this.description = description;
        this.capacity = capacity;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.latitude = latitude;
        this.longitude = longitude;
        this.thumbnail = thumbnail;
        this.categories = categories;
        this.admin = admin;
        this.admin.createEvent(this);
        participants.add(admin);
    }


    public Either<DomainException, Boolean> addParticipant(Player player) {
        if (participants.size() < capacity) {
            participants.add(player);
            var either = player.joinEvent(this);
            if (either.isRight()) {
                return Either.right(true);
            }

            return Either.left(either.getLeft());
        }

        return Either.left(new DomainException("Event is full"));
    }

    public boolean capacityReached() {
        return participants.size() == capacity;
    }

    public List<Player> getParticipants() {
        return List.copyOf(participants);
    }

    public void close(Player player) {
        if (player.equals(admin)) {
            active = false;
            participants.clear();
            admin = null;
        }
    }
}
