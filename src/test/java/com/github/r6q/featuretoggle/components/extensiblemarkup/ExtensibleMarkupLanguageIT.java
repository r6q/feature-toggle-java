package com.github.r6q.featuretoggle.components.extensiblemarkup;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.converter.xml.MappingJackson2XmlHttpMessageConverter;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
@Tag("itest")
@Import(MappingJackson2XmlHttpMessageConverter.class)
class ExtensibleMarkupLanguageIT {

    @Autowired
    private MappingJackson2XmlHttpMessageConverter converter;

    @Test
    void shouldSerialize() throws Exception {
        var request = new ExtensibleMarkupLanguage(123, "Test", "COMPLETED", "14:55");

        String s = converter.getObjectMapper().writeValueAsString(request);

        System.out.println(s);
    }

    @Test
    void shouldDeserialize() throws Exception {
        // TODO move to utility method
//        URL resource = this.getClass().getResource(getClass().getSimpleName() + "/" + "api-response.xml");
        var xml = """
<ApiResponse Status="COMPLETED">
    <GMTTime>14:55</GMTTime>
    <Id>123</Id>
    <Name>Test</Name>
</ApiResponse>
""";

        ExtensibleMarkupLanguage xmlDto = converter.getObjectMapper().readValue(xml, ExtensibleMarkupLanguage.class);

        System.out.println(xmlDto);

        assertThat(xmlDto).isEqualTo(new ExtensibleMarkupLanguage(123, "Test", "COMPLETED", "14:55"));
    }
}
