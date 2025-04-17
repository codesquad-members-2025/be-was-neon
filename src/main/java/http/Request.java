package http;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class Request {
    private static final Logger logger = LoggerFactory.getLogger(Request.class);

    private Map<String, String> requestLine;
    private Map<String, String> headers;

    public Request(Map<String, String> requestLine, Map<String, String> headers) {
        this.requestLine = requestLine;
        this.headers = headers;
    }

    public String getHeader(String headerName) {
        return headers.get(headerName);
    }

    public String getRequestLine(String key) {
        return requestLine.get(key);
    }

    public void print() {
        logger.debug("================Client Request==================");
        logger.debug("{}", requestLine);
        for (Map.Entry<String, String> entry : headers.entrySet()) {
            logger.debug("{}: {}", entry.getKey(), entry.getValue());
        }
        logger.debug("================================================");
    }
}
