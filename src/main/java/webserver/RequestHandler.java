package webserver;

import java.io.*;
import java.net.Socket;

import java.util.HashMap;

import db.Database;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
            BufferedReader br = new BufferedReader(new InputStreamReader(in , "UTF-8"));
            String line = br.readLine();
            if (line == null || line.isEmpty()) {
                return;
            }
            String[] request_lines = line.split(" ");
            String file = request_lines[1];

            logger.debug("request line : {}", line);

            //HTTP request 내용 파싱하기
            HashMap<String, String> requests = new HashMap<>();
            HashMap<String, String> headers = new HashMap<>();
            String requestLine = "requestLine";
            String header = "header";
            loggerParser(line, requests, requestLine);
            while(!line.isEmpty()){
                line = br.readLine();
                logger.debug("header : {}", line);
                loggerParser(line, headers, header);
            }

            System.out.println(requests);
            System.out.println(headers);
            if(requests.get("path").contains("create")){
                User user = requestPathParser(requests);
                Database.addUser(user);
            }

            // TODO 사용자 요청에 대한 처리는 이 곳에 구현하면 된다.
            DataOutputStream dos = new DataOutputStream(out);

            // index.html 파일 읽어오기
            String url = "C:\\CodeSquad-Project-WebServer\\be-was-neon\\src\\main\\resources\\static"; // "src/main/resources/static"
            url += file;

            File f = new File(url);
            if (!f.exists() || f.isDirectory()) {
                logger.error("File not found: {}", f.getAbsolutePath());
                return; // 혹은 404 응답
            }
            byte[] body = readFileToByteArray(f);
            String contentType = ContentType.getContentType(file);
            response200Header(dos, body.length, contentType); // body 값
            responseBody(dos, body);
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


    private void response200Header(DataOutputStream dos, int lengthOfBodyContent, String contentType) {
        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            dos.writeBytes("Content-Type: "+ contentType+"\r\n");
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
