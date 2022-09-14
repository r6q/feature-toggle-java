package com.github.r6q.featuretoggle.components.feature;

import java.time.LocalDateTime;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

record FeatureUpdateRequest(
        int id,
        @NotBlank(message = "Name is required")
        @Size(min = 3, max = 30)
        String name,
        @Size(max = 2000)
        String description,
        boolean enabled,
        LocalDateTime expiration
) {
}
