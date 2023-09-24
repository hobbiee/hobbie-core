package com.br.hobbie.shared.factory;

import com.br.hobbie.modules.event.domain.entities.Event;
import com.br.hobbie.modules.player.domain.entities.Player;
import com.br.hobbie.modules.player.domain.entities.Tag;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Set;

public class PlayerEventTestFactory {
    static final LocalDate DATE = LocalDate.of(1999, 1, 1);
    static final LocalTime START_TIME = LocalTime.of(10, 0);
    static final LocalTime END_TIME = LocalTime.of(12, 0);
    static final BigDecimal LATITUDE = BigDecimal.ONE;
    static final BigDecimal LONGITUDE = BigDecimal.TEN;

    static final int CAPACITY = 10;

    static final Set<Tag> TAGS = Set.of(new Tag("tag1"), new Tag("tag2"));

    private Player player;
    private Event event;

    public static Player createPlayer() {
        return new Player("name", "avatar", BigDecimal.ONE, BigDecimal.TEN, LocalDate.of(1999, 1, 1));
    }

    public static Player createParticipant() {
        return new Player("PLAYER PARTICIPANT", "avatar", BigDecimal.TEN, BigDecimal.TEN, LocalDate.of(1999, 1, 1));
    }

    public static Event createEvent() {
        Player player = createPlayer();
        return new Event("name", "description", CAPACITY, DATE, START_TIME, END_TIME, LATITUDE, LONGITUDE, "thumbnail", TAGS, player);
    }
}
