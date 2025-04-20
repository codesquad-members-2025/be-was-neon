package webserver.response;

import webserver.response.ResponseHeader.Builder;

public class ResponseHeaderFactory {
    private static final String HTTP_1_1 = "HTTP/1.1";
    public static ResponseHeader createHeader(String type, Response response) {

        Builder builder = new ResponseHeader.Builder()
                .version(HTTP_1_1)
                .status(response.getHttpStatus().getCode())
                .statusMessage(response.getHttpStatus().getMessage())
                .contentLength(String.valueOf(response.getBody().length));

        if (response.getHttpStatus().isRedirect()) {
            builder.location(response.getRedirectPath());
        } else {
            builder.contentType(type);
        }

        if(response.getCookie()!=null){
            builder.cookie(response.getCookie());
        }

        return builder.build();
    }
}
