package com.github.r6q.featuretoggle.common.rest;

import java.util.Collections;
import java.util.Map;

public record Error(ErrorCode code, String message, Map<String, String> params) {
    public static Error of(ErrorCode code, String message) {
        return new Error(code, message, Collections.emptyMap());
    }
    public static Error of(ErrorCode code, String message, Map<String, String> params) {
        return new Error(code, message, params);
    }
}
