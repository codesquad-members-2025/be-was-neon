package webserver;

import java.io.*;
import java.net.Socket;
import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RequestHandler implements Runnable { //
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
            String[] request_lines = line.split(" ");
            String file = request_lines[1];
            //file = file.replace('/', '\\');
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

            // TODO 사용자 요청에 대한 처리는 이 곳에 구현하면 된다.
            DataOutputStream dos = new DataOutputStream(out);

            // index.html 파일 읽어오기
            String url = "C:\\CodeSquad-Project-WebServer\\be-was-neon\\src\\main\\resources\\static"; // "src/main/resources/static"
            url += file;

            br = new BufferedReader(new FileReader(url));
            String content = "";
            while(true){
                String l = br.readLine();
                content += l;
                if(l == null) break;
            }

            //index.html의 경로에 있는 내용들을 body에 담아주기
            byte[] body = content.getBytes();
            response200Header(dos, body.length);
            responseBody(dos, body);
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private void loggerParser(String line, HashMap<String, String> map, String type) {

        if(type.equals("requestLine")){
            String[] request_strs = line.split(" ");
            map.put("METHOD",request_strs[0]);
            map.put("Directory and FileName",request_strs[1]);
            map.put("Protocol Version", request_strs[2]);
        }
        else if(type.equals("header")){
            String[] header_strs = line.split(": ");
            StringBuilder value = new StringBuilder();
            for(int i = 1; i<header_strs.length; i++){
                value.append(header_strs[i]);
            }
            map.put(header_strs[0], value.toString());

        }
    }

    private void response200Header(DataOutputStream dos, int lengthOfBodyContent) {
        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            dos.writeBytes("Content-Type: text/html;charset=utf-8\r\n");
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
