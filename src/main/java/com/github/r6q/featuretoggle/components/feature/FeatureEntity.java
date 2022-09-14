package com.github.r6q.featuretoggle.components.feature;

import java.time.LocalDateTime;

import lombok.Builder;

@Builder
record FeatureEntity (
    int id,
    String name,
    String description,
    boolean enabled,
    LocalDateTime expiration
){}
