package webserver.servlet;

import webserver.http.HttpRequest;
import webserver.http.HttpResponse;

public interface HttpServlet {
    void service(HttpRequest request, HttpResponse response);
}