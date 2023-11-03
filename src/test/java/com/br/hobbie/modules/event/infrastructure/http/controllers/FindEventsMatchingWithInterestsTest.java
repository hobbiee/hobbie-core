package com.br.hobbie.modules.event.infrastructure.http.controllers;

import com.br.hobbie.modules.event.domain.entities.Event;
import com.br.hobbie.modules.event.domain.repositories.EventRepository;
import com.br.hobbie.modules.event.infrastructure.http.dtos.response.EventResponse;
import com.br.hobbie.modules.player.domain.entities.Player;
import com.br.hobbie.modules.player.domain.entities.Tag;
import com.br.hobbie.modules.player.domain.repositories.PlayerRepository;
import com.br.hobbie.modules.player.domain.repositories.TagRepository;
import com.br.hobbie.shared.utils.CustomMockMvc;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import static org.hamcrest.Matchers.hasSize;

@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
@SpringBootTest(properties = "spring.profiles.active=test",
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
class FindEventsMatchingWithInterestsTest {

    static final Float VALID_PLAYER_LATITUDE = -20.298711F;
    static final Float VALID_PLAYER_LONGITUDE = -40.292130F;
    static final Float VALID_EVENT_LATITUDE = -20.295845F;
    static final Float VALID_EVENT_LONGITUDE = -40.29005F;
    private static String URL = "/v1/api/events/";
    private static Iterable<Tag> tags;
    private Player participant;
    private Player admin;
    @Autowired
    private EventRepository eventRepository;
    @Autowired
    private PlayerRepository playerRepository;
    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private EntityManager manager;
    @Autowired
    private CustomMockMvc mvc;

    @BeforeEach
    void setUp() {
        tags = tagRepository.saveAll(Arrays.asList(new Tag("SOCCER"), new Tag("BEACH SOCCER")));
        participant = new Player("John Doe", "https://www.google.com", VALID_PLAYER_LATITUDE, VALID_PLAYER_LONGITUDE, BigDecimal.valueOf(400), LocalDate.now());
        admin = playerRepository.save(new Player("Admin Player", "https://www.google.com", VALID_EVENT_LATITUDE, VALID_PLAYER_LONGITUDE, BigDecimal.valueOf(200), LocalDate.now()));
    }

    @AfterEach
    void tearDown() {
        manager.clear();
        URL = "/v1/api/events/";
    }

    @Test
    @DisplayName("Should return a list of events matching with player interests when event is within player radius")
    void shouldReturnListOfEvents_WhenSuccessfull() throws Exception {
        // GIVEN
        tags.forEach(tag -> participant.addInterest(tag));
        participant = playerRepository.save(participant);
        var event = new Event("Event name", "Event description", 10, ZonedDateTime.now().plusDays(1), ZonedDateTime.now().plusDays(2), VALID_EVENT_LATITUDE, VALID_EVENT_LONGITUDE, "thumbnail", new HashSet<>((Collection<Tag>) tags), admin);
        manager.persist(event);
        URL += participant.getId();
        // WHEN
        var response = mvc.get(URL);

        // THEN
        var eventResponseFieldNames = Arrays.stream(EventResponse.class.getDeclaredFields())
                .map(Field::getName)
                .toList();

        response
                .andExpect(mvc.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isArray());
        // expect to have all fields from EventResponse
        eventResponseFieldNames.forEach(field -> {
            try {
                response.andExpect(MockMvcResultMatchers.jsonPath("$[*]." + field).exists());
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    @Test
    @DisplayName("Should return an empty list when there is no events matching with player interests")
    void shouldReturnEmptyList_WhenThereIsNoEventsMatchingWithPlayerInterests() throws Exception {
        // GIVEN
        tags.forEach(tag -> participant.addInterest(tag));
        participant = playerRepository.save(participant);
        var event = new Event("Event name", "Event description", 10, ZonedDateTime.now().plusDays(1), ZonedDateTime.now().plusDays(2), VALID_EVENT_LATITUDE, VALID_EVENT_LONGITUDE, "thumbnail", Set.of(new Tag("NO MATCH TAGS")), admin);
        manager.persist(event);
        URL += participant.getId();
        // WHEN
        var response = mvc.get(URL);

        // THEN
        response
                .andExpect(mvc.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isEmpty());
    }

    @Test
    @DisplayName("Should return an empty list when there is no events within player radius")
    void shouldReturnEmptyList_WhenThereIsNoEventsWithinPlayerRadius() throws Exception {
        // GIVEN
        tags.forEach(tag -> participant.addInterest(tag));
        participant = playerRepository.save(participant);
        var event = new Event("Event name", "Event description", 10, ZonedDateTime.now().plusDays(1), ZonedDateTime.now().plusDays(2), -40.295845F, -80.424213F, "thumbnail", new HashSet<>((Collection<Tag>) tags), admin);
        manager.persist(event);
        URL += participant.getId();
        // WHEN
        var response = mvc.get(URL);

        // THEN
        response
                .andExpect(mvc.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isEmpty());
    }

    @Test
    @DisplayName("Should return an matching event and other possible recommendations according to player distinct interests")
    void shouldReturnMatchingEventAndOtherPossibleRecommendations_WhenSuccessfull() throws Exception {
        // GIVEN
        tags.forEach(tag -> participant.addInterest(tag));
        var differentTag = new Tag("OTHER TAG");
        manager.persist(differentTag);

        var distinctEventTags = new HashSet<>((Collection<Tag>) tags);
        distinctEventTags.add(differentTag);

        participant = playerRepository.save(participant);

        var event = new Event("Event name", "Event description", 10, ZonedDateTime.now().plusDays(1), ZonedDateTime.now().plusDays(2), VALID_EVENT_LATITUDE, VALID_EVENT_LONGITUDE, "thumbnail", distinctEventTags, admin);
        var other = new Event("Event name", "Event description", 10, ZonedDateTime.now().plusDays(1), ZonedDateTime.now().plusDays(2), VALID_EVENT_LATITUDE, VALID_EVENT_LONGITUDE, "thumbnail", Set.of(differentTag), admin);

        manager.persist(event);
        manager.persist(other);

        URL += participant.getId();
        // WHEN
        var response = mvc.get(URL);

        // THEN
        var eventResponseFieldNames = Arrays.stream(EventResponse.class.getDeclaredFields())
                .map(Field::getName)
                .toList();

        response
                .andExpect(mvc.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isNotEmpty())
                // expect to have two events
                .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(2)));
        // expect to have all fields from EventResponse
        eventResponseFieldNames.forEach(field -> {
            try {
                response.andExpect(MockMvcResultMatchers.jsonPath("$[*]." + field).exists());
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    @Test
    @DisplayName("Should return an matching event and exclude the recommendation that is not within player radius")
    void shouldReturnMatchingEventAndExcludeRecommendation_WhenSuccessfull() throws Exception {
        // GIVEN
        tags.forEach(tag -> participant.addInterest(tag));
        var differentTag = new Tag("OTHER TAG");
        manager.persist(differentTag);

        var distinctEventTags = new HashSet<>((Collection<Tag>) tags);
        distinctEventTags.add(differentTag);

        participant = playerRepository.save(participant);

        var event = new Event("Event name", "Event description", 10, ZonedDateTime.now().plusDays(1), ZonedDateTime.now().plusDays(2), VALID_EVENT_LATITUDE, VALID_EVENT_LONGITUDE, "thumbnail", distinctEventTags, admin);
        var other = new Event("Event name", "Event description", 10, ZonedDateTime.now().plusDays(1), ZonedDateTime.now().plusDays(2), -50.5353294F, -40.222423F, "thumbnail", Set.of(differentTag), admin);

        manager.persist(event);
        manager.persist(other);

        URL += participant.getId();
        // WHEN
        var response = mvc.get(URL);

        // THEN
        var eventResponseFieldNames = Arrays.stream(EventResponse.class.getDeclaredFields())
                .map(Field::getName)
                .toList();

        response
                .andExpect(mvc.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isNotEmpty())
                // expect to have two events
                .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(1)));
        // expect to have all fields from EventResponse
        eventResponseFieldNames.forEach(field -> {
            try {
                response.andExpect(MockMvcResultMatchers.jsonPath("$[*]." + field).exists());
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    @Test
    @DisplayName("Should remove events which player has a pending or accepted join request")
    void shouldRemoveEventsWhichPlayerHasPendingOrAcceptedJoinRequest() throws Exception {
        // GIVEN
        tags.forEach(tag -> participant.addInterest(tag));
        participant = playerRepository.save(participant);
        var event = new Event("Event name", "Event description", 10, ZonedDateTime.now().plusDays(1), ZonedDateTime.now().plusDays(2), VALID_EVENT_LATITUDE, VALID_EVENT_LONGITUDE, "thumbnail", new HashSet<>((Collection<Tag>) tags), admin);
        manager.persist(event);
        URL += participant.getId();

        // WHEN
        participant.sendInterest(event);
        manager.refresh(participant);
        var response = mvc.get(URL);

        // THEN
        response
                .andExpect(mvc.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isEmpty());
    }

    @Test
    @DisplayName("Should remove events which player is already participating")
    void shouldRemoveEventsWhichPlayerIsAlreadyParticipating() throws Exception {
        // GIVEN
        tags.forEach(tag -> participant.addInterest(tag));
        participant = playerRepository.save(participant);
        var event = new Event("Event name", "Event description", 10, ZonedDateTime.now().plusDays(1), ZonedDateTime.now().plusDays(2), VALID_EVENT_LATITUDE, VALID_EVENT_LONGITUDE, "thumbnail", new HashSet<>((Collection<Tag>) tags), admin);
        manager.persist(event);
        URL += participant.getId();

        // WHEN
        event.addParticipant(participant);
        manager.flush();
        var response = mvc.get(URL);

        // THEN
        response
                .andExpect(mvc.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isEmpty());
    }
}