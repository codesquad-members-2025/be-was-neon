package handler;

import exception.ClientException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

public class StaticRequestHandler {

    private static final Logger logger = LoggerFactory.getLogger(StaticRequestHandler.class);
    private static final String STATIC_DIRECTORY = "src/main/resources/static";

    public void handleStaticRequest(String path, OutputStream out) throws IOException {
        if (path.endsWith("/")) {
            path += "index.html";
        } else if ("/".equals(path)) {
            path = "/index.html";
        }

        path = URLDecoder.decode(path, StandardCharsets.UTF_8.name());
        File file = new File(STATIC_DIRECTORY + File.separator + path.replace("/", File.separator));

        try {
            validateFilePath(file);

            if (file.exists() && !file.isDirectory()) {
                serveFile(file, out);
            } else if (file.isDirectory()) {
                handleDirectoryRequest(file, out);
            } else {
                throw new ClientException("Not Found", 404);
            }
        } catch (SecurityException e) {
            throw new ClientException("Forbidden", 403);
        }
    }

    private void serveFile(File file, OutputStream out) throws IOException {
        byte[] body = readFileToByteArray(file);
        String contentType = determineContentType(file);
        logger.info("Served file: {}", file.getPath());
        HttpResponseHelper.sendResponse(out, 200, "OK", contentType, body); // 헬퍼 클래스 사용
    }

    private void handleDirectoryRequest(File directory, OutputStream out) throws IOException {
        File indexFile = new File(directory, "index.html");
        if (indexFile.exists()) {
            serveFile(indexFile, out);
        } else {
            HttpResponseHelper.sendErrorResponse(out, 403, "Directory Listing Denied"); // 헬퍼 클래스 사용
        }
    }

    private byte[] readFileToByteArray(File file) throws IOException {
        try (FileInputStream fis = new FileInputStream(file);
             ByteArrayOutputStream bos = new ByteArrayOutputStream((int) file.length())) {

            byte[] buffer = new byte[8192];
            int bytesRead;
            while ((bytesRead = fis.read(buffer)) != -1) {
                bos.write(buffer, 0, bytesRead);
            }
            return bos.toByteArray();
        }
    }

    private void validateFilePath(File file) throws IOException {
        String canonicalPath = file.getCanonicalPath();
        String staticDirCanonical = new File(STATIC_DIRECTORY).getCanonicalPath();

        if (!canonicalPath.startsWith(staticDirCanonical)) {
            throw new SecurityException("Invalid file access attempt");
        }
    }

    private String determineContentType(File file) {
        String fileName = file.getName().toLowerCase();

        if (fileName.endsWith(".html")) return "text/html;charset=utf-8";
        if (fileName.endsWith(".css")) return "text/css";
        if (fileName.endsWith(".js")) return "application/javascript";
        if (fileName.endsWith(".png")) return "image/png";
        if (fileName.endsWith(".jpg") || fileName.endsWith(".jpeg")) return "image/jpeg";
        if (fileName.endsWith(".svg")) return "image/svg+xml";
        if (fileName.endsWith(".gif")) return "image/gif";
        if (fileName.endsWith(".ico")) return "image/x-icon";
        if (fileName.endsWith(".woff")) return "font/woff";
        if (fileName.endsWith(".woff2")) return "font/woff2";
        if (fileName.endsWith(".ttf")) return "font/ttf";
        if (fileName.endsWith(".eot")) return "application/vnd.ms-fontobject";
        if (fileName.endsWith(".otf")) return "font/otf";

        return "application/octet-stream";
    }
}
