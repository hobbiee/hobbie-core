package com.br.hobbie.modules.event.domain.entities;

import com.br.hobbie.modules.player.domain.entities.Player;
import jakarta.persistence.*;
import org.springframework.util.Assert;

import java.time.Instant;

@Entity
public class ParticipationRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @ManyToOne
    private Player player;

    @ManyToOne
    private Event event;

    private Instant requestTime;

    @Enumerated(EnumType.STRING)
    private RequestStatus status;

    /**
     * @deprecated JPA eyes only
     */
    @Deprecated(since = "JPA only")
    protected ParticipationRequest() {
    }

    public ParticipationRequest(Player player, Event event) {
        Assert.state(!event.isOwner(player), "Player cannot request participation in his own event");
        Assert.isTrue(event.notParticipant(player), "Player is already participating in this event");
        Assert.state(!event.capacityReached(), "Event is already full");
        Assert.state(!event.requestAlreadySent(player), "Player already sent a request to this event");
        this.player = player;
        this.event = event;
        requestTime = Instant.now();
        status = RequestStatus.PENDING;
    }

    /**
     * <p>
     * This constructor will be used to build a participation request from the database.
     * </p>
     *
     * <p>
     * It needs to be here because when recovering data from the database, the attributes should not set by default as the above constructor does. They should be set by the database.
     * </p>
     *
     * <p>
     * Not sure if this is really necessary, but I'll leave it here for now.
     * </p>
     *
     * @param player      - Player, who is requesting participation
     * @param event       - Event, which the player is interested in
     * @param status      - RequestStatus, which can be PENDING, ACCEPTED or REJECTED
     * @param requestTime - Instant, which is the time the request was sent
     */
    public ParticipationRequest(Player player, Event event, RequestStatus status, Instant requestTime) {
        Assert.state(!event.isOwner(player), "Player cannot request participation in his own event");
        Assert.isTrue(event.notParticipant(player), "Player is already participating in this event");
        Assert.state(!event.capacityReached(), "Event is already full");
        Assert.state(event.requestAlreadySent(player), "Player already sent a request to this event");
        this.player = player;
        this.event = event;
        this.status = status;
        this.requestTime = requestTime;
    }

    public boolean isFrom(Player player) {
        return this.player.isSameOf(player);
    }
}
