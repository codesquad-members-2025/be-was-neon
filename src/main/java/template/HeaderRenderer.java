package template;

import java.nio.charset.StandardCharsets;
import model.User;

public class HeaderRenderer implements TemplateRenderer {
    private static final String HEADER_PLACEHOLDER = "{{header}}";

    @Override
    public byte[] render(User user, byte[] template) {
        String html = new String(template, StandardCharsets.UTF_8);
        String header = (user == null) ? notLoggedInHeader() : loggedInHeader(user);
        String replaced = html.replace(HEADER_PLACEHOLDER, header);
        return replaced.getBytes(StandardCharsets.UTF_8);
    }

    private String notLoggedInHeader() {
        return """
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
    }

    private String loggedInHeader(User user) {
        return """
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
} 