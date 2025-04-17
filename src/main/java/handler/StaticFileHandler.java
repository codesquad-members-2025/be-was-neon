package handler;

import response.HttpResponseHandler;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class StaticFileHandler {

    public static void handle(String url, DataOutputStream dos) throws IOException {

        File file = new File("src/main/resources/static" + url);
        byte[] body = Files.readAllBytes(file.toPath());
        String extension = url.substring(url.lastIndexOf("."));

        HttpResponseHandler.response200Header(dos, extension, body.length);
        HttpResponseHandler.responseBody(dos, body);

    }



}
