package com.br.hobbie.modules.player.domain.entities;

import com.br.hobbie.modules.event.domain.entities.Event;
import com.br.hobbie.shared.core.errors.Either;
import jakarta.persistence.*;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Entity
public class Player {

    @ManyToMany(cascade = CascadeType.PERSIST)
    private final Set<Tag> interests = new LinkedHashSet<>();
    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Getter
    private String name;
    private String avatar;
    private BigDecimal matchLatitude;
    private BigDecimal matchLongitude;
    private LocalDate birthDate;

    @Getter
    @OneToOne(mappedBy = "admin")
    private Event adminEvent;

    @ManyToMany(mappedBy = "participants")
    private Set<Event> participantEvents = new LinkedHashSet<>();

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

    public Set<Tag> getInterests() {
        return Set.copyOf(interests);
    }

    public Either<RuntimeException, Void> createEvent(Event event) {
        if (adminEvent == null) {
            adminEvent = event;
            participantEvents.add(adminEvent);
            return Either.right(null);
        }

        return Either.left(new RuntimeException("Player already has an event"));
    }

    public Either<RuntimeException, Boolean> closeEvent() {
        // if adminEvent is null, then there is no event to close
        if (adminEvent == null) {
            return Either.left(new RuntimeException("Player has no event to close"));
        }

        adminEvent.close(this);
        participantEvents.remove(adminEvent);
        adminEvent = null;
        return Either.right(true);
    }

    public Either<RuntimeException, Boolean> quitEvent(Event event) {
        if (adminEvent != null && adminEvent.equals(event)) {
            return Either.left(new RuntimeException("Admin cannot quit event"));
        }

        if (!participantEvents.contains(event)) {
            return Either.left(new RuntimeException("Player is not a participant of this event"));
        }

        participantEvents.remove(event);
        return Either.right(true);
    }

    public List<Event> getParticipantEvents() {
        // returns a copy of the participant events to remain immutable
        return List.copyOf(participantEvents);
    }


    public Either<RuntimeException, Boolean> joinEvent(Event event) {
        if (adminEvent != null && adminEvent.equals(event))
            return Either.left(new RuntimeException("Player is already admin of this event"));

        participantEvents.add(event);
        return Either.right(true);
    }

    public boolean isSameOf(Player player) {
        return id.equals(player.id);
    }
}
