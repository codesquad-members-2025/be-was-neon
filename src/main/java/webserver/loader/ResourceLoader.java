package webserver.loader;

public interface ResourceLoader {
    int BUFFER_SIZE = 1024;
    int OFFSET = 0;
    byte[] fileToBytes(String requestUrl);
}
