package com.github.r6q.featuretoggle.components.feature;

import com.github.r6q.featuretoggle.common.rest.DataResponse;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
class FeatureController implements FeatureApi {

    private final FeatureService service;

    @Override
    public List<FeatureResponse> getAll() {
        return service.getAll();
    }

    @Override
    public DataResponse<FeatureResponse> getById(int id) {
        return new DataResponse<>(service.getById(id));
    }

    @Override
    public ResponseEntity<Void> create(FeatureCreateRequest request, UriComponentsBuilder uriBuilder) {
        final var id = service.create(request);
        return ResponseEntity.created(uriBuilder.path("/api/v1/features").path("/{id}").build(id)).build();
    }

    @Override
    public void update(int id, FeatureUpdateRequest request) {
        service.update(id, request);
    }

    @Override
    public void delete(int id) {
        service.delete(id);
    }
}
