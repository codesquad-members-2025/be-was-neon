package templates;

import Exceptions.HttpException;
import loader.ResourceData;
import loader.StaticResourceLoader;
import response.Status;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class TemplateEngine {
    public static byte[] render(String templateUrl, Map<String, String> model){
        try {
            ResourceData resourceData = StaticResourceLoader.loadResourceData(templateUrl);

            String body = new String(resourceData.getInputStream().readAllBytes(), StandardCharsets.UTF_8);

            for (Map.Entry<String, String> entry : model.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                body = body.replace("{{" + key + "}}", value);
            }

            return body.getBytes(StandardCharsets.UTF_8);
        } catch (IOException ex) {
            throw new HttpException(Status.INTERNAL_SERVER_ERROR, ex.getMessage());
        }
    }
}
