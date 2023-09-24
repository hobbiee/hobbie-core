package com.br.hobbie.modules.player.domain.entities;

import com.br.hobbie.modules.event.domain.entities.Event;
import com.br.hobbie.shared.core.errors.Either;
import com.br.hobbie.shared.core.utils.CloneUtils;
import jakarta.persistence.*;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Entity
public class Player implements Comparable<Player> {

    @ManyToMany(cascade = CascadeType.PERSIST)
    private final Set<Tag> interests = new LinkedHashSet<>();
    @Transient
    private int internalId;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Getter
    private String name;
    private String avatar;
    private BigDecimal matchLatitude;
    private BigDecimal matchLongitude;
    private LocalDate birthDate;

    @OneToOne(mappedBy = "admin")
    private Event adminEvent;

    @OneToMany
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
        internalId = hashCode();
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

    public void closeEvent() {
        adminEvent.close();
    }

    public Either<RuntimeException, Boolean> quitEvent(Event event) {
        if (event.getAdmin().compareTo(this) == 0) {
            return Either.left(new RuntimeException("Admin cannot quit event"));
        }

        if (!participantEvents.contains(event)) {
            return Either.left(new RuntimeException("Player is not a participant of this event"));
        }

        participantEvents.remove(event);
        return Either.right(true);
    }

    public Event getAdminEvent() {
        // returns a copy of the event to avoid changes to the original event
        return CloneUtils.clone(Event.class, adminEvent);
    }

    public List<Event> getParticipantEvents() {
        // returns a copy of the participant events to remain immutable
        return List.copyOf(participantEvents);
    }


    /**
     * Compares this object with the specified object for order.  Returns a
     * negative integer, zero, or a positive integer as this object is less
     * than, equal to, or greater than the specified object.
     *
     * <p>The implementor must ensure {@link Integer#signum
     * signum}{@code (x.compareTo(y)) == -signum(y.compareTo(x))} for
     * all {@code x} and {@code y}.  (This implies that {@code
     * x.compareTo(y)} must throw an exception if and only if {@code
     * y.compareTo(x)} throws an exception.)
     *
     * <p>The implementor must also ensure that the relation is transitive:
     * {@code (x.compareTo(y) > 0 && y.compareTo(z) > 0)} implies
     * {@code x.compareTo(z) > 0}.
     *
     * <p>Finally, the implementor must ensure that {@code
     * x.compareTo(y)==0} implies that {@code signum(x.compareTo(z))
     * == signum(y.compareTo(z))}, for all {@code z}.
     *
     * @param o the object to be compared.
     * @return a negative integer, zero, or a positive integer as this object
     * is less than, equal to, or greater than the specified object.
     * @throws NullPointerException if the specified object is null
     * @throws ClassCastException   if the specified object's type prevents it
     *                              from being compared to this object.
     * @apiNote It is strongly recommended, but <i>not</i> strictly required that
     * {@code (x.compareTo(y)==0) == (x.equals(y))}.  Generally speaking, any
     * class that implements the {@code Comparable} interface and violates
     * this condition should clearly indicate this fact.  The recommended
     * language is "Note: this class has a natural ordering that is
     * inconsistent with equals."
     */
    @Override
    public int compareTo(Player o) {
        return internalId - o.internalId;
    }


}
