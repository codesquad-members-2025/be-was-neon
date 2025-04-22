package request;

import Exceptions.HttpException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import response.Status;
import utils.HttpRequestParser;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static constants.HttpHeaders.CONTENT_LENGTH;
import static constants.SpecialChars.EMPTY;

public class RequestReader {
    private static final Logger logger = LoggerFactory.getLogger(RequestReader.class);
    private final InputStream inputStream;

    public RequestReader(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    public Request readRequest() {
        try {
            RequestHeader requestHeader = readHeaders();
            String requestBody = readBody(requestHeader);
            return new Request(requestHeader, requestBody);
        } catch (IOException e) {
            throw new HttpException(Status.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    private RequestHeader readHeaders() throws IOException {
        ByteArrayOutputStream lineBuffer = new ByteArrayOutputStream();
        List<String> lines = new ArrayList<>();

        int prev = -1, curr;
        while ((curr = inputStream.read()) != -1) {
            if (prev == '\r' && curr == '\n') {
                String line = lineBuffer.toString(StandardCharsets.UTF_8).trim();
                if (line.isEmpty()) {
                    break; // 헤더 종료 (\r\n\r\n)
                }
                lines.add(line);
                lineBuffer.reset();
            } else if (curr != '\r') {
                lineBuffer.write(curr);
            }
            prev = curr;
        }

        if (lines.isEmpty()) throw new HttpException(Status.BAD_REQUEST,"Empty request header");

        // Request line 처리
        String requestLine = lines.get(0);
        logger.debug("Request Line: {}", requestLine);
        String[] parsedRequestLine = HttpRequestParser.parseRequestLine(requestLine)
                .orElseThrow(() -> new HttpException(Status.BAD_REQUEST,"invalid request line: " + requestLine));

        // 나머지 헤더들 처리
        Map<String, String> headers = new HashMap<>();
        for (int i = 1; i < lines.size(); i++) {
            String line = lines.get(i);
            logger.debug("HTTPRequest: {}", line);
            String[] headerParts = HttpRequestParser.parseRequestHeader(line)
                    .orElseThrow(() -> new HttpException(Status.BAD_REQUEST,"invalid request header: " + line));
            headers.put(headerParts[0], headerParts[1]);
        }

        return new RequestHeader(parsedRequestLine[0], parsedRequestLine[1], parsedRequestLine[2], headers);
    }

    private String readBody(RequestHeader requestHeader) throws IOException {
        if (!requestHeader.containsHeader(CONTENT_LENGTH)) {
            return EMPTY;
        }

        int contentLength = Integer.parseInt(requestHeader.getHeaderByKey(CONTENT_LENGTH));
        byte[] bodyBytes = new byte[contentLength];
        int totalRead = 0;

        while (totalRead < contentLength) {
            int read = inputStream.read(bodyBytes, totalRead, contentLength - totalRead);
            if (read == -1) break;
            totalRead += read;
        }

        return new String(bodyBytes, 0, totalRead, StandardCharsets.UTF_8);
    }
}
