package webserver.http.request;

import webserver.http.common.HttpHeaders;
import webserver.http.exception.RequestParseException;
import webserver.util.Convertor;

import java.io.BufferedReader;
import java.io.IOException;

import static webserver.http.common.HttpConstants.*;

public class RequestParser {

    private static final char CR = '\r';
    private static final char LF = '\n';
    private static final int BUFFER_SIZE = 1024;
    private static final String CONTENT_LENGTH = "Content-Length";
    private static final String TRANSFER_ENCODING = "Transfer-Encoding";
    private static final String CHUNKED = "chunked";

    private final BufferedReader reader;

    public RequestParser(BufferedReader reader) {
        this.reader = reader;
    }

    /**
     * CRLF (CR+LF)를 엄격하게 확인하면서 한 줄씩 읽습니다.
     *
     * @throws IOException CRLF가 아닌 경우 예외를 발생
     */
    private String readLineStrict() throws IOException {
        StringBuilder sb = new StringBuilder();
        int c;
        boolean seenCR = false;
        while ((c = reader.read()) != -1) {
            if (c == CR) {
                seenCR = true;
            } else if (c == LF) {
                if (!seenCR) {
                    throw new RequestParseException("Invalid request line: " + sb);
                }
                return sb.toString();
            } else {
                if (seenCR) {
                    // CR 이후에 LF가 오지 않은 경우
                    throw new RequestParseException("Invalid request line: " + sb);
                }
                sb.append((char) c);
            }
        }
        if (sb.isEmpty()) {
            return null;
        }
        throw new RequestParseException("Invalid request line: " + sb);
    }

    /**
     * HTTP 요청 전체를 파싱하여 HTTPRequest 객체를 반환합니다.
     */
    public HttpRequest parseRequest() throws IOException {
        // 1. Request-Line 파싱 (CRLF 확인)
        String requestLineStr = readLineStrict();
        if (requestLineStr == null || requestLineStr.isEmpty()) {
            throw new RequestParseException("Request line is empty");
        }
        RequestLine requestLine = parseRequestLine(requestLineStr);

        // 2. 헤더 파싱 (빈 줄(CRLF만 있는 줄)이 나오기 전까지)
        HttpHeaders headers = new HttpHeaders();
        String line;
        while ((line = readLineStrict()) != null) {
            if (line.isEmpty()) {
                // 빈 줄: 헤더 영역의 종료를 의미 (CRLF만 존재)
                break;
            }
            parseHeaderLine(line, headers);
        }

        String body = EMPTY;
        if (headers.containsKey(TRANSFER_ENCODING) &&
                headers.get(TRANSFER_ENCODING).trim().equalsIgnoreCase(CHUNKED)) {
            // Chunked 인코딩일 경우
            body = parseChunkedBody();
        } else if (headers.containsKey(CONTENT_LENGTH)) {
            body = parseBody(headers.get(CONTENT_LENGTH));
        }

        return new HttpRequest(requestLine, headers, body);
    }

    private RequestLine parseRequestLine(String line) {
        String[] parts = line.split(SPACE, 3);
        if (parts.length != 3 || !parts[2].startsWith("HTTP/")) {
            throw new RequestParseException("Invalid Request-Line: " + line);
        }

        return new RequestLine(line);
    }

    private void parseHeaderLine(String line, HttpHeaders headers) {
        int colonIndex = line.indexOf(COLON);
        if (colonIndex == -1) {
            throw new RequestParseException("Invalid header field: " + line);
        }

        String fieldName = line.substring(0, colonIndex).strip();
        String fieldValue = line.substring(colonIndex + 1).strip();

        headers.add(fieldName, fieldValue);
    }

    private String parseBody(String contentLengthStr) throws IOException {
        long contentLength = Convertor.convertToLong(contentLengthStr);
        StringBuilder bodyBuilder = new StringBuilder();
        char[] buffer = new char[BUFFER_SIZE];
        long remaining = contentLength;

        while (remaining > 0) {
            int toRead = (int) Math.min(buffer.length, remaining);
            int read = reader.read(buffer, 0, toRead);
            if (read == -1) {
                throw new IOException("Expected " + contentLength + " bytes, but stream ended early");
            }
            bodyBuilder.append(buffer, 0, read);
            remaining -= read;
        }

        return bodyBuilder.toString();
    }

    /**
     * chunk          = chunk-size [ chunk-ext ] CRLF
     * chunk-data CRLF
     * chunk-size     = 1*HEX
     */
    private String parseChunkedBody() throws IOException {
        StringBuilder bodyBuilder = new StringBuilder();
        while (true) {
            // 각 청크의 시작은 청크 사이즈를 나타내는 라인입니다.
            String chunkSizeLine = readLineStrict();
            if (chunkSizeLine == null) {
                throw new RequestParseException("Unexpected end of stream while reading chunk size");
            }
            // 청크 사이즈 라인에 확장자(Chunk Extensions)가 있을 수 있으므로 ';' 앞까지 읽습니다.
            int semicolonIndex = chunkSizeLine.indexOf(';');
            String hexSizeStr = (semicolonIndex != -1) ? chunkSizeLine.substring(0, semicolonIndex) : chunkSizeLine;
            int chunkSize = Convertor.convertToIntByHex(hexSizeStr);
            if (chunkSize == 0) {
                // trailer 헤더를 무시하고, 빈 줄(CRLF)만 확인
                String trailerLine = readLineStrict();
                if (trailerLine == null) {
                    throw new RequestParseException("Expected CRLF after last chunk");
                }

                break;
            }
            // 청크 데이터 읽기
            char[] chunkData = new char[chunkSize];
            int totalRead = 0;
            while (totalRead < chunkSize) {
                int read = reader.read(chunkData, totalRead, chunkSize - totalRead);
                if (read == -1) {
                    throw new IOException("Unexpected end of stream while reading chunk data");
                }
                totalRead += read;
            }
            bodyBuilder.append(chunkData);
            // 청크 데이터 뒤에는 반드시 CRLF
            String crlf = readLineStrict();
            if (crlf == null || !crlf.isEmpty()) {
                throw new RequestParseException("Expected CRLF after chunk data");
            }
        }
        return bodyBuilder.toString();
    }

}
