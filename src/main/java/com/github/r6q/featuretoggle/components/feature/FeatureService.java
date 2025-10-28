package com.github.r6q.featuretoggle.components.feature;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import static net.logstash.logback.argument.StructuredArguments.v;

@Slf4j
@Service
@RequiredArgsConstructor
class FeatureService {

    private final FeatureMapper mapper;
    private final FeatureEntityRemapper remapper;

    @Transactional(readOnly = true)
    List<FeatureResponse> getAll() {
        return mapper.getAll().stream()
                .map(remapper::entityToDto)
                .toList();
    }

    @Transactional(readOnly = true)
    FeatureResponse getById(int id) {
        return mapper.getById(id)
                .map(remapper::entityToDto)
                .orElseThrow(() -> new IllegalStateException("todo change to specific exception"));
    }

    @Transactional
    int create(FeatureCreateRequest request) {
        final var entity = FeatureEntity.builder()
                .name(request.name())
                .description(request.description())
                .enabled(request.enabled())
                .expiration(request.expiration())
                .build();

        return mapper.create(entity);
    }

    @Transactional
    void update(int id, FeatureUpdateRequest request) {
        log.info("Updating feature with ID {}", v("ID", id)); // Test structured `StructuredArgument` logging
        final var entity = FeatureEntity.builder()
                .id(id)
                .name(request.name())
                .description(request.description())
                .enabled(request.enabled())
                .expiration(request.expiration())
                .build();

        mapper.update(entity);
    }

    @Transactional
    void delete(int id) {
        mapper.delete(id);
    }
}
