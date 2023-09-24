package com.br.hobbie.modules.player.domain.entities;

import com.br.hobbie.modules.event.domain.entities.Event;
import com.br.hobbie.shared.core.errors.Either;
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
    private Event event;

    @BeforeEach
    void setUp() {
        player = new Player("name", "avatar", BigDecimal.ONE, BigDecimal.TEN, LocalDate.of(1999, 1, 1));

        event = new Event("name", "description", 10, DATE, START_TIME, END_TIME, LATITUDE, LONGITUDE, "thumbnail", TAGS, player);
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

    @Test
    @DisplayName("Should create an event and set it as admin event")
    void createEvent_SetAdminEvent() {
        // GIVEN - setUp

        // WHEN
        player.createEvent(event);

        // THEN
        Assertions.assertEquals(1, player.getParticipantEvents().size());
        Assertions.assertTrue(player.getParticipantEvents().contains(event));
    }

    @Test
    @DisplayName("Get admin event should return a copy object of the admin event to keep the original event immutable")
    void getAdminEvent_ReturnCopy() {
        // GIVEN - setUp

        // WHEN
        Event adminEvent = player.getAdminEvent();

        // THEN
        Assertions.assertNotEquals(event, adminEvent);
        Assertions.assertEquals(event.getName(), adminEvent.getName());
        Assertions.assertEquals(event.getDescription(), adminEvent.getDescription());
    }

    @Test
    @DisplayName("Should close an event if it is active")
    void closeEvent_WhenActive() {
        // GIVEN - setUp

        // WHEN
        player.closeEvent();

        // THEN
        Assertions.assertFalse(event.isActive());
    }

    @Test
    @DisplayName("Should do nothing if event is not active")
    void closeEvent_WhenNotActive() {
        // GIVEN - setUp
        event.close();

        // WHEN
        player.closeEvent();

        // THEN
        Assertions.assertFalse(event.isActive());
    }

    @Test
    @DisplayName("Should quit an event when player is not admin")
    void quitEvent_WhenNotAdmin() {
        // GIVEN
        Player other = new Player("other name", "avatar", BigDecimal.ONE, BigDecimal.TEN, LocalDate.of(1999, 1, 1));
        player.createEvent(event);
        event.addParticipant(other);

        // WHEN
        Either<RuntimeException, Boolean> result = other.quitEvent(event);

        // THEN
        Assertions.assertTrue(result.isRight());
    }

    @Test
    @DisplayName("Should not quit an event when player is admin")
    void quitEvent_ReturnsEitherErrorWhenAdmin() {
        // GIVEN - setUp
        player.createEvent(event);

        // WHEN
        Either<RuntimeException, Boolean> result = player.quitEvent(event);

        // THEN
        Assertions.assertTrue(result.isLeft());
    }
}