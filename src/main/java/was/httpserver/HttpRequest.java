package was.httpserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

import static java.nio.charset.StandardCharsets.UTF_8;
import static util.MyLog.log;

public class HttpRequest {
    private String method;
    private String path;
    private final Map<String, String> queryParameters = new HashMap<>();
    private final Map<String, String> headers = new HashMap<>();

    public HttpRequest(BufferedReader reader) throws IOException {
        parseRequestLine(reader);
        parseHeaders(reader);
        parseBody(reader);
    }

    //GET /create?id=a&pw=123 HTTP/1.1
    private void parseRequestLine(BufferedReader reader) throws IOException {
        String requestLine = reader.readLine();
        if (requestLine ==  null) {
            throw new IOException("EOF: 요청이 안날라왔어용..");
        }

        String[] parts = requestLine.split(" ");
        method = parts[0];
        String[] pathParts = parts[1].split("\\?");
        path = pathParts[0];

        if (pathParts.length > 1) {
            parseQueryParameters(pathParts[1]);
        }
    }

    private void parseQueryParameters(String pathPart) {
        for (String queryParam : pathPart.split("&")) {
            String[] keyValue = queryParam.split("=");
            String key = URLDecoder.decode(keyValue[0], UTF_8);
            String value = keyValue.length > 1 ? URLDecoder.decode(keyValue[1], UTF_8) : "";
            queryParameters.put(key, value);
        }
    }


    // Host: localhost:8080
    // Connection: keep-alive
    private void parseHeaders(BufferedReader reader) throws IOException {
        String line;
        while (!(line = reader.readLine()).isEmpty()){
            String[] parts = line.split(":");
            headers.put(parts[0].trim(), parts[1].trim());
        }
    }

    private void parseBody(BufferedReader reader) throws IOException {
        if (!headers.containsKey("Content-Length")) {
            return;
        }

        int contentLength = Integer.parseInt(headers.get("Content-Length"));
        char[] buffer = new char[contentLength];
        int read = reader.read(buffer);
        if (read != contentLength) {
            throw new IOException(contentLength+ "만큼 읽어야하는데 "+ read + "만큼 읽혔어용");
        }
        String body = new String(buffer);
        log("HTTP Message Body: " + body);
        String contentType = headers.get("Content-Type");
        if ("application/x-www-form-urlencoded".equals(contentType)) {
            parseQueryParameters(body);
        }
    }

    public String getMethod() {
        return method;
    }

    public String getPath() {
        return path;
    }

    public String getParameter(String name) {
        return queryParameters.get(name);
    }

    public String getHeader(String name) {
        return headers.get(name);
    }

    @Override
    public String toString() {
        return "HttpRequest{" +
                "headers=" + headers +
                ", method='" + method + '\'' +
                ", path='" + path + '\'' +
                ", queryParameters=" + queryParameters +
                '}';
    }
}
