package webserver.servlet;

import webserver.http.HttpRequest;
import webserver.http.HttpResponse;
import java.io.IOException;

public interface HttpServlet {
    void service(HttpRequest request, HttpResponse response) throws IOException;
}