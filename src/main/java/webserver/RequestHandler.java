package webserver;

import db.Database;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.RequestParser;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;
// nio 제거하기

public class RequestHandler implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);

    private Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        logger.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF-8"));
            String request = br.readLine();
            String[] tokens = request.split(" ");
            logger.debug("Request received: {}", request);

            while(!request.equals("")){
                request = br.readLine();
                logger.debug("Request received: {}", request);
            }

            // TODO 사용자 요청에 대한 처리는 이 곳에 구현하면 된다.
            DataOutputStream dos = new DataOutputStream(out);

            // 정적 파일 경로 설정 (예: index.html)
            String url = tokens[1];
            if(url.contains("?")){
                Map<String, String> paramMap = RequestParser.parseUserInfo(url);
                String parsedUrl = RequestParser.extractPath(url);

                User newUser = new User(
                        paramMap.get("userId"),
                        paramMap.get("name"),
                        paramMap.get("password"),
                        paramMap.get("email")
                );
                Database.addUser(newUser);

                File file = new File("src/main/resources/static" + parsedUrl);
                byte[] body = Files.readAllBytes(file.toPath());

                String extension = parsedUrl.substring(parsedUrl.lastIndexOf("."));

                response200Header(dos, extension, body.length);
                responseBody(dos, body);
            }

            else {
                String extension = url.substring(url.lastIndexOf("."));

                File file = new File("src/main/resources/static" + url);
                byte[] body = Files.readAllBytes(file.toPath());

                response200Header(dos, extension, body.length);
                responseBody(dos, body);
            }

        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private void response200Header(DataOutputStream dos, String extension, int lengthOfBodyContent) {
        try {
            String contentType = ContentTypeMapper.getContentType(extension);
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            dos.writeBytes("Content-Type: " + contentType + "\r\n");
            dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private void responseBody(DataOutputStream dos, byte[] body) {
        try {
            dos.write(body, 0, body.length);
            dos.flush();
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }
}
