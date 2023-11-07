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
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, properties = "spring.profiles.active=test")
@Transactional
class RejectJoinRequestTest {
    private static final String URL = "/v1/api/players/reject-join-request";

    private Player admin;

    private Player joiningPlayer;

    private Event event;

    @Autowired
    private CustomMockMvc mvc;

    @Autowired
    private EntityManager manager;

    @BeforeEach
    void setUp() {
        admin = PlayerEventTestFactory.createPlayerToSave();
        joiningPlayer = PlayerEventTestFactory.createParticipantPlayerToSave();
        manager.persist(admin);
        manager.persist(joiningPlayer);
        event = PlayerEventTestFactory.createEventToSave(admin);
        manager.persist(event);
    }

    @Test
    @DisplayName("Should returns a 422 error when admin does not exist in database")
    void shouldReturns422WhenAdminPlayerDoesNotExist() throws Exception {
        // given
        Map<String, Object> form = Map.of(
                "adminId", 999999999,
                "playerToRejectId", joiningPlayer.getId(),
                "eventId", event.getId()
        );

        // when
        ResultActions result = mvc.put(URL, form);

        // then
        result
                .andExpect(mvc.status().isUnprocessableEntity())
                .andExpect(mvc.body().string("Check if you are passing the correct admin id"));
    }

    @Test
    @DisplayName("Should returns a 422 error when joining player does not exist in database")
    void shouldReturns422WhenJoiningPlayerDoesNotExist() throws Exception {
        // given
        Map<String, Object> form = Map.of(
                "adminId", admin.getId(),
                "playerToRejectId", 999999999,
                "eventId", event.getId()
        );

        // when
        ResultActions result = mvc.put(URL, form);

        // then
        result
                .andExpect(mvc.status().isUnprocessableEntity())
                .andExpect(mvc.body().string("Check if you are passing the correct player to reject id"));
    }

    @Test
    @DisplayName("Should returns a 422 when event does not exist")
    void shouldReturns422WhenEventDoesNotExist() throws Exception {
        // given
        Map<String, Object> form = Map.of(
                "adminId", admin.getId(),
                "playerToRejectId", joiningPlayer.getId(),
                "eventId", 999999999
        );

        // when
        ResultActions result = mvc.put(URL, form);

        // then
        result
                .andExpect(mvc.status().isUnprocessableEntity())
                .andExpect(mvc.body().string("Check if you are passing the correct event id"));
    }


    @Test
    @DisplayName("Should returns a 422 when admin is not the owner of the event")
    void shouldReturns422WhenAdminIsNotTheOwnerOfTheEvent() throws Exception {
        // given
        Map<String, Object> form = Map.of(
                "adminId", joiningPlayer.getId(),
                "playerToRejectId", joiningPlayer.getId(),
                "eventId", event.getId()
        );

        // when
        ResultActions result = mvc.put(URL, form);

        // then
        result
                .andExpect(mvc.status().isUnprocessableEntity())
                .andExpect(mvc.body().string("Player must be the admin of the event"));
    }

    @Test
    @DisplayName("Should reject a join request")
    void shouldRejectAJoinRequest() throws Exception {
        // given
        joiningPlayer.sendInterest(event);
        manager.refresh(joiningPlayer);

        Map<String, Object> form = Map.of(
                "adminId", admin.getId(),
                "playerToRejectId", joiningPlayer.getId(),
                "eventId", event.getId()
        );

        // when
        ResultActions result = mvc.put(URL, form);

        // then
        result
                .andExpect(mvc.status().isOk());
    }

    @Test
    @DisplayName("Should return a 422 when the joining player does not have a join request")
    void shouldReturns422WhenJoiningPlayerDoesNotHaveAJoinRequest() throws Exception {
        // given
        Map<String, Object> form = Map.of(
                "adminId", admin.getId(),
                "playerToRejectId", joiningPlayer.getId(),
                "eventId", event.getId()
        );

        // when
        ResultActions result = mvc.put(URL, form);

        // then
        result
                .andExpect(mvc.status().isUnprocessableEntity())
                .andExpect(mvc.body().string("Player must have a join request"));
    }
}