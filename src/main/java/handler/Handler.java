package handler;

import request.Request;
import response.HttpResponseWriter;

import java.io.DataOutputStream;
import java.io.IOException;

public interface Handler {

    public void handle(Request request, HttpResponseWriter responseWriter) throws IOException;

}
