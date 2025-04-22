package template;

import db.Database;
import java.nio.charset.StandardCharsets;
import model.User;

public class UserListRenderer implements TemplateRenderer {
    private static final String USER_LIST_PLACEHOLDER = "{{userList}}";

    @Override
    public byte[] render(User user, byte[] template) {
        String html = new String(template, StandardCharsets.UTF_8);
        StringBuilder sb = new StringBuilder();
        
        for (User u : Database.findAll()) {
            sb.append("<tr>")
                    .append("<td>").append(u.getUserId()).append("</td>")
                    .append("<td>").append(u.getName()).append("</td>")
                    .append("<td>").append(u.getEmail()).append("</td>")
                    .append("</tr>");
        }

        String replaced = html.replace(USER_LIST_PLACEHOLDER, sb.toString());
        return replaced.getBytes(StandardCharsets.UTF_8);
    }
} 