package com.br.hobbie.modules.event.domain.entities;

import com.br.hobbie.modules.player.domain.entities.Player;
import jakarta.persistence.*;
import lombok.Getter;
import org.springframework.util.Assert;

import java.time.Instant;

@Entity
public class JoinRequest {
    @Getter
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
    protected JoinRequest() {
    }

    public JoinRequest(Player player, Event event) {
        Assert.state(!event.isOwner(player), "Player cannot request participation in his own event");
        Assert.isTrue(event.notParticipant(player), "Player is already participating in this event");
        Assert.state(!event.capacityReached(), "Event is already full");
        Assert.state(!event.requestAlreadySent(player), "Player already sent a request to this event");
        this.player = player;
        this.event = event;
        requestTime = Instant.now();
        status = RequestStatus.PENDING;
        this.event.newJoinRequest(this);
    }

    public boolean isFrom(Player player) {
        return this.player.isSameOf(player);
    }

    public boolean isExpired() {
        return status == RequestStatus.EXPIRED;
    }

    public void accept() {
        Assert.state(status == RequestStatus.PENDING, "Request must be pending");
        status = RequestStatus.ACCEPTED;
        event.addParticipant(player);
    }

    public void reject() {
        Assert.state(status == RequestStatus.PENDING, "Request must be pending");
        status = RequestStatus.REJECTED;
    }

    public boolean isRejected() {
        return status == RequestStatus.REJECTED;
    }

    public boolean isAccepted() {
        return status == RequestStatus.ACCEPTED;
    }
}
