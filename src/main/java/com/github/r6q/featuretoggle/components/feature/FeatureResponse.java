package com.github.r6q.featuretoggle.components.feature;

import java.time.LocalDateTime;

public record FeatureResponse(int id, String name, String description, boolean enabled, LocalDateTime expiration) {
}
