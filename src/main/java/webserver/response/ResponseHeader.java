package webserver.response;

import static webserver.common.Constants.BLANK;
import static webserver.common.Constants.EMPTY;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ResponseHeader {
    private static final String CRLF = "\r\n";
    private static final String CONTENT_TYPE = "Content-Type: ";
    private static final String CONTENT_LENGTH = "Content-Length: ";
    private static final String LOCATION = "Location: ";
    private static final String SET_COOKIE = "Set-Cookie: ";
    private String requestLine;
    private String contentType;
    private String contentLength;
    private String location;
    private List<String> cookies;

    public ResponseHeader(Builder builder) {
        this.requestLine = builder.requestLine;
        this.contentType = builder.contentType;
        this.contentLength = builder.contentLength;
        this.location = builder.location;
        this.cookies = builder.cookies;
    }

    @Override
    public String toString() {
        return Stream.concat(
                        Stream.of(requestLine, contentType, contentLength, location),
                        (cookies != null ? cookies : List.<String>of()).stream()
                                .map(cookie -> SET_COOKIE + cookie)
                )
                .filter(Objects::nonNull)
                .collect(Collectors.joining(CRLF, EMPTY, CRLF + CRLF));
    }

    public static class Builder{
        private String version;
        private String status;
        private String statusMessage;
        private String requestLine;
        private String contentType;
        private String contentLength;
        private String location;
        private List<String> cookies;

        public Builder version(String version) {
            this.version = version;
            return this;
        }
        public Builder status(String status) {
            this.status = status;
            return this;
        }
        public Builder statusMessage(String statusMessage) {
            this.statusMessage = statusMessage;
            return this;
        }
        public Builder contentType(String contentType) {
            this.contentType = CONTENT_TYPE + contentType;
            return this;
        }
        public Builder contentLength(String contentLength) {
            this.contentLength = CONTENT_LENGTH + contentLength;
            return this;
        }
        public Builder location(String location) {
            this.location = LOCATION + location;
            return this;
        }

        public Builder cookie(List<String> cookies) {
            this.cookies = cookies;
            return this;
        }

        public ResponseHeader build() {
            this.requestLine = version + BLANK + status + BLANK + statusMessage;
            return new ResponseHeader(this);
        }
    }
}
