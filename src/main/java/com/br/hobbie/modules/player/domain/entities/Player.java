package com.br.hobbie.modules.player.domain.entities;

import com.br.hobbie.modules.event.domain.entities.Event;
import com.br.hobbie.modules.event.domain.entities.ParticipationRequest;
import com.br.hobbie.shared.core.errors.Either;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NonNull;
import org.springframework.util.Assert;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Entity
public class Player {

    @ManyToMany(cascade = CascadeType.PERSIST)
    private final Set<Tag> interests = new LinkedHashSet<>();

    @OneToMany(mappedBy = "player", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private final Set<ParticipationRequest> joinRequests = new LinkedHashSet<>();

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
    @OneToMany(mappedBy = "admin")
    private Set<Event> adminEvents = new LinkedHashSet<>();

    @ManyToMany(mappedBy = "participants")
    private Set<Event> participantEvents = new LinkedHashSet<>();

    /**
     * @deprecated (since = " JPA ") Constructor for JPA uses only.
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

    public Set<Tag> getInterests() {
        return Set.copyOf(interests);
    }

    public void createEvent(Event event) {
        Assert.isTrue(event.isOwner(this), "Player must be the admin of the event");
        adminEvents.add(event);
        participantEvents.add(event);
    }

    public List<Event> getParticipantEvents() {
        // returns a copy of the participant events to remain immutable
        return List.copyOf(participantEvents);
    }


    public void joinEvent(Event event) {
        Assert.isTrue(event.isActive(), "Event must be active");
        Assert.isTrue(!event.capacityReached(), "Event must have capacity");
        event.addParticipant(this);
    }

    public boolean isSameOf(Player player) {
        return Objects.equals(id, player.id);
    }

    public Either<IllegalStateException, ParticipationRequest> sendInterest(@NonNull Event event) {
        if (!canSendInterest(event)) {
            return Either.left(new IllegalStateException("Player already has an event at this time"));
        }

        var participationRequest = new ParticipationRequest(this, event);
        joinRequests.add(participationRequest);
        return Either.right(participationRequest);
    }

    private boolean canSendInterest(Event event) {
        return participantEvents.stream().noneMatch(event::overlapsWith);
    }
}
