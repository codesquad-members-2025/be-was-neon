package webserver.request;

import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.http.HttpRequest;

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

    private static final String USER_ID = "userId";
    private static final String PASSWORD = "password";
    private static final String NAME = "name";
    private static final String EMAIL = "email";

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

    public static User parseRegistrationData(String urlPath) {
        int index = urlPath.indexOf("?");
        if (index == -1) return null;

        String query = urlPath.substring(index + 1);
        Map<String, String> param = new HashMap<>();

        for (String pair : query.split("&")) {
            String[] kv = pair.split("=", 2);   // "="로 딱 한번만 분리한다
            String key = URLDecoder.decode(kv[0], StandardCharsets.UTF_8);
            String value = kv.length > 1 ? URLDecoder.decode(kv[1], StandardCharsets.UTF_8) : "";
            param.put(key, value);
        }
        return new User(param.get(USER_ID), param.get(PASSWORD), param.get(NAME), param.get(EMAIL));
    }
}
