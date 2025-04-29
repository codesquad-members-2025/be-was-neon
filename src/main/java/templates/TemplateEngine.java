package templates;

import loader.ResourceData;
import loader.StaticResourceLoader;

import java.nio.charset.StandardCharsets;
import java.util.Map;

public class TemplateEngine {
    public static byte[] render(String templateUrl, Map<String, String> model){

        ResourceData resourceData = StaticResourceLoader.loadResourceData(templateUrl);

        String body = new String(resourceData.readAllBytes(), StandardCharsets.UTF_8);

        for (Map.Entry<String, String> entry : model.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            body = body.replace("{{" + key + "}}", value);
        }

        return body.getBytes(StandardCharsets.UTF_8);
    }
}

