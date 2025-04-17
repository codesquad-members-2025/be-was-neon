package response;

import java.util.HashMap;
import java.util.Map;

import static constants.SpecialChars.COLON;
import static constants.SpecialChars.SPACE;

public class Response {
    private final String httpVersion;
    private final Status status;
    private final Map<String, String> headers;
    private final byte[] body;

    private static final String CRLF = "\r\n";

    private Response(Builder builder) {
        this.httpVersion = builder.httpVersion;
        this.status = builder.status;
        this.headers = builder.headers;
        this.body = builder.body;
    }

    public String getHeader(){
        StringBuilder response = new StringBuilder();
        response.append(httpVersion).append(SPACE).append(status.getCode()).append(SPACE).append(status.getMessage()).append(CRLF);
        for (Map.Entry<String, String> entry : headers.entrySet()) {
            response.append(entry.getKey()).append(COLON).append(entry.getValue()).append(CRLF);
        }
        response.append(CRLF);
        return response.toString();
    }

    public byte[] getBody() {
        return body;
    }

    public static Builder builder() {
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

        public Response build() {
            if (httpVersion == null || httpVersion.isBlank()) {
                throw new IllegalStateException("HTTP version must be set");
            }
            if (status == null) {
                throw new IllegalStateException("Status must be set");
            }
            if (body == null){
                body = new byte[0];
            }
            return new Response(this);
        }
    }
}
