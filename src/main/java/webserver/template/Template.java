package webserver.template;

public class Template {
    private Template() {}

    public static final String LOGGED_IN_HEADER =
            "<li class=\"header__menu__item\">"
                    + "어서오세요. {{name}}님"
                    + "</li>"
                    + "<li class=\"header__menu__item\">"
                    + "<a class=\"btn btn_contained btn_size_s\" href=\"/article\">글쓰기</a>"
                    + "</li>"
                    + "<li class=\"header__menu__item\">"
                    + "<a id=\"logout-btn\" class=\"btn btn_ghost btn_size_s\" href=\"/logout\">로그아웃</a>"
                    + "</li>";

    public static final String LOGGED_OUT_HEADER =
            "<li class=\"header__menu__item\">"
                    + "<a class=\"btn btn_contained btn_size_s\" href=\"/login\">로그인</a>"
                    + "</li>"
                    + "<li class=\"header__menu__item\">"
                    + "<a class=\"btn btn_ghost btn_size_s\" href=\"/registration\">회원 가입</a>"
                    + "</li>";
}