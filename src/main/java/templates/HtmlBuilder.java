package templates;

import model.User;

import java.util.List;

public class HtmlBuilder {
    public static String headerLoginButton(){
        return """
            <li class="header__menu__item">
              <a class="btn btn_contained btn_size_s" href="/user/login.html">로그인</a>
            </li>
            """ ;
    }

    public static String headerLogoutButton(){
        return """
            <li class="header__menu__item">
              <form method="post" action="/user/logout">
                <button id="logout-btn" class="btn btn_contained btn_size_s" type="submit">
                  로그아웃
                </button>
              </form>
            </li>
            """ ;
    }

    public static String headerNickname(String nickname){
        return """
            <li class="header__menu__item">
              <span class="header__menu__item__username">""" + nickname + """
            </span>
            </li>
            """ ;
    }

    public static String userList(List<User> users) {
        StringBuilder sb = new StringBuilder();
        sb.append("<h2>사용자 목록</h2>\n");
        sb.append("<ul>\n");
        for (User user : users) {
            sb.append("<li>").append(user.toString()).append("</li>\n");
        }
        sb.append("</ul>\n");
        return sb.toString();
    }
}
