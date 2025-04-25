package webserver.http.request;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.http.request.param.BodyParams;
import webserver.http.request.param.HeaderParams;
import webserver.http.request.param.QueryParams;
import webserver.http.request.param.RequestLineParams;

import java.util.Map;

public class Request {
    private static final Logger logger = LoggerFactory.getLogger(Request.class);

    private RequestLineParams requestLine;
    private HeaderParams headers;
    private BodyParams body;
    private QueryParams query;

    public Request(RequestLineParams requestLine, HeaderParams headers, BodyParams body, QueryParams query) {
        this.requestLine = requestLine;
        this.headers = headers;
        this.body = body;
        this.query = query;
        print();
    }

    public String getRequestLine(String key) {
        return requestLine.get(key);
    }

    public BodyParams getBody() {
        return body;
    }

    private void print() {
        logger.debug("================Client Request==================");
        logger.debug("{}", requestLine);
        for (Map.Entry<String, String> entry : headers.entrySet()) {
            logger.debug("{}: {}", entry.getKey(), entry.getValue());
        }
        logger.debug("================================================");
    }
}
