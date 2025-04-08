package webserver.http;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.http.exception.RequestParseException;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class HttpRequest {

    private static final Logger logger = LoggerFactory.getLogger(HttpRequest.class);
    private final String method;
    private final String uri;
    private final String protocol;
    private final Map<String, String> headers;

    public HttpRequest(BufferedReader reader) throws IOException {
        headers = new HashMap<>();

        String requestLine = reader.readLine();
        if (requestLine == null || requestLine.isEmpty()) {
            throw new RequestParseException("Empty request line");
        }
        logger.debug("Request line: {}", requestLine);

        String[] tokens = requestLine.split(" ");
        if (tokens.length < 3) {
            throw new RequestParseException("Invalid request line: " + requestLine);
        }

        this.method = tokens[0];
        this.uri = tokens[1];
        this.protocol = tokens[2];

        String line;
        while ((line = reader.readLine()) != null && !line.isEmpty()) {
            logger.debug("Header line: {}", line);
            int colonIndex = line.indexOf(":");
            if (colonIndex != -1) {
                String key = line.substring(0, colonIndex).trim();
                String value = line.substring(colonIndex + 1).trim();
                headers.put(key, value);
            }
        }
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

    public Map<String, String> getHeaders() {
        return headers;
    }

}
