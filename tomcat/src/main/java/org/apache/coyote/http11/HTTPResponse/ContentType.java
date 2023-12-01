package org.apache.coyote.http11.HTTPResponse;

public enum ContentType {
    HTML("text/html"),
    CSS("text/css"),
    JS("/js/scripts.js")
    ;

    private final String value;

    ContentType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
