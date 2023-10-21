package com.br.hobbie.modules.event.domain.entities;

import com.br.hobbie.modules.player.domain.entities.Player;
import com.br.hobbie.modules.player.domain.entities.Tag;
import com.br.hobbie.shared.core.errors.Either;
import jakarta.persistence.*;
import lombok.Getter;
import org.springframework.util.Assert;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
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
    private ZonedDateTime startDate;
    private ZonedDateTime endDate;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private String thumbnail;
    @Getter
    private boolean active = true;
    @ManyToMany(cascade = CascadeType.PERSIST)
    private Set<Tag> categories = new LinkedHashSet<>();

    @Getter
    @ManyToOne
    private Player admin;

    @ManyToMany
    private Set<Player> participants = new LinkedHashSet<>();


    @Deprecated(since = "JPA only")
    protected Event() {
    }

    public Event(String name, String description, int capacity, ZonedDateTime startDate, ZonedDateTime endDate, BigDecimal latitude, BigDecimal longitude, String thumbnail, Set<Tag> categories, Player admin) {
        this.name = name;
        this.description = description;
        this.capacity = capacity;
        this.startDate = startDate;
        this.endDate = endDate;
        this.latitude = latitude;
        this.longitude = longitude;
        this.thumbnail = thumbnail;
        this.categories = categories;
        this.admin = admin;
        this.admin.createEvent(this);
        participants.add(admin);
    }


    public Either<RuntimeException, Boolean> addParticipant(Player player) {
        Assert.state(!admin.equals(player), "You cannot add yourself as a participant");

        if (capacityReached()) {
            return Either.left(new RuntimeException("Event is full"));
        }

        participants.add(player);
        return Either.right(true);
    }

    public boolean capacityReached() {
        return participants.size() == capacity;
    }

    public List<Player> getParticipants() {
        return List.copyOf(participants);
    }

    public boolean isOwner(Player player) {
        return admin.isSameOf(player);
    }

    public boolean notParticipant(Player player) {
        return !participants.contains(player);
    }

    public boolean requestAlreadySent(Player player) {
        return requests.stream().anyMatch(request -> request.isFrom(player));
    }
}
