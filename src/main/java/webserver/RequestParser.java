package webserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class RequestParser {
    private static final Logger logger = LoggerFactory.getLogger(RequestParser.class);

    public String[] generateRequestLine(InputStream in) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        String line = br.readLine();
        String[] requestLine = line.split(" ");
        logger.debug("request line : {}", requestLine);

        while ((line = br.readLine()) != null && !line.equals("")) {
            logger.debug("header line : {}", line);
        }
        return requestLine;
    }
}
