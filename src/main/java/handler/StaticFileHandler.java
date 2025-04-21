package handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.ContentType;
import webserver.WebServer;
import webserver.request.HttpRequest;
import webserver.response.HttpResponse;

import java.io.*;

public class StaticFileHandler {
    private static String basePath = "C:\\CodeSquad-Project-WebServer\\be-was-neon\\src\\main\\resources\\static";
    private static final Logger logger = LoggerFactory.getLogger(WebServer.class);
    public static void handle(HttpRequest request, HttpResponse response) throws IOException {
        File file = new File(basePath + request.getPath());

        if (!file.exists() || file.isDirectory()) {
            logger.error("File not found: {}", file.getAbsolutePath());
            response.response404();
            return;
        }

        byte[] body = readFileToByteArray(file);
        String contentType = ContentType.getContentType(request.getPath());
        response.response200(body, contentType);
    }

    //자바의 정석 - FileInputStream 공부
    private static byte[] readFileToByteArray(File file) throws IOException {
        try (
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file))
        ) {
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = bis.read(buffer)) != -1) {
                baos.write(buffer, 0, bytesRead);
            }
            return baos.toByteArray();
        }
    }
}
