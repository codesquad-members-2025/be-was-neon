package webserver.loader;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class FileResourceLoader implements ResourceLoader{
    private static final String PREFIX = "src/main/resources/static";
    @Override
    public byte[] fileToBytes(String requestUrl) throws FileNotFoundException {

        File file = getFileFromUrl(requestUrl);
        try (FileInputStream fis = new FileInputStream(file);
             ByteArrayOutputStream out = new ByteArrayOutputStream()) {

            byte[] buffer = new byte[BUFFER_SIZE];
            int read;
            while ((read = fis.read(buffer)) != -1) {
                out.write(buffer, OFFSET, read);
            }
            return out.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private File getFileFromUrl(String requestUrl) throws FileNotFoundException {
        File file = new File(PREFIX + requestUrl);
        // 디렉토리면 index.html을 읽음
        if (file.isDirectory()) {
            file = new File(file, "index.html");
        }

        // 파일 존재 여부 확인
        if (!file.exists() || !file.isFile()) {
            throw new FileNotFoundException("요청한 파일이 존재하지 않습니다: " + file.getPath());
        }
        return file;
    }
}
