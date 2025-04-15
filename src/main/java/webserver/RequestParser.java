package webserver;

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
    private final Logger logger = LoggerFactory.getLogger(RequestParser.class);
    private final String USER_ID = "userId";
    private final String PASSWORD = "password";
    private final String NAME = "name";
    private final String EMAIL = "email";


    public String[] generateRequestLine(InputStream in) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        String line = br.readLine();
        logger.debug("request line : {}", line);
        String[] requestLine = line.split(" ");

        while ((line = br.readLine()) != null && !line.equals("")) {
            logger.debug("header line : {}", line);
        }
        return requestLine;
    }

    public User parseRegistrationData(String urlPath) {
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
