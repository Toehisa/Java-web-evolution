package com.evolution.web;

import java.nio.file.Path;

public record ServerConfiguration(Path baseDir, int port) {
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Path baseDir;
        private int port = -1;

        private Builder() {}

        public Builder baseDir(Path baseDir) {
            this.baseDir = baseDir;
            return this;
        }

        public Builder port(int port) {
            this.port = port;
            return this;
        }

        public ServerConfiguration build() {
            if (port == -1) throw new IllegalArgumentException("Port is not configured");
            if (baseDir == null) throw new IllegalArgumentException("baseDir is not configured");
            return new ServerConfiguration(baseDir, port);
        }
    }
}
