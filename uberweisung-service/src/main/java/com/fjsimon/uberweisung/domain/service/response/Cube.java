package com.fjsimon.uberweisung.domain.service.response;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

public class Cube {

    @JacksonXmlProperty(isAttribute = true)
    private String currency;

    @JacksonXmlProperty(isAttribute = true)
    private String rate;
}
