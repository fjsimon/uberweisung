package com.fjsimon.rates.client;

public final class Urls {

    // baseUrl = https://www.ecb.europa.eu/stats/eurofxref/eurofxref-daily.xml
    public static final UrlBuilder.Spec GET_RATES = new UrlBuilder.Spec("#{baseUrl}");

    private Urls() {}

}
