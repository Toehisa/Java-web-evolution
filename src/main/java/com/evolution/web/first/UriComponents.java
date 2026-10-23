package com.evolution.web.first;

public final class UriComponents {
    private final String path;
    private final String query;

    public UriComponents(String uri) {
        int splitIdx = uri.indexOf('?');

        if (splitIdx == -1) {
            path = uri;
            query = "";
        } else {
            path = uri.substring(0, splitIdx);
            query = uri.substring(splitIdx + 1);
        }
    }

    public String path() {
        return path;
    }

    public String query() {
        return query;
    }
}
