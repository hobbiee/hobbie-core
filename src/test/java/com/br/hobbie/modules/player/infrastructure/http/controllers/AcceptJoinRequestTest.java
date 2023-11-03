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
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, properties = "spring.profiles.active=test")
@Transactional
class AcceptJoinRequestTest {

    private static final String URL = "/v1/api/players/accept-join-request";

    private Player admin;

    private Player playerToAccept;

    private Event event;

    @Autowired
    private EntityManager manager;

    @Autowired
    private CustomMockMvc mvc;

    @BeforeEach
    void setUp() {
        admin = PlayerEventTestFactory.createPlayerToSave();
        playerToAccept = PlayerEventTestFactory.createParticipantPlayerToSave();
        manager.persist(admin);
        manager.persist(playerToAccept);
        event = PlayerEventTestFactory.createEventToSave(admin);
        manager.persist(event);
    }

    @Test
    @DisplayName("Should returns a 422 error when the player who is accepting does not exist in database")
    void shouldThrowAnErrorAndReturns500WhenAdminPlayerDoesNotExist() throws Exception {
        // given
        Map<String, Object> form = Map.of(
                "adminId", 999999999,
                "playerToAcceptId", playerToAccept.getId(),
                "eventId", event.getId()
        );

        // when
        mvc.put(URL, form)
                .andExpect(mvc.status().isUnprocessableEntity())
                // assert the error message
                .andExpect(mvc.body().string("Something went wrong, maybe the entered player who is accepting does not exist"));
    }

    @Test
    @DisplayName("Should returns a 422 error when the player who is being accepted does not exist in database")
    void shouldThrowAnErrorAndReturns500WhenPlayerToAcceptDoesNotExist() throws Exception {
        // given
        Map<String, Object> form = Map.of(
                "adminId", admin.getId(),
                "playerToAcceptId", 999999999,
                "eventId", event.getId()
        );

        // when
        mvc.put(URL, form)
                .andExpect(mvc.status().isUnprocessableEntity())
                // assert the error message
                .andExpect(mvc.body().string("Something went wrong, maybe the entered player who is being accepted does not exist"));
    }

    @Test
    @DisplayName("Should returns a 422 when event does not exist")
    void shouldThrowAnErrorAndReturns500WhenEventDoesNotExist() throws Exception {
        // given
        Map<String, Object> form = Map.of(
                "adminId", admin.getId(),
                "playerToAcceptId", playerToAccept.getId(),
                "eventId", 999999999
        );

        // when
        mvc.put(URL, form)
                .andExpect(mvc.status().isUnprocessableEntity())
                // assert the error message
                .andExpect(mvc.body().string("Something went wrong, maybe the entered event does not exist"));
    }

    @Test
    @DisplayName("Should accept a join request")
    void shouldAcceptAJoinRequest() throws Exception {
        // given
        playerToAccept.sendInterest(event);
        manager.refresh(playerToAccept);

        Map<String, Object> form = Map.of(
                "adminId", admin.getId(),
                "playerToAcceptId", playerToAccept.getId(),
                "eventId", event.getId()
        );

        // when
        mvc.put(URL, form)
                .andExpect(mvc.status().isOk());
    }

    @Test
    @DisplayName("Should return a 422 when the player who is accepting is not the admin of the event")
    void shouldThrowAnErrorAndReturns500WhenPlayerWhoIsAcceptingIsNotTheAdminOfTheEvent() throws Exception {
        // given
        Map<String, Object> form = Map.of(
                "adminId", playerToAccept.getId(),
                "playerToAcceptId", playerToAccept.getId(),
                "eventId", event.getId()
        );

        // when
        mvc.put(URL, form)
                .andExpect(mvc.status().isUnprocessableEntity())
                // assert the error message
                .andExpect(mvc.body().string("Player must be the admin of the event"));
    }

    @Test
    @DisplayName("Should return a 422 when the player who is requesting to join has not sent a request")
    void shouldThrowAnErrorAndReturns500WhenPlayerWhoIsRequestingToJoinHasNotSentARequest() throws Exception {
        // given
        Map<String, Object> form = Map.of(
                "adminId", admin.getId(),
                "playerToAcceptId", playerToAccept.getId(),
                "eventId", event.getId()
        );

        // when
        mvc.put(URL, form)
                .andExpect(mvc.status().isUnprocessableEntity())
                // assert the error message
                .andExpect(mvc.body().string("Player must have a join request"));
    }
}