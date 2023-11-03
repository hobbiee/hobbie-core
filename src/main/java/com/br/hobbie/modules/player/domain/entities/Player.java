package com.br.hobbie.modules.player.domain.entities;

import com.br.hobbie.modules.event.domain.entities.Event;
import com.br.hobbie.modules.event.domain.entities.JoinRequest;
import com.br.hobbie.shared.core.errors.Either;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NonNull;
import org.springframework.util.Assert;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

@Entity
public class Player {

    @OneToMany(mappedBy = "player", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private final Set<JoinRequest> joinRequests = new LinkedHashSet<>();
    @ManyToMany(cascade = CascadeType.PERSIST)
    private final Set<Tag> interests = new HashSet<>();
    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Getter
    private String name;
    @Getter
    private String avatar;

    /**
     * <p>
     * The first 3 attributes below are used to control the matching of players with events based on their location.
     * </p>
     */
    @Getter
    private Float matchLatitude;
    @Getter
    private Float matchLongitude;

    @Getter
    private BigDecimal radius;

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

    public Player(String name, String avatar, Float matchLatitude, Float matchLongitude, BigDecimal radius, LocalDate birthDate) {
        this.name = name;
        this.avatar = avatar;
        this.matchLatitude = matchLatitude;
        this.matchLongitude = matchLongitude;
        this.birthDate = birthDate;
        this.radius = radius;
    }

    public void addInterest(Tag tag) {
        interests.add(tag);
    }


    public void createEvent(Event event) {
        Assert.isTrue(event.isOwner(this), "Player must be the admin of the event");
        adminEvents.add(event);
        participantEvents.add(event);
    }

    public boolean isSameOf(Player player) {
        Objects.requireNonNull(player.id, "Player id cannot be null");
        Objects.requireNonNull(id, "Player id cannot be null");
        return Objects.equals(id, player.id);
    }

    public Either<IllegalStateException, JoinRequest> sendInterest(@NonNull Event event) {
        Assert.isTrue(!event.isOwner(this), "Player cannot request participation in his own event");

        if (!canSendInterest(event)) {
            return Either.left(new IllegalStateException("Player already has an event at this time"));
        }

        var participationRequest = new JoinRequest(this, event);
        joinRequests.add(participationRequest);
        return Either.right(participationRequest);
    }

    private boolean canSendInterest(Event event) {
        return participantEvents.stream().noneMatch(event::overlapsWith);
    }

    public Either<RuntimeException, Boolean> acceptJoinRequest(Player joiningParticipant, Event event) {
        if (!event.isOwner(this)) return Either.left(new RuntimeException("Player must be the admin of the event"));

        if (event.hasJoinRequestFrom(joiningParticipant)) {
            event.acceptJoinRequest(joiningParticipant);
            return Either.right(true);
        }

        return Either.left(new RuntimeException("Player must have a join request"));
    }

    public void rejectJoinRequest(Player joiningPlayer, Event event) {
        Assert.isTrue(event.isOwner(this), "Player must be the admin of the event");
        Assert.isTrue(adminEvents.contains(event), "Player must be the admin of the event");

        if (event.hasJoinRequestFrom(joiningPlayer)) {
            event.rejectJoinRequest(joiningPlayer);
        }
    }

    /**
     * <h2>
     * Checks if the distance is within the radius of the player
     * </h2>
     *
     * <p>
     * Both distance parameter and player's radius are and must be in meters
     * </p>
     *
     * @param distance in meters
     * @return true if the distance is within the radius of the player
     */
    public boolean distanceIsWithinRadius(double distance) {
        return radius.compareTo(BigDecimal.valueOf(distance)) >= 0;
    }

    public boolean notInterestedIn(Tag tag) {
        return interests.stream().noneMatch(interest -> interest.isSameOf(tag));
    }

    public boolean hasInterestIn(Tag tag) {
        return interests.stream().anyMatch(interest -> interest.isSameOf(tag));
    }

    public boolean isParticipant(Event event) {
        return participantEvents.contains(event);
    }
}
