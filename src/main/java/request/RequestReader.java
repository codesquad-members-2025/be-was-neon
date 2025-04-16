package request;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.HttpRequestParser;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

import static constants.HttpHeaders.CONTENT_LENGTH;
import static constants.SpecialChars.EMPTY;

public class RequestReader {
    private static final Logger logger = LoggerFactory.getLogger(RequestReader.class);
    private final BufferedReader bufferedReader;

    public RequestReader(InputStream inputStream) throws IOException {
        bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
    }

    public Request readRequest() throws IOException {
        RequestHeader requestHeader = readHeaders();
        String requestBody = readBody(requestHeader);
        return new Request(requestHeader, requestBody);
    }


    private RequestHeader readHeaders() throws IOException {
        String requestLine = bufferedReader.readLine();
        logger.debug("Request Line: {}", requestLine);
        String[] parsedRequestLine = HttpRequestParser.parseRequestLine(requestLine).orElseThrow(IOException::new);

        Map<String, String> headers = new HashMap<>();
        String line;
        while ((line = bufferedReader.readLine()) != null && !line.isEmpty()) {
            logger.debug("HTTPRequest : {}", line);
            String[] headerParts = HttpRequestParser.parseRequestHeader(line).orElseThrow(IOException::new);
            headers.put(headerParts[0], headerParts[1]);
        }
        return new RequestHeader(parsedRequestLine[0], parsedRequestLine[1], parsedRequestLine[2], headers);
    }

    private String readBody(RequestHeader requestHeader) throws IOException {
        if (!requestHeader.containsHeader(CONTENT_LENGTH)) {
            return EMPTY;
        }
        int contentLength = Integer.parseInt(requestHeader.getHeaderByKey(CONTENT_LENGTH));
        char[] bodyChars = new char[contentLength];
        int totalRead = 0;
        while (totalRead < contentLength) {
            int read = bufferedReader.read(bodyChars, totalRead, contentLength - totalRead);
            if (read == -1) break; // EOF
            totalRead += read;
        }
        return new String(bodyChars, 0, totalRead);
    }
}
