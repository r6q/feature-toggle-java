package com.github.r6q.featuretoggle.components.feature;

import com.github.r6q.featuretoggle.test.IntegrationTest;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@IntegrationTest
class FeatureControllerIT {

    private final MockMvc mvc;

    public FeatureControllerIT(MockMvc mvc) {
        this.mvc = mvc;
    }

    @Test
    @DisplayName("should create new feature and retrieve it by ID")
    void getFeatureById() throws Exception {
        String id = create();

        mvc.perform(get("/api/v1/features/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(id))
                .andExpect(jsonPath("$.data.name").value("test-name"))
                .andExpect(jsonPath("$.data.description").value("a description"))
                .andExpect(jsonPath("$.data.enabled").value(true))
                .andExpect(jsonPath("$.data.expiration").value("2023-12-22T12:55:00"));
    }

    @Test
    @DisplayName("should update existing feature")
    void update() throws Exception {
        var id = create();
        var updateRequest = """
                {
                  "name": "updated-name",
                  "description": "updated description",
                  "enabled": false,
                  "expiration": "2024-01-01T00:00:00"
                }
                """;

        mvc.perform(put("/api/v1/features/{id}", id)
                        .content(updateRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("should create new feature and delete it")
    void deleteFeature() throws Exception {
        var id = create();

        mvc.perform(delete("/api/v1/features/{id}", id)).andExpect(status().isNoContent());
    }

    private String create() throws Exception {
        var header = mvc.perform(post("/api/v1/features")
                        .content(createFeatureRequest("test-name"))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getHeader(HttpHeaders.LOCATION);

        assertThat(header).isNotNull();

        return header.substring(header.lastIndexOf("/") + 1);
    }

    private String createFeatureRequest(String name) {
        return """
                {
                  "name": "%s",
                  "description": "a description",
                  "enabled": true,
                  "expiration": "2023-12-22T12:55:00"
                }
                """.formatted(name);
    }
}
