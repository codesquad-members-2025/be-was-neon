package utils;

public class HttpRequestParser {
    public static String parseUrl(String header) {
        String[] tokens  = header.split(" ");
        return tokens[1];
    }
}
