package webserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RequestParser {
    private static final Logger logger = LoggerFactory.getLogger(RequestParser.class);

    public String[] parseRequest(InputStream in) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        String line = br.readLine();
        String[] requestLine = line.split(" ");
        logger.debug("requestLine : {}", line);

        while (!line.isEmpty()) {
            line = br.readLine();
            logger.debug("header : {}", line);
        }
        return requestLine;
    }
}
