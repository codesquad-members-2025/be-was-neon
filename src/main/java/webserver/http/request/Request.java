package webserver.http.request;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class Request {
    private static final Logger logger = LoggerFactory.getLogger(Request.class);

    private Map<String, String> requestLine;
    private Map<String, String> headers;
    private Map<String, String> queryMap;

    public Request(Map<String, String> requestLine, Map<String, String> headers, Map<String, String> queryMap) {
        this.requestLine = requestLine;
        this.headers = headers;
        this.queryMap = queryMap;
        print();
    }

    public String getRequestLine(String key) {
        return requestLine.get(key);
    }

    public Map<String, String> getQueryMap() { return queryMap; }

    private void print() {
        logger.debug("================Client Request==================");
        logger.debug("{}", requestLine);
        for (Map.Entry<String, String> entry : headers.entrySet()) {
            logger.debug("{}: {}", entry.getKey(), entry.getValue());
        }
        logger.debug("================================================");
    }
}
