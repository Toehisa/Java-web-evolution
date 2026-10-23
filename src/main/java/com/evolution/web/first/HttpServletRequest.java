package com.evolution.web.first;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.util.Objects;

public class HttpServletRequest {
    private final String method;
    private final String uri;
    private final String protocol;

    private final String cleanPath;
    private final String query;

    private final UriComponents components;

    HttpServletRequest(InputStream stream) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));

        String request = reader.readLine();
        isNotEmptyRequest(request);

        String[] tokens = request.split(" ");
        isValidTokens(tokens);

        method = restExtractMethod(tokens);
        uri = restExtractPath(tokens);
        protocol = restExtractProtocol(tokens);

        components = new UriComponents(uri);

        cleanPath = restExtractCleanPath(components.path());
        query = components.query();
    }

    public String getMethod() {
        return method;
    }

    public String getUri() {
        return uri;
    }

    public String getProtocol() {
        return protocol;
    }

    public String getCleanPath() {return cleanPath;}

    public String getQuery() {
        return query;
    }

    private String restExtractMethod(String[] tokens) {
        return tokens[0];
    }

    private String restExtractPath(String[] tokens) {
        return tokens[1];
    }

    private String restExtractProtocol(String[] tokens) {
        return tokens[2];
    }

    private void isNotEmptyRequest(String request) throws IOException {
        if (Objects.isNull(request) || request.isBlank()) throw new IOException("request is empty");
    }

    private void isValidTokens(String[] tokens) throws IOException {
        if (tokens.length != 3) throw new IOException("it is not rest request, strange lines count");
    }

    public String restExtractCleanPath(String path){
        return path.startsWith("/") ? path.substring(1) : path;
    }

    public static Path restExtractFullPath(String cleanPath, Path serverDir) throws IOException {
        return serverDir.resolve(cleanPath).toRealPath();
    }
}
