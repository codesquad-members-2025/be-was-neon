package webserver.util;
import java.io.IOException;
import java.io.BufferedReader;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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

    // 필터링된 헤더를 묶어서 문자열로 반환
    public static String extractFilteredHeaders(BufferedReader br) throws IOException {
        StringBuilder filteredHeaders = new StringBuilder();
        filteredHeaders.append("📥 New HTTP Request Headers:\n");

        List<String> importantHeaders = List.of("Host", "User-Agent", "Accept", "Cookie", "Referer");

        String line;
        while ((line = br.readLine()) != null && !line.isEmpty()) {
            for (String key : importantHeaders) {
                if (line.toLowerCase().startsWith(key.toLowerCase() + ":")) {
                    filteredHeaders.append(" - ").append(line).append("\n");
                    break;
                }
            }
        }

        return filteredHeaders.toString();
    }
}
