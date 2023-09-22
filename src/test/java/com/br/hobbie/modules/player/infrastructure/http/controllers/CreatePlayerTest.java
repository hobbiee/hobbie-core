package com.br.hobbie.modules.player.infrastructure.http.controllers;

import com.br.hobbie.shared.utils.CustomMockMvc;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Map;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, properties = "spring.profiles.active=test")
@AutoConfigureMockMvc
class CreatePlayerTest {
    static final String URL = "/v1/api/players";

    @Autowired
    private CustomMockMvc mvc;

    @Test
    @DisplayName("Should create a player with success")
    void createPlayerSuccessfully() throws Exception {
        // GIVEN
        Map<String, Object> params = Map.of(
                "name", "John Doe",
                "avatar", "https://www.google.com",
                "latitude", 10,
                "longitude", 10,
                "birthDate", "1990-01-01",
                "interests", new String[]{"FOOTBALL", "MUSIC"}
        );

        // WHEN
        var result = mvc.post(URL, params);

        // THEN
        result.andExpect(mvc.status().isCreated());
    }

    @Test
    @DisplayName("Should not create a player with invalid data")
    void createPlayerWithInvalidData() {
    }


}