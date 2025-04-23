package handler;

import httpconst.HttpConst;
import request.Request;
import response.HttpResponseWriter;

import java.io.IOException;

public class ErrorHandler implements Handler {

    @Override
    public void handle(Request request, HttpResponseWriter responseWriter) throws IOException {
        responseWriter.send405Error(HttpConst.METHOD_POST);
    }
}
