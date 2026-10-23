package com.evolution.web.first;

import com.evolution.web.ServerConfiguration;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Path;
import java.util.Objects;

class RawSocketServer {
    public static void main(String[] args) {
        ServerConfiguration config = serverConfiguration();
        int port = Objects.requireNonNull(config).port();

        try(ServerSocket serverSocket = new ServerSocket(port)) {

            RestValidator validator = new RestValidator(config);

            while (!serverSocket.isClosed()) {
                try (Socket socket = serverSocket.accept()) {
                    HttpServletRequest request = new HttpServletRequest(socket.getInputStream());
                    HttpServletResponse response = new HttpServletResponse(socket.getOutputStream());

                    Path filePath = HttpServletRequest.restExtractFullPath(request.getCleanPath(), config.baseDir());

                    if (validator.validateRequest(request, filePath)) {
                        response.sendFile(filePath);
                        System.out.println("Успешно отдали файл: " + filePath);
                    } else {
                        response.sendError(400, "Bad Request");
                        System.out.println("Запрос заблокирован валидатором.");
                    }
                }
            }
        } catch (java.nio.file.NoSuchFileException e) {
            System.out.println("Файл не найден (404): " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Ошибка обработки запроса: " + e.getMessage());
        }
    }

    private static ServerConfiguration serverConfiguration() {
        try {
            return ServerConfiguration.builder()
                    .port(22222)
                    .baseDir(Path.of("public").toAbsolutePath().toRealPath())
                    .build();
        } catch (IOException e) {
            System.out.println("path is not valid");
        }
        return null;
    }
}