package com.br.hobbie.modules.event.infrastructure.http.controllers;

import com.br.hobbie.modules.player.domain.entities.Player;
import com.br.hobbie.modules.player.domain.repositories.PlayerRepository;
import com.br.hobbie.shared.factory.PlayerEventTestFactory;
import com.br.hobbie.shared.utils.CustomMockMvc;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        properties = "spring.profiles.active=test"
)
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
class CreateEventTest {
    static final String URL = "/v1/api/events";

    private Player player;

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private CustomMockMvc mvc;

    @BeforeEach
    void setUp() {
        player = playerRepository.save(PlayerEventTestFactory.createPlayerToSave());
    }

    @Test
    @DisplayName("Should create an event")
    void shouldCreate_WhenSuccessfull() throws Exception {
        // GIVEN
        Map<String, Object> request = new HashMap<>(PlayerEventTestFactory.createEventRequest());
        request.put("adminId", player.getId());

        // WHEN
        var response = mvc.post(URL, request);


        // THEN
        response
                .andExpect(mvc.status().isCreated());
        MockMvcResultMatchers.jsonPath("$.id").exists();
        MockMvcResultMatchers.jsonPath("$.name").value(request.get("name"));
    }

    @Test
    @DisplayName("Should not create an event when does not have admin")
    void shouldNotCreate_WhenDoesNotHaveAdmin() throws Exception {
        // GIVEN
        Map<String, Object> request = new HashMap<>(PlayerEventTestFactory.createEventRequest());

        // WHEN
        var response = mvc.post(URL, request);

        // THEN
        response
                .andExpect(mvc.status().isBadRequest());
    }

    @Test
    @DisplayName("Should return Unprocessable Entity when admin id is invalid")
    void shouldReturnUnprocessableEntity_WhenAdminIdIsInvalid() throws Exception {
        // GIVEN
        Map<String, Object> request = new HashMap<>(PlayerEventTestFactory.createEventRequest());
        request.put("adminId", 10);

        // WHEN
        var response = mvc.post(URL, request);

        // THEN
        response
                .andExpect(mvc.status().isUnprocessableEntity());
        MockMvcResultMatchers.jsonPath("$.errors").isArray();
    }


    @Test
    @DisplayName("Should not create an event on past date")
    void shouldNotCreate_WhenPastDate() throws Exception {
        // GIVEN
        Map<String, Object> request = new HashMap<>(PlayerEventTestFactory.createEventRequest());
        request.put("adminId", player.getId());
        request.put("startDate", LocalDate.now().minusDays(1).toString());

        // WHEN
        var response = mvc.post(URL, request);

        // THEN
        response
                .andExpect(mvc.status().isBadRequest());
    }

    @Test
    @DisplayName("Should not create an event when date is more than 7 days from now")
    void shouldNotCreate_WhenDateIsMoreThan7DaysFromNow() throws Exception {
        // GIVEN
        Map<String, Object> request = new HashMap<>(PlayerEventTestFactory.createEventRequest());
        request.put("adminId", player.getId());
        request.put("startDate", LocalDate.now().plusDays(8).toString());

        // WHEN
        var response = mvc.post(URL, request);

        // THEN
        response
                .andExpect(mvc.status().isBadRequest());
    }

    @Test
    @DisplayName("Should not create an event when start time is after end time")
    void shouldNotCreate_WhenStartTimeIsAfterEndTime() throws Exception {
        // GIVEN
        Map<String, Object> request = new HashMap<>(PlayerEventTestFactory.createEventRequest());
        request.put("adminId", player.getId());
        request.put("startDate", ZonedDateTime.now().plusHours(1).toString());
        request.put("endDate", ZonedDateTime.now().toString());

        // WHEN
        var response = mvc.post(URL, request);

        // THEN
        response
                .andExpect(mvc.status().isBadRequest());
    }
}