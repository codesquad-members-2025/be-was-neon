package handler;

import request.Request;

import java.io.DataOutputStream;
import java.io.IOException;

public interface Handler {

    public void handle(Request request, DataOutputStream dos) throws IOException;

}
