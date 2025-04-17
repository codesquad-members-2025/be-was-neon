package request;

public class Request {
    RequestHeader requestHeader;
    String requestBody;

    public Request(RequestHeader requestHeader, String requestBody) {
        this.requestHeader = requestHeader;
        this.requestBody = requestBody;
    }

    public RequestHeader getRequestHeader() {
        return requestHeader;
    }

    public String getRequestBody() {
        return requestBody;
    }
}
