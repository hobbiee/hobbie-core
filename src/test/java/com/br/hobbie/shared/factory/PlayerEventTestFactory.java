package com.br.hobbie.shared.factory;

import com.br.hobbie.modules.event.domain.entities.Event;
import com.br.hobbie.modules.player.domain.entities.Player;
import com.br.hobbie.modules.player.domain.entities.Tag;
import com.br.hobbie.shared.utils.PlayerEventTestUtils;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.Map;
import java.util.Set;

public class PlayerEventTestFactory {

    static final ZonedDateTime DATE_TOMORROW = ZonedDateTime.now().plusDays(1);
    static final ZonedDateTime START_DATE = ZonedDateTime.now().plusDays(1);
    static final ZonedDateTime END_DATE = ZonedDateTime.now().plusDays(2);
    static final Float LATITUDE = 10F;
    static final Float LONGITUDE = 10F;

    static final Float VALID_PLAYER_LATITUDE = -20.298711F;
    static final Float VALID_PLAYER_LONGITUDE = -40.298711F;

    static final Float VALID_EVENT_LATITUDE = -20.298692F;
    static final Float VALID_EVENT_LONGITUDE = -40.291370F;

    static final BigDecimal RADIUS = BigDecimal.valueOf(400);

    static final int CAPACITY = 10;

    static final Set<Tag> TAGS = Set.of(new Tag("tag1"), new Tag("tag2"));

    static final Set<Tag> PLAYER_INTERESTS = Set.of(new Tag("SOCCER"), new Tag("BEACH SOCCER"));

    private Player player;
    private Event event;

    public static Player createPlayer() {
        var player = new Player("name", "avatar", 10F, 20F, RADIUS, LocalDate.of(1999, 1, 1));
        PlayerEventTestUtils.assignId(player, 1L);
        return player;
    }

    public static Player createParticipant() {
        var player = new Player("PLAYER PARTICIPANT", "avatar", VALID_PLAYER_LATITUDE, VALID_PLAYER_LONGITUDE, RADIUS, LocalDate.of(1999, 1, 1));
        PlayerEventTestUtils.assignId(player, 2L);
        return player;
    }

    public static Event createEvent() {
        Player player = createPlayer();
        return new Event("name", "description", CAPACITY, START_DATE, END_DATE, VALID_EVENT_LATITUDE, VALID_PLAYER_LONGITUDE, "thumbnail", TAGS, player);
    }

    public static Event createStartedEvent(Player admin) {
        var startDate = ZonedDateTime.now().minusMinutes(5);
        var endDate = ZonedDateTime.now().plusHours(1);
        return new Event("name", "description", CAPACITY, startDate, endDate, LATITUDE, LONGITUDE, "thumbnail", TAGS, admin);
    }

    public static Set<Event> createManyEvents() {
        return Set.of(
                new Event("FUTEBOL", "description", CAPACITY, START_DATE, END_DATE, VALID_EVENT_LATITUDE, VALID_EVENT_LONGITUDE, "thumbnail", Set.of(new Tag("SOCCER")), createPlayer()),


                new Event("BASQUETE", "description", CAPACITY, START_DATE, END_DATE, VALID_EVENT_LATITUDE, VALID_EVENT_LONGITUDE, "thumbnail", Set.of(new Tag("BASKETBALL"), new Tag("VOLLEYBALL")), createPlayer()),


                new Event("TENIS", "description", CAPACITY, START_DATE, END_DATE, VALID_EVENT_LATITUDE, VALID_EVENT_LONGITUDE, "thumbnail", Set.of(new Tag("BEACH SOCCER"), new Tag("TENNIS")), createPlayer()),

                new Event("HÓQUEI", "description", CAPACITY, START_DATE, END_DATE, 90F, -80F, "thumbnail", Set.of(new Tag("HOQUEI")), createParticipant()),

                new Event("BEACH SOCCER", "description", CAPACITY, START_DATE, END_DATE, -50.42422F, -30.424252F, "thumbnail", TAGS, createPlayer())
        );
    }

    public static Map<String, Object> createEventRequest() {
        return Map.of(
                "name", "name",
                "description", "description",
                "capacity", CAPACITY,
                "date", DATE_TOMORROW.toString(),
                "startDate", START_DATE.toString(),
                "endDate", END_DATE.toString(),
                "latitude", LATITUDE,
                "longitude", LONGITUDE,
                "thumbnail", "thumbnail",
                "categories", Set.of("tag1", "tag2").toArray()
        );
    }

    public static Event createOverlappingEvent() {
        var startDate = START_DATE.plusMinutes(30);
        var endDate = END_DATE.plusMinutes(30);
        return new Event("FUTEBOL", "description", CAPACITY, startDate, endDate, VALID_EVENT_LATITUDE, VALID_EVENT_LONGITUDE, "thumbnail", Set.of(new Tag("SOCCER")), createPlayer());
    }

    public static Event createNonOverlappingEvent() {
        var startDate = ZonedDateTime.now().plusHours(2);
        var endDate = ZonedDateTime.now().plusHours(4);
        return new Event("FUTEBOL", "description", CAPACITY, startDate, endDate, VALID_EVENT_LATITUDE, VALID_EVENT_LONGITUDE, "thumbnail", Set.of(new Tag("SOCCER")), createPlayer());
    }

    public static Player createPlayerToSave() {
        return new Player("name", "avatar", 10F, 20F, RADIUS, LocalDate.of(1999, 1, 1));
    }
}
