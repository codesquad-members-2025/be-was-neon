package utils;

import model.User;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class DynamicHtmlBuilder {
    private static final String STATIC_DIR = "src/main/resources/static";

    public static byte[] buildIndexPage(User user) throws IOException {
        String template = Files.readString(Paths.get(STATIC_DIR, "index.html"));

        StringBuilder content = new StringBuilder();
        if (user != null) {
            content.append("""
                <div class="user-info">
                    <span>%s님 환영합니다!</span>
                    <a href="/logout" class="btn">로그아웃</a>
                </div>
                """.formatted(escapeHtml(user.getName())));
        } else {
            content.append("""
                <div class="auth-buttons">
                    <a href="/login.html" class="btn">로그인</a>
                    <a href="/signup.html" class="btn">회원가입</a>
                </div>
                """);
        }

        return template.replace("<!-- DYNAMIC_CONTENT -->", content.toString())
                .getBytes(StandardCharsets.UTF_8);
    }

    public static byte[] buildUserListPage(List<User> users) throws IOException {
        String template = Files.readString(Paths.get(STATIC_DIR, "user-list.html"));

        StringBuilder userList = new StringBuilder("<ul class='user-list'>");
        for (User user : users) {
            userList.append("""
                <li class="user-item">
                    <strong>%s</strong> (%s)
                    <span class="email">%s</span>
                </li>
                """.formatted(
                    escapeHtml(user.getName()),
                    escapeHtml(user.getUserId()),
                    escapeHtml(user.getEmail())
            ));
        }
        userList.append("</ul>");

        return template.replace("<!-- USER_LIST -->", userList.toString())
                .getBytes(StandardCharsets.UTF_8);
    }

    private static String escapeHtml(String input) {
        return StringEscapeUtils.escapeHtml4(input);
    }
}
