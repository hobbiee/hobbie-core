package com.br.hobbie.modules.event.domain.entities;

import com.br.hobbie.modules.player.domain.entities.Player;
import com.br.hobbie.modules.player.domain.entities.Tag;
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
    @OneToMany(mappedBy = "event")
    private final Set<ParticipationRequest> requests = new LinkedHashSet<>();
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

    @ManyToMany
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


    public Either<RuntimeException, Boolean> addParticipant(Player player) {
        if (!admin.equals(player) && participants.contains(player)) {
            return Either.left(new RuntimeException("Player already joined"));
        }

        if (participants.size() < capacity) {
            participants.add(player);
            var either = player.joinEvent(this);
            if (either.isRight()) {
                return Either.right(true);
            }

            return Either.left(either.getLeft());
        }

        return Either.left(new RuntimeException("Event is full"));
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

    public boolean isOwner(Player player) {
        return admin.isSameOf(player);
    }

    public boolean alreadyParticipating(Player player) {
        return participants.contains(player);
    }

    public boolean isFull() {
        return participants.size() == capacity;
    }

    public boolean requestAlreadySent(Player player) {
        return requests.stream().anyMatch(request -> request.isFrom(player));
    }
}
