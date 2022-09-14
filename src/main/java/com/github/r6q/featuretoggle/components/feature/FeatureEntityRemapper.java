package com.github.r6q.featuretoggle.components.feature;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
interface FeatureEntityRemapper {
    FeatureEntity dtoToEntity(Feature source);
    Feature entityToDto(FeatureEntity destination);
}
