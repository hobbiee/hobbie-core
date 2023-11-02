package com.br.hobbie.modules.player.domain.entities;

import com.br.hobbie.modules.event.domain.entities.Event;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.Set;

@ExtendWith(MockitoExtension.class)
class PlayerTest {
    static final ZonedDateTime START_DATE = ZonedDateTime.now();
    static final ZonedDateTime END_DATE = ZonedDateTime.now().plusDays(1);
    static final Float LATITUDE = 10F;
    static final Float LONGITUDE = 10F;

    static final BigDecimal RADIUS = BigDecimal.valueOf(100);

    static final int CAPACITY = 10;

    static final Set<Tag> TAGS = Set.of(new Tag("tag1"), new Tag("tag2"));

    private Player player;
    private Event event;

    @BeforeEach
    void setUp() {
        player = new Player("name", "avatar", 10F, 10F, RADIUS, LocalDate.of(1999, 1, 1));

        event = new Event("name", "description", CAPACITY, START_DATE, END_DATE, LATITUDE, LONGITUDE, "thumbnail", TAGS, player);
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
    @DisplayName("Should create an event and set it as admin event")
    void createEvent_SetAdminEvent() {
        // GIVEN - setUp

        // WHEN
        player.createEvent(event);

        // THEN
        Assertions.assertEquals(1, player.getParticipantEvents().size());
        Assertions.assertTrue(player.getParticipantEvents().contains(event));
    }

}