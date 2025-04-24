package response.handler;

import request.Request;
import response.Response;
import response.ResponseSender;
import response.Status;
import templates.HeaderModelBuilder;
import templates.TemplateEngine;

import java.util.Map;

import static constants.HttpHeaders.*;

public class DynamicHeaderHandler implements Handler{
    @Override
    public void sendResponse(Request request, ResponseSender responseSender) {
        String path = request.getRequestHeader().getPath();

        Map<String,String> model = HeaderModelBuilder.build(request);

        byte[] body = TemplateEngine.render(path, model);

        Response response = Response.builder()
                .httpVersion(request.getRequestHeader().getHttpVersion())
                .status(Status.OK)
                .header(CONTENT_LENGTH, Integer.toString(body.length))
                .body(body)
                .build();

        responseSender.send(response);
    }
}
