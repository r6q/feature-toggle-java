package com.github.r6q.featuretoggle.components.feature;

import com.github.r6q.featuretoggle.common.rest.DataResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

import javax.validation.Valid;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;

@Tag(name = "features", description = "Feature controller")
@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/features")
class FeatureController {

    private final FeatureService service;

    @Operation(summary = "Get all features")
    @GetMapping
    List<Feature> getAll() {
        return service.getAll();
    }

    @Operation(summary = "Get feature by ID")
    @GetMapping("/{id}")
    DataResponse<Feature> getById(@PathVariable int id) {
        return new DataResponse<>(service.getById(id));
    }

    @Operation(summary = "Create new feature")
    @PostMapping
    ResponseEntity<Void> create(@Valid @RequestBody FeatureCreateRequest request, UriComponentsBuilder uriBuilder) {
        final var id = service.create(request);
        return ResponseEntity.created(uriBuilder.path("/api/v1/features").path("/{id}").build(id)).build();
    }

    @Operation(summary = "Update existing feature")
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void update(@PathVariable int id, @Valid @RequestBody FeatureUpdateRequest request) {
        service.update(id, request);
    }

    @Operation(summary = "Delete existing feature")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void delete(@PathVariable int id) {
        service.delete(id);
    }
}
