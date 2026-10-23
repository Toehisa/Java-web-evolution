package com.evolution.web.first;

import com.evolution.web.ServerConfiguration;

import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class RestValidator {
    private final Path serverDir;

    public RestValidator(ServerConfiguration config) {
        serverDir = config.baseDir();
    }
    private static boolean isRestMethod(String method) {
        return switch (method) {
            case "GET", "DELETE", "POST", "PUT", "PATCH" -> true;
            default -> false;
        };
    }

    private boolean isValidPath(Path fullPath) throws IOException {
        return fullPath.startsWith(serverDir) && !Files.isDirectory(fullPath);
    }

    private boolean isValidQuery(String query) {
        if (query == null || query.isBlank()) return true;
        if (query.length() > 1024) return false;

        try {
            URLDecoder.decode(query, StandardCharsets.UTF_8);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    private boolean isValidProtocol(String protocol) {
        return "HTTP/1.1".equals(protocol) || "HTTP/1.0".equals(protocol);
    }

    public boolean validateRequest(HttpServletRequest request, Path fullPath) throws IOException {
        String method = request.getMethod();
        String protocol = request.getProtocol();
        String query = request.getQuery();

        return isRestMethod(method)
                && isValidPath(fullPath)
                && isValidQuery(query)
                && isValidProtocol(protocol);
    }
}
