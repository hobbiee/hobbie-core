package com.br.hobbie.modules.player.infrastructure.http.controllers;

import com.br.hobbie.shared.utils.CustomMockMvc;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

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
                "radius", 400,
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
    void createPlayer_ReturnsError_WithInvalidData() throws Exception {
        // GIVEN
        Map<String, Object> params = Map.of(
                "name", "",
                "avatar", "https://www.google.com",
                "latitude", 10,
                "longitude", 10,
                "birthDate", "1990-01-01",
                "interests", new String[]{"soccer", "basketball"}
        );

        // WHEN
        var result = mvc.post(URL, params);

        // THEN
        result.andExpect(mvc.status().isBadRequest());
        result.andExpect(mvc.status().is(HttpStatus.BAD_REQUEST.value()));
        MockMvcResultMatchers.jsonPath("$.errors").exists();
        MockMvcResultMatchers.jsonPath("$.errors").isNotEmpty();
    }


    @Test
    @DisplayName("Should not be able to create a player without interests")
    void createPlayer_ReturnsError_WithoutInterests() throws Exception {
        // GIVEN
        Map<String, Object> params = Map.of(
                "name", "John Doe",
                "avatar", "https://www.google.com",
                "latitude", 10,
                "longitude", 10,
                "radius", 100,
                "birthDate", "1990-01-01",
                "interests", new String[]{}
        );

        // WHEN
        var result = mvc.post(URL, params);

        // THEN
        result.andExpect(mvc.status().isBadRequest());
        result.andExpect(mvc.status().is(HttpStatus.BAD_REQUEST.value()));
        MockMvcResultMatchers.jsonPath("$.errors").exists();
        MockMvcResultMatchers.jsonPath("$.errors").isNotEmpty();
    }


}