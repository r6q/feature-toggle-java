package com.github.r6q.featuretoggle.components.feature;

import java.time.LocalDateTime;

record Feature(int id, String name, String description, boolean enabled, LocalDateTime expiration) {
}
