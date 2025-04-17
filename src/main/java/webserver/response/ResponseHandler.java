package webserver.response;

import db.Database;
import model.User;
import webserver.loader.FileResult;
import webserver.util.ContentType;
import webserver.request.HttpRequest;
import webserver.loader.FileResourceLoader;
import webserver.loader.ResourceLoader;
import webserver.request.RequestParser;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class ResponseHandler {

    private final DataOutputStream dos;
    private final HttpRequest httpRequest;
    private final ResponseWriter responseWriter;

    private final String GET = "GET";
    private final String REGISTRATION_ACTION = "/create";
    private final String DEFAULT_MAIN_PAGE = "/index.html";
    private final String ROOT_PATH = "/";

    public ResponseHandler(HttpRequest request, OutputStream out) {
        this.dos = new DataOutputStream(out);
        this.responseWriter = new ResponseWriter(dos);
        this.httpRequest = request;
    }

    public void dispatch() throws IOException {
        if (httpRequest.getMETHOD().equals(GET)) {
            handleGetRequest();
        }
    }

    private void handleGetRequest() throws IOException {
        String urlPath = httpRequest.getURL_PATH();
        if (httpRequest.getPathWithoutQuery().equals(REGISTRATION_ACTION)) {
            User justJoinedUser = httpRequest.parseRegistrationData();
            Database.addUser(justJoinedUser);

            responseWriter.send302Redirect(ROOT_PATH);
            return;
        }

        if (urlPath.equals(ROOT_PATH)) {
            urlPath = DEFAULT_MAIN_PAGE;
        }
        ResourceLoader loader = new FileResourceLoader();
        FileResult fileResult = loader.fileToBytes(urlPath);
        responseWriter.send200(fileResult.body().length, ContentType.getContentType(fileResult.resolvedPath()), fileResult.body());
    }
}
