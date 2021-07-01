package com.fjsimon.uberweisung.domain.service.response;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.util.ArrayList;


public class CubeDeserializer extends JsonDeserializer<ReferenceRatesResponse.CubeRoot> {

    @Override
    public ReferenceRatesResponse.CubeRoot deserialize(JsonParser p, DeserializationContext ctx) throws IOException {

        ReferenceRatesResponse.CubeRoot cubeRoot = (ReferenceRatesResponse.CubeRoot) ctx.getAttribute("value");

        if (cubeRoot == null) {
            cubeRoot = new ReferenceRatesResponse.CubeRoot();
            cubeRoot.setCube(new ArrayList<>());
        }

        ReferenceRatesResponse.Cube cube = new ReferenceRatesResponse.Cube();

        while (p.nextToken() != JsonToken.END_OBJECT) {
            String name = p.getCurrentName();
            p.nextToken();
            String value = p.getValueAsString();

            if (name.equalsIgnoreCase("time")) {
                cubeRoot.setTime(value);
            }

            if (name.equalsIgnoreCase("currency")) {
                cube.setCurrency(value);
            }

            if (name.equalsIgnoreCase("rate")) {
                cube.setRate(value);
            }
        }

        if (cube.getCurrency() != null && cube.getRate() != null) {
            cubeRoot.getCube().add(cube);
        }

        ctx.setAttribute("value", cubeRoot);

        return cubeRoot;

    }
}