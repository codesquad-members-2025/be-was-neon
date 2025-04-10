package webserver.http.request;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class HttpRequest {

    private static final String DOT = ".";
    private static final String COLON = ":";

    private static final Logger logger = LoggerFactory.getLogger(HttpRequest.class);
    private final RequestLine requestLine;
    private final Map<String, String> headers;

    public HttpRequest(BufferedReader reader) throws IOException {
        headers = new HashMap<>();

        String line = reader.readLine();
        this.requestLine = new RequestLine(line);
        logger.debug("Request line: {}", requestLine);

        while ((line = reader.readLine()) != null && !line.isEmpty()) {
            logger.debug("Header line: {}", line);
            int colonIndex = line.indexOf(COLON);
            if (colonIndex != -1) {
                String key = line.substring(0, colonIndex).trim();
                String value = line.substring(colonIndex + 1).trim();
                headers.put(key, value);
            }
        }
    }

    public boolean isResource() {
        return requestLine.getPath().contains(DOT);
    }

    public RequestLine getRequestLine() {
        return requestLine;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

}
