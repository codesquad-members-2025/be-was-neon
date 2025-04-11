package webserver;

import java.io.*;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.ContentType;

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
            //클라이언트 header 분리
            String[] header = getRequestHeaders(in);
            logger.debug("Client Request (IP: {}, Port: {})\n{}",connection.getInetAddress(), connection.getPort(), String.join("\n", header));

            //클라이언트 requestLine 분리
            String[] requestLine = getRequestLine(header);
            String url = requestLine[1];

            //클라이언트 요청에 대한 응답처리
            DataOutputStream dos = new DataOutputStream(out);
            byte[] body = getRequestedFileContent(url);
            String extension = getFileExtension(url);
            ContentType contentType = ContentType.valueOf(extension.toUpperCase());
            response200Header(dos, body.length, contentType.getContentType());
            responseBody(dos, body);

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
            dos.flush();
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private String[] getRequestHeaders(InputStream in) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            if (line.isEmpty()) {
                break;
            }

            sb.append(line).append("\r\n");
        }

        String header = sb.toString();
        return header.split("\r\n");
    }

    private byte[] getRequestedFileContent(String url) {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

        byte[] body = new byte[0];

        try (InputStream fileResource = classLoader.getResourceAsStream("static" + url)) {

            if (fileResource == null) {
                logger.error("File not found: {}", url);
            }

            body = fileResource.readAllBytes();

        } catch (IOException e) {
            logger.error(e.getMessage());
        }

        return body;
    }

    private String[] getRequestLine(String[] header) {
        return header[0].split(" ");
    }

    private String getFileExtension(String url) {
        String[] tokens = url.split("\\.");
        return tokens[1];
    }
}
