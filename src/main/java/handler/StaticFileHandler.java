package handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.ContentType;
import webserver.WebServer;
import webserver.request.HttpRequest;
import webserver.response.HttpResponse;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

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
        //파일을 읽어서 바이트 배열로 만들때 사용 : 바이트 데이터를 메모리의 바이트 배열로 저장할 수 있는 출력 스트림
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        //FileInputStream으로 파일 열기
        FileInputStream fis = new FileInputStream(file);

        //1024 바이트(1KB)씩 읽고 ByteArrayOutputStream에 저장
        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = fis.read(buffer)) != -1) {
            baos.write(buffer, 0, bytesRead);
        }

        fis.close(); // 또는 try-with-resources로 처리 가능
        return baos.toByteArray();
    }
}
