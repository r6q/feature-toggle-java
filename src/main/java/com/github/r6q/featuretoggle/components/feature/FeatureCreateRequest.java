package com.github.r6q.featuretoggle.components.feature;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

record FeatureCreateRequest(
        @NotBlank(message = "Name is required")
        @Size(min = 3, max = 30)
        String name,
        @Size(max = 2000)
        String description,
        boolean enabled,
        LocalDateTime expiration
) {
}
