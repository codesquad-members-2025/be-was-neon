package handler;

import httpconst.HttpConst;
import request.Request;
import request.RequestStatusLine;
import response.HttpResponseWriter;
import utils.RequestParser;
import webserver.ContentTypeMapper;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class StaticFileHandler {

    public static void handle(Request request, DataOutputStream dos) throws IOException {

        String url = request.getStatusLine().url();

        if(url.equals("/")) {
            url = HttpConst.MAIN_PAGE;
        }

        File file = new File("src/main/resources/static/" + url);
        byte[] body = Files.readAllBytes(file.toPath());

        String contentType = ContentTypeMapper.getContentType(RequestParser.extractExtension(url));

        HttpResponseWriter.response200Header(dos, contentType, body.length);
        HttpResponseWriter.responseBody(dos, body);
        // 단일 책임 원칙에 대해 생각해볼것

    }



}
