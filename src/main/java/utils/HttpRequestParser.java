package utils;

import java.util.Optional;

public class HttpRequestParser {
    public static Optional<String[]> parseRequestLine(String requestLine) {
        String[] parts = requestLine.split(" ");
        if (parts.length != 3) {
            return Optional.empty();
        }
        for(int i = 0 ; i < parts.length ; i++) {
            parts[i] = parts[i].trim();
        }
        return Optional.of(parts);
    }

    public static Optional<String[]> parseRequestHeader(String requestHeader) {
        String[] headerParts = requestHeader.split(":",2);
        if (headerParts.length != 2) {
            return Optional.empty();
        }
        String key = headerParts[0].trim();
        String value = headerParts[1].trim();
        return Optional.of(new String[] { key, value });
    }
}
