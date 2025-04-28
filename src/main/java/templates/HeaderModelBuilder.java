package templates;

import model.User;

import java.util.Map;

import static constants.SpecialChars.EMPTY;
import static templates.ModelConstants.*;

public class HeaderModelBuilder {
    public static Map<String, String> build(Map<String,String> model, User user) {
        if (user != null) {
            model.put(GREETING, HtmlBuilder.headerNickname(user.getNickname()));
            model.put(LOGIN_OR_LOGOUT, HtmlBuilder.headerLogoutButton());
        } else {
            model.put(GREETING, EMPTY);
            model.put(LOGIN_OR_LOGOUT, HtmlBuilder.headerLoginButton());
        }

        return model;
    }
}
