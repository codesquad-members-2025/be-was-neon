package loader;

import Exceptions.HttpException;
import response.Status;

import java.io.IOException;
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

    public byte[] readAllBytes() {
        try (InputStream inputStream = this.inputStream) {
            return inputStream.readAllBytes();
        } catch (IOException e) {
            throw new HttpException(Status.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}
