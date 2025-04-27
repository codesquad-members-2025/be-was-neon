package utils;

import java.util.Optional;

import static constants.SpecialChars.*;

public class HttpRequestParser {
    public static Optional<String[]> parseRequestLine(String requestLine) {
        String[] parts = requestLine.split(SPACE);
        if (parts.length != 3) {
            return Optional.empty();
        }
        for(int i = 0 ; i < parts.length ; i++) {
            parts[i] = parts[i].trim();
        }
        return Optional.of(parts);
    }

    public static String[] parsePathAndQueryString(String path) {
        String[] parts = path.split("\\?");
        if (parts.length == 1) {
            return new String[] { parts[0], EMPTY };
        }
        return new String[] { parts[0], parts[1] };
    }

    public static Optional<String[]> parseRequestHeader(String requestHeader) {
        String[] headerParts = requestHeader.split(COLON,2);
        if (headerParts.length != 2) {
            return Optional.empty();
        }
        String key = headerParts[0].trim().toLowerCase();
        String value = headerParts[1].trim();
        return Optional.of(new String[] { key, value });
    }
}
