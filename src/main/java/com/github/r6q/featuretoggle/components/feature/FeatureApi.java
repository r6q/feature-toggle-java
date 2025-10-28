package com.github.r6q.featuretoggle.components.feature;

import com.github.r6q.featuretoggle.common.rest.DataResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "features", description = "Feature controller")
@RequestMapping(path = "/api/v1/features", produces = MediaType.APPLICATION_JSON_VALUE)
public interface FeatureApi {

    @Operation(summary = "Get all features")
    @GetMapping
    List<FeatureResponse> getAll();

    @Operation(summary = "Get feature by ID")
    @GetMapping("/{id}")
    DataResponse<FeatureResponse> getById(@PathVariable int id);

    @Operation(summary = "Create new feature")
    @PostMapping
    ResponseEntity<Void> create(@Valid @RequestBody FeatureCreateRequest request, UriComponentsBuilder uriBuilder);

    @Operation(summary = "Update existing feature")
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void update(@PathVariable int id, @Valid @RequestBody FeatureUpdateRequest request);

    @Operation(summary = "Delete existing feature")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void delete(@PathVariable int id);

}
