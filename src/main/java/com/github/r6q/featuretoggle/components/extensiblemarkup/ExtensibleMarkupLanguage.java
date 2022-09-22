package com.github.r6q.featuretoggle.components.extensiblemarkup;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

@JacksonXmlRootElement(localName = "ApiResponse")
@JsonNaming(PropertyNamingStrategies.UpperCamelCaseStrategy.class)
record ExtensibleMarkupLanguage(
        @JacksonXmlProperty(localName = "Id")
        int id,
        @JacksonXmlProperty(localName = "Name")
        String name,
        @JacksonXmlProperty(isAttribute = true, localName = "Status")
        String status,
        @JacksonXmlProperty(localName = "GMTTime")
        String gmtTime
) {
}
