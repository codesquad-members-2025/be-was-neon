package webserver.loader;

public interface ResourceLoader {
    final int BUFFER_SIZE = 1024;   // 1024 바이트 = 1 KB
    final int OFFSET = 0;           // buffer 배열에서 항상 처음(0)부터 쓰겠다는 의미
    byte[] fileToBytes(String requestURL);
}
