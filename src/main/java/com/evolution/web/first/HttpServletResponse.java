package com.evolution.web.first;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;

public class HttpServletResponse {
    private final OutputStream out;

    public HttpServletResponse(OutputStream out) {
        this.out = out;
    }

    public void sendFile(Path filePath) throws IOException {
        // 1. Пытаемся угадать тип файла (вернет "text/html", "image/png" и т.д.)
        String contentType = Files.probeContentType(filePath);

        // Если Java не знает такого файла, отдаем как бинарник (браузер предложит его скачать)
        if (contentType == null) {
            contentType = "application/octet-stream";
        }

        // 2. Формируем заголовки. Каждый заголовок заканчивается на \r\n
        // Пустая строка \r\n\r\n в конце отделяет заголовки от тела файла
        String headers = "HTTP/1.1 200 OK\r\n" +
                "Content-Type: " + contentType + "\r\n" +
                "Connection: close\r\n" + // Говорим браузеру, что не поддерживаем keep-alive
                "\r\n";

        // 3. Отправляем заголовки и байты файла
        out.write(headers.getBytes());
        Files.copy(filePath, out);
        out.flush();
    }

    public void sendError(int statusCode, String statusText) throws IOException {
        String response = "HTTP/1.1 " + statusCode + " " + statusText + "\r\n" +
                "Connection: close\r\n" +
                "\r\n";
        out.write(response.getBytes());
        out.flush();
    }
}