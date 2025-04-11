package loader;

import java.io.InputStream;

public class ResourceData {
    private final String extension;
    private final InputStream inputStream;

    public ResourceData(String extension, InputStream inputStream) {
        this.extension = extension;
        this.inputStream = inputStream;
    }

    public String getExtension() {
        return extension;
    }

    public InputStream getInputStream() {
        return inputStream;
    }
}
