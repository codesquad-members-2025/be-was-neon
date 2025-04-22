package handler;

import model.User;
import service.UserService;
import session.SessionManager;

import java.util.List;
import java.util.Map;

import static session.SessionManager.SESSION_COOKIE_NAME;

public class UserListHandler implements ReturnViewPathHandler {

    @Override
    public String process(Map<String, String> paramMap, Map<String, Object> model) {
        String cookieId = paramMap.getOrDefault(SESSION_COOKIE_NAME,null);
        if (cookieId == null) {
            return "redirect:/login.html";
        }

        User user = SessionManager.getUser(cookieId);

        List<User> users = UserService.getAllUsers();
        model.put("users", users);
        return "dynamic/user-list";
    }
}
