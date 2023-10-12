package com.br.hobbie.shared.factory;

import com.br.hobbie.modules.event.domain.entities.Event;
import com.br.hobbie.modules.player.domain.entities.Player;
import com.br.hobbie.modules.player.domain.entities.Tag;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Map;
import java.util.Set;

public class PlayerEventTestFactory {
    static final LocalDate DATE = LocalDate.of(1999, 1, 1);

    static final LocalDate DATE_TOMORROW = LocalDate.now().plusDays(1);
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

    public static Tag createTag() {
        return new Tag("DIFFERENT TAG");
    }

    public static Map<String, Object> createEventRequest() {
        return Map.of(
                "name", "name",
                "description", "description",
                "capacity", CAPACITY,
                "date", DATE_TOMORROW.toString(),
                "startTime", START_TIME.toString(),
                "endTime", END_TIME.toString(),
                "latitude", LATITUDE,
                "longitude", LONGITUDE,
                "thumbnail", "thumbnail",
                "categories", Set.of("tag1", "tag2").toArray()
        );
    }
}
