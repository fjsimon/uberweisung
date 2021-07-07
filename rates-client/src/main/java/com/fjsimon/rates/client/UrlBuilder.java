package com.fjsimon.rates.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class UrlBuilder {

    @Autowired
    @Qualifier("exchangeRatesBaseUri")
    private String baseUrl;

    public LinkBuilder aLink(Spec spec) {

        return new LinkBuilder(spec).with("baseUrl", baseUrl);
    }

    public String getUrl() {
        return baseUrl;
    }

    public static class Spec {

        private final String url;
        private final Set<String> vars;

        public Spec(String url) {
            this.url = url;
            this.vars = new HashSet<>();

            Matcher matcher = Pattern.compile("#\\{(.*?)\\}").matcher(url);
            while (matcher.find()) {
                getVars().add(matcher.group(1));
            }
        }

        public String getUrl() {
            return url;
        }

        public Set<String> getVars() {
            return vars;
        }
    }

    public static final class LinkBuilder {
        private final Spec type;
        private final Map<String, String> vars = new HashMap<>();
        private final Map<String, String> params = new LinkedHashMap<>();

        public LinkBuilder(Spec type) {
            this.type = type;
        }

        public LinkBuilder with(String variable, String value) {

            if (vars.containsKey(variable)) {
                throw new IllegalStateException("already set");
            } else if(!type.getVars().contains(variable)) {
                throw new IllegalStateException("not valid");
            }

            vars.put(variable, value);
            return this;
        }

        public LinkBuilder withParams(String parameter, String value) {

            if(params.containsKey(parameter)) {
                throw new IllegalStateException("already set");
            }
            params.put(parameter, value);
            return this;
        }

        public String build() {
            return addParams(toUrl());
        }

        private String toUrl() {
            String url = type.getUrl();
            for(String var : type.getVars()) {
                if(!vars.containsKey(var)) {
                    throw new IllegalStateException("not provided");
                }
                url = url.replace("#{" + var + "}", vars.get(var));
            }
            return url;
        }

        private String addParams(String url) {
            StringBuilder sb = new StringBuilder();
            for(String param : params.keySet()) {
                sb.append(sb.length() == 0 && !url.contains("?") ? "?" : "&");
                sb.append(param).append('=').append(params.get(param));
            }
            return url + sb.toString();
        }
    }
}
