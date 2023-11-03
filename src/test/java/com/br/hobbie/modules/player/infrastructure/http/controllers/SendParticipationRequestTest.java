package com.br.hobbie.modules.player.infrastructure.http.controllers;

import com.br.hobbie.modules.event.domain.entities.Event;
import com.br.hobbie.modules.player.domain.entities.Player;
import com.br.hobbie.shared.factory.PlayerEventTestFactory;
import com.br.hobbie.shared.utils.CustomMockMvc;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, properties = "spring.profiles.active=test")
@ExtendWith(SpringExtension.class)
@Transactional
class SendParticipationRequestTest {
    private static final String URL = "/v1/api/players/interest";

    private Player joiningPlayer;

    private Event event;

    @Autowired
    private EntityManager manager;

    @Autowired
    private CustomMockMvc mvc;

    @BeforeEach
    void setUp() {
        Player admin = PlayerEventTestFactory.createPlayerToSave();
        joiningPlayer = PlayerEventTestFactory.createParticipantPlayerToSave();
        manager.persist(admin);
        manager.persist(joiningPlayer);
        event = PlayerEventTestFactory.createEventToSave(admin);
        manager.persist(event);
    }

    @Test
    @DisplayName("Should returns a 422 error when event does not exist in database")
    void shouldReturns422WhenEventDoesNotExist() throws Exception {
        // given
        Map<String, Object> form = Map.of(
                "playerId", joiningPlayer.getId(),
                "eventId", 999999999
        );

        // when
        ResultActions result = mvc.post(URL, form);

        // then
        result
                .andExpect(mvc.status().isUnprocessableEntity())
                .andExpect(mvc.body().string("Something went wrong, maybe the entered event or player does not exist"));

    }

    @Test
    @DisplayName("Should returns a 422 error when player does not exist in database")
    void shouldReturns422WhenInterestedPlayerDoesNotExist() throws Exception {
        // given
        Map<String, Object> form = Map.of(
                "playerId", 999999999,
                "eventId", event.getId()
        );

        // when
        ResultActions result = mvc.post(URL, form);

        // then
        result
                .andExpect(mvc.status().isUnprocessableEntity())
                .andExpect(mvc.body().string("Something went wrong, maybe the entered event or player does not exist"));

    }
}