package template;

import java.nio.charset.StandardCharsets;
import model.User;

/**
 * 헤더 템플릿을 렌더링하는 클래스입니다.
 * 사용자의 로그인 상태에 따라 다른 헤더를 렌더링합니다.
 */
public class HeaderRenderer implements TemplateRenderer {
    private static final String HEADER_PLACEHOLDER = "{{header}}";

    /**
     * 주어진 템플릿에 사용자의 로그인 상태에 맞는 헤더를 렌더링합니다.
     *
     * @param user 현재 로그인한 사용자 정보. 로그인하지 않은 경우 null
     * @param template 렌더링할 템플릿의 바이트 배열
     * @return 헤더가 렌더링된 결과의 바이트 배열
     */
    @Override
    public byte[] render(User user, byte[] template) {
        String html = new String(template, StandardCharsets.UTF_8);
        String header = (user == null) ? notLoggedInHeader() : loggedInHeader(user);
        String replaced = html.replace(HEADER_PLACEHOLDER, header);
        return replaced.getBytes(StandardCharsets.UTF_8);
    }

    /**
     * 로그인하지 않은 사용자를 위한 헤더 HTML을 생성합니다.
     *
     * @return 로그인과 회원가입 버튼이 포함된 헤더 HTML
     */
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

    /**
     * 로그인한 사용자를 위한 헤더 HTML을 생성합니다.
     *
     * @param user 현재 로그인한 사용자 정보
     * @return 사용자 이름과 로그아웃 버튼이 포함된 헤더 HTML
     */
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