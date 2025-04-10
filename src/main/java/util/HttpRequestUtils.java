package util;

public class HttpRequestUtils {

    public static String extractRequestPath(String requestLine) {
        String[] requestLineArr = requestLine.split(" ");
        return requestLineArr[1];
    }
}
