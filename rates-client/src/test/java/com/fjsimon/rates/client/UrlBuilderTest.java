package com.fjsimon.rates.client;


import org.junit.Test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class UrlBuilderTest {

    @Test
    public void regex_test() {

        Matcher matcher = Pattern.compile("#\\{(.*?)\\}")
                .matcher("#{baseUrl}");

        assertThat(matcher.find(), is(true));
        assertThat(matcher.group(1), is("baseUrl"));


    }
}