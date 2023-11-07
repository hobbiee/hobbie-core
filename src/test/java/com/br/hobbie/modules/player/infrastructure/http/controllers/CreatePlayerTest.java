package com.br.hobbie.modules.player.infrastructure.http.controllers;

import com.br.hobbie.shared.utils.CustomMockMvc;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;


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
        MockMultipartFile file
                = new MockMultipartFile(
                "file",
                "hello.txt",
                MediaType.TEXT_PLAIN_VALUE,
                "Hello, World!".getBytes()
        );

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("name", "John Doe");
        params.add("latitude", "-40.53983");
        params.add("longitude", "-20.93483");
        params.add("radius", "400");
        params.add("birthDate", "1990-01-01");
        params.add("interests", "FOOTBALL");
        params.add("interests", "BASKETBALL");


        // WHEN
        var result = mvc.multipart(URL, file, params);

        // THEN
        result.andExpect(mvc.status().isCreated());
    }

    @Test
    @DisplayName("Should not create a player with invalid data")
    void createPlayer_ReturnsError_WithInvalidData() throws Exception {
        // GIVEN
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("name", "John Doe");
        params.add("latitude", "-40.53983");
        params.add("longitude", "-20.93483");
        params.add("radius", "0");
        params.add("birthDate", "1990-01-01");
        params.add("interests", "FOOTBALL");
        params.add("interests", "BASKETBALL");


        MockMultipartFile file
                = new MockMultipartFile(
                "file",
                "hello.txt",
                MediaType.TEXT_PLAIN_VALUE,
                "Hello, World!".getBytes()
        );

        // WHEN
        var result = mvc.multipart(URL, file, params);

        // THEN
        result
                .andExpect(mvc.status().isBadRequest())
                .andExpect(mvc.status().is(HttpStatus.BAD_REQUEST.value()));
    }


    @Test
    @DisplayName("Should not be able to create a player without interests")
    void createPlayer_ReturnsError_WithoutInterests() throws Exception {
        // GIVEN
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("name", "John Doe");
        params.add("latitude", "-40.53983");
        params.add("longitude", "-20.93483");
        params.add("radius", "400");
        params.add("birthDate", "1990-01-01");

        MockMultipartFile file
                = new MockMultipartFile(
                "file",
                "hello.txt",
                MediaType.TEXT_PLAIN_VALUE,
                "Hello, World!".getBytes()
        );

        // WHEN
        var result = mvc.multipart(URL, file, params);

        // THEN
        result.andExpect(mvc.status().isBadRequest())
                .andExpect(mvc.status().is(HttpStatus.BAD_REQUEST.value()));
    }


}