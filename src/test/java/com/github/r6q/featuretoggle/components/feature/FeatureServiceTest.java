package com.github.r6q.featuretoggle.components.feature;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("FeatureService Unit Tests")
class FeatureServiceTest {

    @Mock
    private FeatureMapper mapper;

    @Mock
    private FeatureEntityRemapper remapper;

    @InjectMocks
    private FeatureService featureService;

    private FeatureEntity testEntity;
    private FeatureResponse testFeature;
    private LocalDateTime testExpiration;

    @BeforeEach
    void setUp() {
        testExpiration = LocalDateTime.of(2025, 12, 31, 23, 59);

        testEntity = FeatureEntity.builder()
                .id(1)
                .name("test-feature")
                .description("Test feature description")
                .enabled(true)
                .expiration(testExpiration)
                .build();

        testFeature = new FeatureResponse(
                1,
                "test-feature",
                "Test feature description",
                true,
                testExpiration
        );
    }

    @Test
    @DisplayName("getAll should return all features")
    void getAll_shouldReturnAllFeatures() {
        var entity = FeatureEntity.builder()
                .id(2)
                .name("another-feature")
                .description("Another description")
                .enabled(false)
                .expiration(testExpiration)
                .build();

        var feature = new FeatureResponse(
                2,
                "another-feature",
                "Another description",
                false,
                testExpiration
        );

        when(mapper.getAll()).thenReturn(List.of(testEntity, entity));
        when(remapper.entityToDto(testEntity)).thenReturn(testFeature);
        when(remapper.entityToDto(entity)).thenReturn(feature);

        List<FeatureResponse> result = featureService.getAll();

        assertThat(result).hasSize(2);
        assertThat(result).containsExactly(testFeature, feature);
        verify(mapper).getAll();
        verify(remapper).entityToDto(testEntity);
        verify(remapper).entityToDto(entity);
    }

    @Test
    @DisplayName("getAll should return empty list when no features exist")
    void getAll_shouldReturnEmptyList_whenNoFeaturesExist() {
        when(mapper.getAll()).thenReturn(List.of());

        List<FeatureResponse> result = featureService.getAll();

        assertThat(result).isEmpty();
        verify(mapper).getAll();
    }

    @Test
    @DisplayName("getById should return feature when found")
    void getById_shouldReturnFeature_whenFound() {
        int featureId = 1;

        when(mapper.getById(featureId)).thenReturn(Optional.of(testEntity));
        when(remapper.entityToDto(testEntity)).thenReturn(testFeature);

        FeatureResponse result = featureService.getById(featureId);

        assertThat(result).isEqualTo(testFeature);
        verify(mapper).getById(featureId);
        verify(remapper).entityToDto(testEntity);
    }

    @Test
    @DisplayName("getById should throw exception when feature not found")
    void getById_shouldThrowException_whenNotFound() {
        int featureId = 999;

        when(mapper.getById(featureId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> featureService.getById(featureId))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("todo change to specific exception");

        verify(mapper).getById(featureId);
    }

    @Test
    @DisplayName("create should create new feature and return its ID")
    void create_shouldCreateFeatureAndReturnId() {
        var request = new FeatureCreateRequest(
                "new-feature",
                "New feature description",
                true,
                testExpiration
        );

        var unsavedEntity = new FeatureEntity(
                0,
                "new-feature",
                "New feature description",
                true,
                testExpiration
        );

        int expectedId = 42;

        when(mapper.create(unsavedEntity)).thenReturn(expectedId);

        int result = featureService.create(request);

        assertThat(result).isEqualTo(expectedId);
        verify(mapper).create(unsavedEntity);
    }

    @Test
    @DisplayName("update should update existing feature")
    void update_shouldUpdateFeature() {
        int featureId = 1;
        var request = new FeatureUpdateRequest(
                featureId,
                "updated-feature",
                "Updated description",
                false,
                testExpiration
        );

        var toUpdateEntity = FeatureEntity.builder()
                .id(featureId)
                .name("updated-feature")
                .description("Updated description")
                .enabled(false)
                .expiration(testExpiration)
                .build();

        featureService.update(featureId, request);

        verify(mapper).update(toUpdateEntity);
    }

    @Test
    @DisplayName("delete should delete feature by ID")
    void delete_shouldDeleteFeature() {
        int featureId = 1;

        featureService.delete(featureId);

        verify(mapper).delete(featureId);
    }
}

