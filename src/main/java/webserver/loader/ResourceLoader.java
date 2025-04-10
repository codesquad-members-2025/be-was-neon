package webserver.loader;

import java.io.FileNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public interface ResourceLoader {
    Logger logger = LoggerFactory.getLogger(ResourceLoader.class);
    int BUFFER_SIZE = 1024;
    int OFFSET = 0;
    byte[] fileToBytes(String requestUrl) throws FileNotFoundException;
}
