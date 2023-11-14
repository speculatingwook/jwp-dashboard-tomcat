package org.apache.coyote.response;

public enum ContentType {
    HTML("text/html"),
    CSS("text/css"),
    JS("application/javascript");

    private final String contentType;

    ContentType(String contentType) {
        this.contentType = contentType;
    }

    public static ContentType from(String path){
        if(path.contains("css")){
            return CSS;
        }
        if(path.contains("html")){
            return HTML;
        }
        if(path.contains("js")){
            return JS;
        }
        return null;
    }

    @Override
    public String toString() {
        return contentType;
    }
}
