package webserver;

import java.io.*;
import java.net.Socket;

import java.util.HashMap;

import db.Database;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.request.HttpRequest;
import webserver.response.HttpResponse;

import static util.Parser.loggerParser;
import static util.Parser.requestPathParser;

public class RequestHandler implements Runnable { //dispatcher!!, request, response 디렉토리로 구조 변경해보기
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);

    private Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        logger.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            HttpRequest request = new HttpRequest(in);
            HttpResponse response = new HttpResponse(new DataOutputStream(out));


            System.out.println("path: "+request.getPath());
            System.out.println("method: "+request.getMethod());
            System.out.println("version: "+request.getVersion());
            System.out.println(request.getQueryParams());

            if (request.getPath().contains("/create")) {
                User user = new User(
                        request.getQueryParams().get("userId"),
                        request.getQueryParams().get("password"),
                        request.getQueryParams().get("name"),
                        request.getQueryParams().get("email")
                );
                Database.addUser(user);
            }

            String basePath = "C:\\CodeSquad-Project-WebServer\\be-was-neon\\src\\main\\resources\\static";
            File file = new File(basePath + request.getPath());

            if (!file.exists() || file.isDirectory()) {
                logger.error("File not found: {}", file.getAbsolutePath());
                response.response404();
                return;
            }

            byte[] body = readFileToByteArray(file);
            String contentType = ContentType.getContentType(request.getPath());
            response.response200(body, contentType);




        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }


    //자바의 정석 - FileInputStream 공부
    private byte[] readFileToByteArray(File file) throws IOException {
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
