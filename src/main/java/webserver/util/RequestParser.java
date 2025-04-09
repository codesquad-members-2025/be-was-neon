package webserver.util;
import java.io.IOException;
import java.io.BufferedReader;

public class RequestParser {
    //경로 추출(ex: "/index.html")
    public static String parseRequestPath(BufferedReader br) throws IOException {
        String requestLine = br.readLine(); //HTTP 요청의 첫 번째 줄(요청라인)만 사용
        if (requestLine == null || requestLine.isEmpty()) {
            throw new IOException("Invalid request line");
        }
        String[] tokens = requestLine.split(" ");
        if (tokens.length < 2) { //잘못된 형식의 요청 검사필요
            throw new IOException("Malformed request line");
        }
        return tokens[1];

    }

}
