package org.apache.coyote.http11.HTTPRequest;

import java.util.*;
import java.util.stream.Collectors;

public class HttpRequestHeaders {
    private Map<String, String> header;

    private Map<String, String> cookies;

    private HttpRequestHeaders(Map<String, String> header) {
        this.header = header;
        this.cookies = parseCookie();
    }
    public static HttpRequestHeaders parse(List<String> lines) {
        return new HttpRequestHeaders(lines.stream()
                .map(line -> line.split(":", 2))
                .filter(split -> split.length == 2)
                .collect(Collectors.toMap(split -> split[0].trim(), split -> split[1].trim())));
    }

    private Map<String, String>  parseCookie() {
       return Optional.ofNullable(header.get("Cookie"))
                .map(cookieHeader -> Arrays.stream(cookieHeader.split(";"))
                        .map(part -> part.trim().split("=", 2))
                        .filter(keyValue -> keyValue.length == 2)
                        .collect(Collectors.toMap(
                                keyValue -> keyValue[0],
                                keyValue -> keyValue[1],
                                (existing, replacement) -> existing // 병합 로직: 기존 값 유지
                        )))
                .orElseGet(Collections::emptyMap);
    }

    public Map<String, String> getHeader() {
        return header;
    }
    public Map<String, String> getCookie() {
        return cookies;
    }


}
