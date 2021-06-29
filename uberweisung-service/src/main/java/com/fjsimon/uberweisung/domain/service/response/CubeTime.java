package com.fjsimon.uberweisung.domain.service.response;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import java.util.List;

public class CubeTime {

    @JacksonXmlElementWrapper(localName = "Cube")
    private List<Cube> cubes;

}
