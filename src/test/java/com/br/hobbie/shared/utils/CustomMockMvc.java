package com.br.hobbie.shared.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.ContentResultMatchers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.result.StatusResultMatchers;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class CustomMockMvc {

    private final MockMvc mockMvc;

    public ResultActions post(String url, Map<String, Object> params) {
        try {
            String payload = new ObjectMapper()
                    .writeValueAsString(params);


            MockHttpServletRequestBuilder content = MockMvcRequestBuilders
                    .post(url)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .content(payload);

            return mockMvc.perform(content);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public ResultActions get(String url) {
        try {

            MockHttpServletRequestBuilder content = MockMvcRequestBuilders
                    .get(url)
                    .accept(MediaType.APPLICATION_JSON_VALUE);

            return mockMvc.perform(content);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }


    public StatusResultMatchers status() {
        return MockMvcResultMatchers.status();
    }

    public ResultActions put(String url, Map<String, Object> form) {
        try {
            String payload = new ObjectMapper()
                    .writeValueAsString(form);
            MockHttpServletRequestBuilder content = MockMvcRequestBuilders
                    .put(url)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .content(payload);
            return mockMvc.perform(content);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public ContentResultMatchers body() {
        return MockMvcResultMatchers.content();
    }
}