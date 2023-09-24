package com.br.hobbie.modules.event.domain.entities;

import com.br.hobbie.modules.player.domain.entities.Player;
import com.br.hobbie.modules.player.domain.entities.Tag;
import com.br.hobbie.shared.core.errors.Either;
import jakarta.persistence.*;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashSet;
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
    private boolean active = true;


    @ManyToMany
    private Set<Tag> categories = new HashSet<>();

    @OneToOne
    private Player admin;

    @OneToMany
    private Set<Player> participants = new HashSet<>();


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


    public Either<RuntimeException, Void> addParticipant(Player player) {
        if (participants.size() < capacity) {
            participants.add(player);
            return Either.right(null);
        }

        return Either.left(new RuntimeException("Event is full"));
    }

    public void close() {
        if (active) {
            active = false;
            participants.forEach(player -> player.removeEvent(this));
        }
    }

    @Override
    public String toString() {
        return "Event{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", capacity=" + capacity +
                ", date=" + date +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", thumbnail='" + thumbnail + '\'' +
                ", active=" + active +
                ", categories=" + categories +
                ", admin=" + admin +
                ", participants=" + participants +
                '}';
    }
}
