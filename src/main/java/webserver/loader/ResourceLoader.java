package webserver.loader;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public interface ResourceLoader {
    String FILE_NOT_FOUND = "파일을 찾을 수 없습니다.";
    String DEFAULT_PAGE = "index.html";
    int BUFFER_SIZE = 1024;
    int OFFSET = 0;

    default byte[] fileToBytes(String requestUrl){
        try (InputStream is = getInputStreamByUrl(requestUrl);
             ByteArrayOutputStream out = new ByteArrayOutputStream()) {

            byte[] buffer = new byte[BUFFER_SIZE];
            int read;
            while ((read = is.read(buffer)) != -1) {
                out.write(buffer, OFFSET, read);
            }
            return out.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

   InputStream getInputStreamByUrl(String requestUrl) throws FileNotFoundException;

    boolean exists(String path);
}
