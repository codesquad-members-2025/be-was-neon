package templates;

import model.User;

import java.util.Map;

import static constants.SpecialChars.EMPTY;
import static templates.ModelConstants.LOGIN_OR_LOGOUT;
import static templates.ModelConstants.NICKNAME;

public class HeaderModelBuilder {
    public static Map<String, String> build(Map<String,String> model, User user) {
        if (user != null) {
            model.put(NICKNAME, HtmlBuilder.headerNickname(user.getNickname()));
            model.put(LOGIN_OR_LOGOUT, HtmlBuilder.headerLogoutButton());
        } else {
            model.put(NICKNAME, EMPTY);
            model.put(LOGIN_OR_LOGOUT, HtmlBuilder.headerLoginButton());
        }

        return model;
    }
}
