package handler.request;

import java.io.OutputStream;

public record RequestContext(
        String path,
        String method,
        String body,
        OutputStream out
) {}