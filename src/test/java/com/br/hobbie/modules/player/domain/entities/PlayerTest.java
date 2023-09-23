package com.br.hobbie.modules.player.domain.entities;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Set;

@ExtendWith(MockitoExtension.class)
class PlayerTest {
    static final LocalDate DATE = LocalDate.of(1999, 1, 1);
    static final LocalTime START_TIME = LocalTime.of(10, 0);
    static final LocalTime END_TIME = LocalTime.of(12, 0);
    static final BigDecimal LATITUDE = BigDecimal.ONE;
    static final BigDecimal LONGITUDE = BigDecimal.TEN;

    static final Set<Tag> TAGS = Set.of(new Tag("tag1"), new Tag("tag2"));

    private Player player;

    @BeforeEach
    void setUp() {
        player = new Player("name", "avatar", BigDecimal.ONE, BigDecimal.TEN, LocalDate.of(1999, 1, 1));
    }

    @Test
    @DisplayName("Should add interest to player")
    void addInterest_WhenSuccessFull() {
        // GIVEN
        Tag tag = new Tag("tag");

        // WHEN
        player.addInterest(tag);

        // THEN
        Assertions.assertEquals(1, player.getInterests().size());
        Assertions.assertEquals(tag, player.getInterests().iterator().next());
    }

    @Test
    @DisplayName("Should not add interest to player when already exists")
    void addInterest_WhenAlreadyExists() {
        // GIVEN
        Tag tag = new Tag("tag");
        player.addInterest(tag);

        // WHEN
        player.addInterest(tag);

        // THEN
        Assertions.assertEquals(1, player.getInterests().size());
        Assertions.assertEquals(tag, player.getInterests().iterator().next());
    }

    @Test
    @DisplayName("Should be able to add multiple interests to player")
    void addInterests_WhenSuccessFull() {
        // GIVEN
        Tag tag1 = new Tag("tag1");
        Tag tag2 = new Tag("tag2");

        // WHEN
        player.addInterests(tag1, tag2);

        // THEN
        Assertions.assertEquals(2, player.getInterests().size());
        Assertions.assertTrue(player.getInterests().contains(tag1));
        Assertions.assertTrue(player.getInterests().contains(tag2));
    }

    @Test
    @DisplayName("Should not add multiple interests to player when already exists")
    void addInterests_WhenAlreadyExists() {
        // GIVEN
        Tag tag1 = new Tag("tag1");
        Tag tag2 = new Tag("tag2");
        player.addInterests(tag1, tag2);

        // WHEN
        player.addInterests(tag1, tag2);

        // THEN
        Assertions.assertEquals(2, player.getInterests().size());
        Assertions.assertTrue(player.getInterests().contains(tag1));
        Assertions.assertTrue(player.getInterests().contains(tag2));
    }


    // the test passes, but the player should not be able to change other players references
    // this is a proof of concept of if player can change other players references
    // so maybe we should change the way we are doing the relationship between player and event
    // maybe we should create a new entity called PlayerEvent and use it to make the relationship
    // between player and event

    /**
     * The class PlayerEvent should have the following attributes:
     * <p>
     * private Set<Player> players;
     * private Set<Event> events;
     * </p>
     * <p>
     * the first attribute is a set of players, which means the players that are going to the event
     * the second attribute is a set of events, which means the events that the player is going to participate
     */
    @Test
    @DisplayName("Proof of concept of if player can change other players references")
    void pocOfIfPlayerCanChangeOtherPlayersReferences() {
    }
}