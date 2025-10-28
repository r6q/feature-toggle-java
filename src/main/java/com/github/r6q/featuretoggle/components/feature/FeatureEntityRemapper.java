package com.github.r6q.featuretoggle.components.feature;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
interface FeatureEntityRemapper {
    FeatureResponse entityToDto(FeatureEntity destination);
}
