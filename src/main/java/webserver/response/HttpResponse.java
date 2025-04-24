package webserver.response;

import java.util.HashMap;
import java.util.Map;

public class HttpResponse {
    private final Status status;
    private final String httpversion;
    private final Map<String, String> headers;
    private final byte[] body;

    private final String SPACE = " ";
    private final String CRLF = "\r\n";
    private final String COLON = ":";


    private HttpResponse(Builder builder) {
        this.status = builder.status;
        this.httpversion = builder.httpVersion;
        this.headers = builder.headers;
        this.body = builder.body;
    }

    public Map<String, String> getMapHeaders() {
        return headers;
    }

    public String getHeader() {
        StringBuilder header = new StringBuilder();
        header.append(httpversion).append(SPACE).append(status.asHttpLine()).append(CRLF);
        for(Map.Entry<String, String> entry : headers.entrySet()) {
            header.append(entry.getKey()).append(COLON).append(entry.getValue()).append(CRLF);
        }
        header.append(CRLF);
        return header.toString();
    }

    public byte[] getBody() {
        return body;
    }

    public Status getStatus() {
        return status;
    }

    public static Builder getBuilder() {
        return new Builder();
    }

    public static class Builder {
        private String httpVersion;
        private Status status;
        private Map<String, String> headers = new HashMap<>();
        private byte[] body;

        public Builder httpVersion(String httpVersion) {
            this.httpVersion = httpVersion;
            return this;
        }

        public Builder status(Status status) {
            this.status = status;
            return this;
        }

        public Builder header(String key, String value) {
            this.headers.put(key, value);
            return this;
        }

        public Builder body(byte[] body) {
            this.body = body;
            return this;
        }

        public HttpResponse build() {
            if (httpVersion == null || httpVersion.isBlank()) {
                throw new IllegalStateException("Invalid http version");
            }
            if (status == null) {
                throw new IllegalStateException("Invalid status");
            }
            if (body == null){
                body = new byte[0];
            }
            return new HttpResponse(this);
        }
    }
}
