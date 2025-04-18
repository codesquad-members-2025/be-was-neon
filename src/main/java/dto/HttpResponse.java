package dto;

import java.util.HashMap;
import java.util.Map;

public class HttpResponse {
    private int statusCode;
    private String statusText;
    private String contentType;
    private byte[] body;
    private Map<String, String> headers = new HashMap<>();
    private Map<String,String> cookies = new HashMap<>();

    public HttpResponse() {
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getStatusText() {
        return statusText;
    }

    public void setStatusText(String statusText) {
        this.statusText = statusText;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public byte[] getBody() {
        return body;
    }

    public void setBody(byte[] body) {
        this.body = body;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    public Map<String, String> getCookies() {
        return cookies;
    }

    public void setCookies(Map<String, String> cookies) {
        this.cookies = cookies;
    }

    public HttpResponse(int statusCode, String statusText, String contentType, byte[] body, Map<String, String> headers, Map<String, String> cookies) {
        this.statusCode = statusCode;
        this.statusText = statusText;
        this.contentType = contentType;
        this.body = body;
        this.headers = headers;
        this.cookies = cookies;
    }
}




