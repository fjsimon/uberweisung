package com.fjsimon.rates.client;


import org.junit.Test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class UrlBuilderTest {

    @Test
    public void regex_test() {

        Matcher matcher = Pattern.compile("#\\{(.*?)\\}")
                .matcher("#{baseUrl}/books?bibkeys=ISBN:#{isbn}&jscmd=#{jscmd}&format=#{format}");

        assertThat(matcher.find(), is(true));
        assertThat(matcher.group(1), is("baseUrl"));

        assertThat(matcher.find(), is(true));
        assertThat(matcher.group(1), is("isbn"));

        assertThat(matcher.find(), is(true));
        assertThat(matcher.group(1), is("jscmd"));

        assertThat(matcher.find(), is(true));
        assertThat(matcher.group(1), is("format"));

    }
}