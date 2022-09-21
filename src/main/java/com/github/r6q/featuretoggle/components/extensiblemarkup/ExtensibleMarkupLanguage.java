package com.github.r6q.featuretoggle.components.extensiblemarkup;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

@JacksonXmlRootElement(localName = "ApiResponse")
@JsonNaming(PropertyNamingStrategies.UpperCamelCaseStrategy.class)
record ExtensibleMarkupLanguage(
        int id,
        String name,
        @JacksonXmlProperty(isAttribute = true)
        String status,
        @JacksonXmlProperty(localName = "GMTTime")
        String gmtTime
) {
}
