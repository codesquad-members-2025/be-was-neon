package template;

import db.Database;
import java.nio.charset.StandardCharsets;
import model.User;

/**
 * 사용자 목록을 HTML 테이블로 렌더링하는 클래스입니다.
 * 데이터베이스에서 모든 사용자 정보를 가져와 HTML 테이블 형태로 변환합니다.
 */
public class UserListRenderer implements TemplateRenderer {
    private static final String USER_LIST_PLACEHOLDER = "{{userList}}";

    /**
     * 주어진 템플릿에 사용자 목록을 HTML 테이블로 렌더링합니다.
     *
     * @param user 현재 로그인한 사용자 정보 (이 구현에서는 사용되지 않음)
     * @param template 렌더링할 템플릿의 바이트 배열
     * @return 사용자 목록이 HTML 테이블로 렌더링된 결과의 바이트 배열
     */
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