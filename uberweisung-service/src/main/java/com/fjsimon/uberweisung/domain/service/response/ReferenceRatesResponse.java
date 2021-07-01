package com.fjsimon.uberweisung.domain.service.response;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Data;
import java.util.List;

@Data
@JacksonXmlRootElement(namespace = "http://www.ecb.int/vocabulary/2002-08-01/eurofxref", localName = "gesmes:Envelope")
public class ReferenceRatesResponse {

    @JacksonXmlProperty(namespace = "gesmes:subject")
    private String subject;

    @JacksonXmlProperty(namespace = "gesmes:Sender")
    private Sender sender;

    @JacksonXmlProperty(localName = "Cube")
    private CubeRoot cube;

    @Data
    public static class Sender {

        @JacksonXmlProperty(namespace = "gesmes:name")
        private String name;
    }

    @Data
    public static class CubeRoot {

        @JacksonXmlProperty(isAttribute = true)
        private String time;

        @JacksonXmlElementWrapper(localName = "Cube")
        private List<Cube> cube;
    }

    @Data
    public static class Cube {

        @JacksonXmlProperty(isAttribute = true)
        private String currency;

        @JacksonXmlProperty(isAttribute = true)
        private String rate;
    }

}