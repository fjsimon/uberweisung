package com.fjsimon.uberweisung.domain.service.response;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

public class Sender {

    @JacksonXmlProperty(namespace = "gesmes:name")
    private String name;

}
