package response;

import java.util.HashMap;
import java.util.Map;

import static constants.SpecialChars.COLON;
import static constants.SpecialChars.SPACE;

public class Response {
    private final String httpVersion;
    private final int statusCode;
    private final String statusMessage;
    private final Map<String, String> headers;
    private final byte[] body;

    private static final String CRLF = "\r\n";

    private Response(Builder builder) {
        this.httpVersion = builder.httpVersion;
        this.statusCode = builder.statusCode;
        this.statusMessage = builder.statusMessage;
        this.headers = builder.headers;
        this.body = builder.body;
    }

    public String getHeader(){
        StringBuilder response = new StringBuilder();
        response.append(httpVersion).append(SPACE).append(statusCode).append(SPACE).append(statusMessage).append(CRLF);
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
        private int statusCode;
        private String statusMessage;
        private Map<String, String> headers = new HashMap<>();
        private byte[] body;

        public Builder httpVersion(String httpVersion) {
            this.httpVersion = httpVersion;
            return this;
        }

        public Builder statusCode(int statusCode) {
            this.statusCode = statusCode;
            return this;
        }

        public Builder statusMessage(String statusMessage) {
            this.statusMessage = statusMessage;
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
            if (statusCode == 0) {
                throw new IllegalStateException("Status code must be set");
            }
            if (statusMessage == null || statusMessage.isBlank()) {
                throw new IllegalStateException("Status message must be set");
            }
            if (body == null){
                body = new byte[0];
            }
            return new Response(this);
        }
    }
}
