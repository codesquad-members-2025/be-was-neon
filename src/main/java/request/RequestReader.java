package request;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.HttpRequestParser;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class RequestReader {
    private static final Logger logger = LoggerFactory.getLogger(RequestReader.class);
    private final InputStream inputStream;
    private final BufferedReader bufferedReader;

    public RequestReader(InputStream inputStream) throws IOException {
        this.inputStream = inputStream;
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
        if (!requestHeader.containsHeader("Content-Length")) {
            return "";
        }
        int contentLength = Integer.parseInt(requestHeader.getHeaderByKey("Content-Length"));
        byte[] buffer = new byte[contentLength];
        int totalRead = 0;
        while (totalRead < contentLength) {
            int read = inputStream.read(buffer, totalRead, contentLength - totalRead);
            if (read == -1) break; // EOF
            totalRead += read;
        }
        return new String(buffer, 0, totalRead, StandardCharsets.UTF_8);
    }
}
