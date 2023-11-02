package com.br.hobbie.modules.event.domain.entities;

import com.br.hobbie.modules.player.domain.entities.Player;
import com.br.hobbie.modules.player.domain.entities.Tag;
import com.br.hobbie.shared.core.errors.Either;
import jakarta.persistence.*;
import lombok.Getter;
import org.springframework.util.Assert;

import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Entity
public class Event {
    @OneToMany(mappedBy = "event")
    private final Set<JoinRequest> requests = new LinkedHashSet<>();
    @ManyToMany(cascade = CascadeType.PERSIST)
    private final Set<Tag> categories;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    @Getter
    private String name;
    @Getter
    private String description;
    @Getter
    private int capacity;
    private ZonedDateTime startDate;
    private ZonedDateTime endDate;
    @Getter
    private Float latitude;
    @Getter
    private Float longitude;
    @Getter
    private String thumbnail;
    @Getter
    private boolean active = true;
    @Getter
    @ManyToOne
    private Player admin;

    @ManyToMany
    private Set<Player> participants = new LinkedHashSet<>();


    @Deprecated(since = "JPA only")
    protected Event() {
        categories = Collections.emptySet();
    }

    public Event(String name, String description, int capacity, ZonedDateTime startDate, ZonedDateTime endDate, Float latitude, Float longitude, String thumbnail, Set<Tag> categories, Player admin) {
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

    public boolean overlapsWith(Event event) {
        // player cannot send interest to an event that starts at least 1 hour before or after the event
        return startDate.isBefore(event.endDate.minusHours(1)) && endDate.isAfter(event.startDate.plusHours(1));
    }


    public boolean hasJoinRequestFrom(Player player) {
        return requests.stream().anyMatch(request -> request.isFrom(player));
    }

    public void acceptJoinRequest(Player joiningParticipant) {
        Assert.state(hasJoinRequestFrom(joiningParticipant), "Player must have a join request");
        requests.stream()
                .filter(joinRequest -> joinRequest.isFrom(joiningParticipant))
                .findFirst()
                .ifPresent(JoinRequest::accept);
    }

    public void rejectJoinRequest(Player joiningPlayer) {
        Assert.state(hasJoinRequestFrom(joiningPlayer), "Player must have a join request");
        requests.stream()
                .filter(joinRequest -> joinRequest.isFrom(joiningPlayer))
                .findFirst()
                .ifPresent(JoinRequest::reject);
    }

    public String getAdminName() {
        return admin.getName();
    }

    public String getAdminAvatar() {
        return admin.getAvatar();
    }

    public String[] getCategoriesNames() {
        return categories.stream().map(Tag::getName).toArray(String[]::new);
    }

    public int getAmountOfParticipants() {
        return participants.size();
    }

    public String getFormattedDate() {
        return startDate.getDayOfMonth() + "/" + startDate.getMonthValue() + "/" + startDate.getYear();
    }

    public String getFormattedStartTime() {
        return startDate.getHour() + ":" + startDate.getMinute();
    }

    public String getFormattedEndTime() {
        return endDate.getHour() + ":" + endDate.getMinute();
    }

    public Set<Tag> distinctTagsFrom(Player player) {
        return categories.stream()
                .filter(player::notInterestedIn)
                .collect(Collectors.toSet());
    }

    public Collection<Tag> getCategories() {
        return Collections.unmodifiableCollection(categories);
    }

    public void beeingRequested(JoinRequest joinRequest) {
        requests.add(joinRequest);
    }
}
