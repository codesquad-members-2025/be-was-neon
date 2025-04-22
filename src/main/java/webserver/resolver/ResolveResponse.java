package webserver.resolver;

import webserver.http.common.ContentType;
import webserver.http.common.HttpHeaders;
import webserver.http.response.HttpStatusCode;
import webserver.model.Model;
import webserver.view.ModelAndView;

import static webserver.http.common.ContentType.HTML;
import static webserver.http.common.ContentType.TEXT;
import static webserver.http.response.HttpStatusCode.*;

public class ResolveResponse<T> {

    private final HttpStatusCode statusCode;
    private final HttpHeaders headers;
    private final T body;
    private final ModelAndView mav;

    private ResolveResponse(HttpStatusCode statusCode, HttpHeaders headers, T body) {
        this.statusCode = statusCode;
        this.headers = headers;
        this.body = body;
        this.mav = null;
    }

    private ResolveResponse(HttpStatusCode statusCode, HttpHeaders headers, T body, ModelAndView mav) {
        this.statusCode = statusCode;
        this.headers = headers;
        this.body = body;
        this.mav = mav;
    }

    public HttpStatusCode getStatusCode() {
        return statusCode;
    }

    public HttpHeaders getHeaders() {
        return headers;
    }

    public T getBody() {
        return body;
    }

    public ModelAndView getModelAndView() {
        return mav;
    }

    public boolean isView() {
        return mav != null;
    }

    public static <T> ResolveResponse<T> ok(T body) {
        HttpHeaders headers = new HttpHeaders();
        headers.addContentType(HTML);

        return new ResolveResponse<>(OK, headers, body);
    }

    public static <T> ResolveResponse<T> ok(ContentType contentType, T body) {
        HttpHeaders headers = new HttpHeaders();
        headers.addContentType(contentType);

        return new ResolveResponse<>(OK, headers, body);
    }

    public static <T> ResolveResponse<T> status(HttpStatusCode statusCode, T body) {
        HttpHeaders headers = new HttpHeaders();
        headers.addContentType(TEXT);

        return new ResolveResponse<>(statusCode, headers, body);
    }

    public static ResolveResponse<String> status(HttpStatusCode statusCode) {
        HttpHeaders headers = new HttpHeaders();
        headers.addContentType(TEXT);

        return new ResolveResponse<>(statusCode, headers, statusCode.getReasonPhrase());
    }

    public static ResolveResponse<String> redirect(String location) {
        HttpHeaders headers = new HttpHeaders();
        headers.addLocation(location);

        return new ResolveResponse<>(FOUND, headers, null);
    }

    public static ResolveResponse<Void> view(String viewName, Model model) {
        HttpHeaders headers = new HttpHeaders();
        headers.addContentType(HTML);

        ModelAndView mav = new ModelAndView(viewName, model);
        return new ResolveResponse<>(OK, headers, null, mav);
    }

    public static ResolveResponse<Void> view(ModelAndView mav) {
        HttpHeaders headers = new HttpHeaders();
        headers.addContentType(HTML);

        return new ResolveResponse<>(OK, headers, null, mav);
    }

}
