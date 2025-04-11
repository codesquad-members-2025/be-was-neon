package request;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.HttpRequestParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class RequestHeaderReader {
    private static final Logger logger = LoggerFactory.getLogger(RequestHeaderReader.class);

    public static RequestHeader readHeaders(InputStream in) throws IOException {

        BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF-8"));

        String requestLine = br.readLine();
        logger.debug("Request Line: {}", requestLine);
        String[] parsedRequestLine = HttpRequestParser.parseRequestLine(requestLine).orElseThrow(IOException::new);

        Map<String,String> headers = new HashMap<>();
        String line;
        while ((line = br.readLine()) != null && !line.isEmpty()) {
            logger.debug("HTTPRequest : {}", line);
            String[] headerParts = HttpRequestParser.parseRequestHeader(line).orElseThrow(IOException::new);
            headers.put(headerParts[0], headerParts[1]);
        }
        return new RequestHeader(parsedRequestLine[0], parsedRequestLine[1], parsedRequestLine[2],headers);
    }
}
