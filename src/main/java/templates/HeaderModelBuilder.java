package templates;

import model.User;

import java.util.Map;

public class HeaderModelBuilder {
    public static Map<String, String> build(Map<String,String> model, User user) {
        if (user != null) {
            model.put("nickname", HtmlBuilder.headerNickname(user.getNickname()));
            model.put("loginOrLogout", HtmlBuilder.headerLogoutButton());
        } else {
            model.put("nickname", "");
            model.put("loginOrLogout", HtmlBuilder.headerLoginButton());
        }

        return model;
    }
}
