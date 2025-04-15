package webserver;

import webserver.util.RequestParser;

import java.io.BufferedReader;
import java.io.IOException;

public class HttpRequest {
    private String path;
    private String requestLine;
    private String headers;

    public HttpRequest(BufferedReader br) throws IOException {
        this.requestLine = br.readLine();
        if  (this.requestLine == null || this.requestLine.isEmpty()) {
            throw new IOException("Invalid request line"); //이 에러를 여기서 던지는게 맞는지
        }
        String[] tokens = this.requestLine.split(" ");
        this.path = tokens[1];
        this.headers = RequestParser.extractFilteredHeaders(br);

    }

    public String getPath() {
        return path;
    }

    public String getRequestLine() {
        return requestLine;
    }

    public String getHeaders() {
        return headers;
    }
}
