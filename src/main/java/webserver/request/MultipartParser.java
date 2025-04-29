package webserver.request;

import static webserver.common.Constants.CRLF;
import static webserver.request.RequestParser.getQueryMap;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MultipartParser {

    private static final String BOUNDARY_PREFIX = "--";
    private static final String CONTENT_DISPOSITION = "Content-Disposition";
    private static final String FILENAME_PREFIX = "filename=\"";
    private static final String CONTENT_TYPE = "Content-Type";
    private static final String FILENAME = "filename";
    private static final String BOUNDARY = "boundary=";
    private static final String NAME_PREFIX = "name=\"";
    private static final String DOUBLE_QUOTE = "\"";
    private static final String IMAGE_PATH = "src/main/resources/static/upload/images/";
    private static final String IMAGE_URL = "imageUrl";
    private static final int FILE_NAME_LENGTH = 10;
    private static final int NAME_LENGTH = 6;
    private static final Logger logger = LoggerFactory.getLogger(MultipartParser.class);

    public static Map<String, String> getBody(InputStream in, Map<String, List<String>> requestMap) throws IOException {
        String contentLength = requestMap.getOrDefault("Content-Length", List.of()).stream().findFirst().orElse("");
        Map<String, String> bodyMap = new HashMap<>();

        if (!contentLength.isEmpty() && Integer.parseInt(contentLength) > 0) {
            int length = Integer.parseInt(contentLength);
            byte[] bodyBytes = new byte[length];
            int bytesRead = 0;

            // 전체 본문을 읽어옴
            while (bytesRead < length) {
                int result = in.read(bodyBytes, bytesRead, length - bytesRead);
                if (result == -1) break;
                bytesRead += result;
            }

            // 본문을 UTF-8로 변환하여 문자열로 처리
            String body = new String(bodyBytes, StandardCharsets.ISO_8859_1);

            // boundary를 추출
            String boundary = getBoundaryFromContentType(requestMap.getOrDefault(CONTENT_TYPE, List.of()));
            if (boundary != null) {
                // Multipart 형식일 경우 파싱 시작
                bodyMap = parseMultipartFormData(body, boundary);
            } else {
                // 일반적인 x-www-form-urlencoded 파싱
                bodyMap = getQueryMap(body);
            }
        }
        return bodyMap;
    }

    private static Map<String, String> parseMultipartFormData(String body, String boundary) throws IOException {
        Map<String, String> bodyMap = new HashMap<>();
        String boundaryLine = BOUNDARY_PREFIX + boundary;

        // boundary로 파트를 나눔
        String[] parts = body.split(boundaryLine);

        for (String part : parts) {
            if (part.trim().isEmpty()) continue;

            // 헤더가 있는 부분 찾기 (Content-Disposition)
            if (part.contains(CONTENT_DISPOSITION)) {
                String[] headers = part.split(CRLF+CRLF);
                String header = headers[0]; // Content-Disposition 등이 있는 헤더 부분
                String data = headers[1]; // 실제 데이터 (파일 또는 폼 데이터)

                if (header.contains(FILENAME)) {
                    // 파일 처리 (파일은 따로 저장하는 로직 필요)
                    String fileName = extractFileName(header);
                    saveFile(data, fileName);
                    bodyMap.put(IMAGE_URL, "/upload/images/" + fileName);
                } else {
                    header = isoToUtf8(header);
                    data = isoToUtf8(data);
                    // 일반적인 폼 데이터 처리
                    String name = extractName(header);
                    bodyMap.put(name, data);
                }
            }
        }
        return bodyMap;
    }

    private static String isoToUtf8(String value) {
        byte[] bytes = value.getBytes(StandardCharsets.ISO_8859_1);
        return new String(bytes, StandardCharsets.UTF_8);
    }

    private static String getBoundaryFromContentType(List<String> contentTypeList) {
        // Content-Type에서 boundary를 추출하는 메서드
        for (String contentType : contentTypeList) {
            if (contentType.contains(BOUNDARY)) {
                return contentType.split(BOUNDARY)[1];
            }
        }
        return null;
    }

    private static String extractFileName(String header) {
        // 파일명 추출하는 메서드 (Content-Disposition에서 filename=)
        header = isoToUtf8(header);
        int start = header.indexOf(FILENAME_PREFIX) + FILE_NAME_LENGTH;
        int end = header.indexOf(DOUBLE_QUOTE, start);
        String originalName = header.substring(start, end);

        // 확장자 유지 (예: .png, .jpg 등)
        String extension = "";
        int dotIndex = originalName.lastIndexOf(".");
        if (dotIndex != -1) {
            extension = originalName.substring(dotIndex);
        }

        // 무작위 파일명 생성
        return UUID.randomUUID().toString() + extension;
    }

    private static String extractName(String header) {
        // 일반적인 폼 데이터의 name 추출
        int start = header.indexOf(NAME_PREFIX) + NAME_LENGTH;
        int end = header.indexOf(DOUBLE_QUOTE, start);
        return header.substring(start, end);
    }

    private static void saveFile(String data, String fileName) throws IOException {
        // 파일 저장 로직 (실제로 파일을 디스크에 저장)
        try (FileOutputStream fos = new FileOutputStream(IMAGE_PATH + fileName)) {
            fos.write(data.getBytes(StandardCharsets.ISO_8859_1));
        }
    }
}