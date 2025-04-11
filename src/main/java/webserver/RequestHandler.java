package webserver;

import java.io.*;
import java.net.Socket;

import http.ContentType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RequestHandler implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);
    private final String PATH = "src/main/resources/static";

    private Socket connection;
    private final ContentType contentType;

    public RequestHandler(Socket connectionSocket, ContentType contentType) {
        this.connection = connectionSocket;
        this.contentType = contentType;
    }

    public void run() {
        logger.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF-8"));

            String requestLine = br.readLine();
            logger.debug("request line : {}", requestLine);

            // 헤더 읽기
            String line;
            while ((line = br.readLine()) != null && !line.equals("")) {
                logger.debug("header line : {}", line);
            }

            // 요청 경로 파싱
            String[] tokens = requestLine.split(" ");
            String urlPath = tokens[1];
            if(urlPath.equals("/")) {
                urlPath = "/index.html";
            }

            DataOutputStream dos = new DataOutputStream(out);
            File file = new File(PATH + urlPath);

            if(file.exists()) {
                byte[] body = new byte[(int) file.length()];
                try (FileInputStream fis = new FileInputStream(file)){
                    fis.read(body);
                }
                response200Header(dos, body.length, contentType.getContentType(urlPath));
                responseBody(dos, body);
            } else {
                // 파일을 찾지 못했을 경우, 브라우저에 띄워줄 간단한 메세지
                String notFoundMessage = "&lt;h1&gt;404 Not Found&lt;/h1&gt;";
                byte[] body = notFoundMessage.getBytes();
                dos.writeBytes("HTTP/1.1 404 Not Found\r\n");
                dos.writeBytes("Content-Type: text/html;charset=utf-8\r\n");
                dos.writeBytes("Content-Length: " + body.length + "\r\n");
                dos.writeBytes("\r\n");
                dos.write(body, 0, body.length);
                dos.flush();
            }
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private void response200Header(DataOutputStream dos, int lengthOfBodyContent, String contentType) {
        try {
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
            // flush() : 버퍼에 남아있는 데이터를 강제로 전송한다, 네트워크에 응답을 즉시 보내야 할 때 사용한다
            dos.flush();
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }
}
