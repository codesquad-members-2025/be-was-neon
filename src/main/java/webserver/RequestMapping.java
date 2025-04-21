package webserver;

import webserver.controller.Controller;
import webserver.controller.impl.SignUpController;
import webserver.controller.impl.StaticFileController;

import java.util.HashMap;
import java.util.Map;

public class RequestMapping {

    private RequestMapping() {}

    private static Map<String, Controller> controllers = new HashMap<>();
    private static StaticFileController staticFileController = new StaticFileController();

    public static final String CREATE = "/create";

    static{
        controllers.put(CREATE, new SignUpController());
    }

    public static Controller getController(String requestPath) {
        Controller controller = controllers.get(requestPath);
        if (controller == null) {
            return staticFileController;
        }
        return controller;
    }
}
