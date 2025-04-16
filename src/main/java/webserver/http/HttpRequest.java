package webserver.http;

public class HttpRequest {
    private final String METHOD;
    private final String URL_PATH;
    private final int METHOD_INDEX = 0;
    private final int URL_INDEX = 1;

    public HttpRequest(String[] requestLine) {
        this.METHOD = requestLine[METHOD_INDEX];
        this.URL_PATH = requestLine[URL_INDEX];
    }

    public String getMETHOD() {
        return METHOD;
    }

    public String getURL_PATH() {
        return URL_PATH;
    }

    public String getPathWithoutQuery() {
        return getURL_PATH().split("\\?")[0];
    }
}
