package webserver.request;

import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class RequestParser {
    private static final Logger logger = LoggerFactory.getLogger(RequestParser.class);
    private static final int METHOD_INDEX = 0;
    private static final int PATH_INDEX = 1;
    private static final int VERSION_INDEX = 2;
    private static final int NAME_INDEX = 0;
    private static final int VALUE_INDEX = 1;

    public static HttpRequest parseRequest(InputStream in) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(in));

        String requestLine = br.readLine();
        logger.debug("request line : {}", requestLine);
        String[] requestParts = requestLine.split(" ");
        String method = requestParts[METHOD_INDEX];
        String path = requestParts[PATH_INDEX];
        String version = requestParts[VERSION_INDEX];

        Map<String, String> headers = new HashMap<>();
        String line;
        while (!(line = br.readLine()).isEmpty()) {
            logger.debug("header line : {}", line);
            String[] headerParts = line.split(": ", 2);
            headers.put(headerParts[NAME_INDEX], headerParts[VALUE_INDEX]);
        }

        String body = null;
        return new HttpRequest(method, path, version, headers, body);
    }
}
