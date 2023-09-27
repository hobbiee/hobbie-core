package com.br.hobbie.modules.event.infrastructure.http.controllers;

import com.br.hobbie.modules.player.domain.entities.Player;
import com.br.hobbie.modules.player.domain.repositories.PlayerRepository;
import com.br.hobbie.shared.factory.PlayerEventTestFactory;
import com.br.hobbie.shared.utils.CustomMockMvc;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.HashMap;
import java.util.Map;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        properties = "spring.profiles.active=test"
)
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
        player = playerRepository.save(PlayerEventTestFactory.createPlayer());
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
}