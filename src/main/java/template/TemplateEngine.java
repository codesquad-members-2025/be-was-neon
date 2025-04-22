package template;

import static handler.Handler.SESSION_USER;

import db.Database;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import model.User;
import webserver.exception.UnauthorizedUserException;
import webserver.session.Session;

public class TemplateEngine {
    private static final String HEADER_PLACEHOLDER = "{{header}}";
    private static final String USER_LIST_PLACEHOLDER = "{{userList}}";

    public static byte[] renderingHeader(Session session, byte[] responseBody) {
        String string = new String(responseBody, StandardCharsets.UTF_8);
        User user = (User) session.getAttribute(SESSION_USER);

        String content;
        if (Optional.ofNullable(user).isEmpty()) {

            content =
                    """
                        <header class="header">
                          <a href="/"><img src="./img/signiture.svg" /></a>
                          <ul class="header__menu">
                            <li class="header__menu__item">
                              <a class="btn btn_contained btn_size_s" href="/login">로그인</a>
                            </li>
                            <li class="header__menu__item">
                              <a class="btn btn_ghost btn_size_s" href="/registration">
                                회원 가입
                              </a>
                            </li>
                          </ul>
                        </header>
                     """;
        }else{
            content =
                    """
                        <header class="header">
                          <a href="/"><img src="./img/signiture.svg" /></a>
                          <ul class="header__menu">
                            <li class="header__menu__item">
                              <a class="btn btn_contained btn_size_s" style="pointer-events: none; cursor: default;">
                                """ + user.getName() + """
                               님 </a>
                            </li>
                            <li class="header__menu__item">
                              <form method="POST" action="/user/logout" style="display:inline;">
                                <button type="submit" class="btn btn_ghost btn_size_s">
                                  로그아웃
                                </button>
                              </form>
                            </li>
                          </ul>
                        </header>
                    """;
        }

        String replaced = string.replace(HEADER_PLACEHOLDER, content);
        return replaced.getBytes(StandardCharsets.UTF_8);
    }

    public static byte[] renderingUserList(Session session, byte[] responseBody){
        String string = new String(responseBody, StandardCharsets.UTF_8);
        User sessionUser = (User) session.getAttribute(SESSION_USER);

        StringBuilder sb = new StringBuilder();
        if (Optional.ofNullable(sessionUser).isEmpty()) {
            throw new UnauthorizedUserException("로그인하지 않은 사용자 입니다.");
        }else{
            for (User user : Database.findAll()) {
                sb.append("<tr>")
                        .append("<td>").append(user.getUserId()).append("</td>")
                        .append("<td>").append(user.getName()).append("</td>")
                        .append("<td>").append(user.getEmail()).append("</td>")
                        .append("</tr>");
            }
        }
        String replaced = string.replace(USER_LIST_PLACEHOLDER, sb);
        return replaced.getBytes(StandardCharsets.UTF_8);
    }
}
