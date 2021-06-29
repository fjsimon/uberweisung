package com.fjsimon.uberweisung.domain.service.response;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;


@JacksonXmlRootElement(namespace = "http://www.ecb.int/vocabulary/2002-08-01/eurofxref", localName = "gesmes:Envelope")
public class ReferenceRatesResponse {

    @JacksonXmlProperty(namespace = "gesmes:subject")
    private String subject;

    @JacksonXmlProperty(namespace = "gesmes:Sender")
    private Sender sender;

    @JacksonXmlElementWrapper(localName = "Cube")
    private CubeTime cubeTime;

}


//<gesmes:Envelope xmlns:gesmes="http://www.gesmes.org/xml/2002-08-01" xmlns="http://www.ecb.int/vocabulary/2002-08-01/eurofxref">
//<gesmes:subject>Reference rates</gesmes:subject>
//<gesmes:Sender>
//<gesmes:name>European Central Bank</gesmes:name>
//</gesmes:Sender>
//<Cube>
//<Cube time="2021-06-25">
//<Cube currency="USD" rate="1.1950"/>
//<Cube currency="ZAR" rate="16.8359"/>
//</Cube>
//</Cube>
//</gesmes:Envelope>