package templates;

import model.Article;
import model.User;

import java.util.Collection;

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
              <span class="header__menu__item__username">""" + "환영합니다 " + nickname + "님" + """
            </span>
            </li>
            """ ;
    }

    public static String userList(Collection<User> users) {
        StringBuilder sb = new StringBuilder();
        sb.append("<h2>사용자 목록</h2>\n");
        sb.append("<ul class=\"comment\">\n");
        for (User user : users) {
            sb.append("<li class=\"comment_item\">").append(user.toString()).append("</li>\n");
        }
        sb.append("</ul>\n");
        return sb.toString();
    }

    public static String articleList(Collection<Article> articles){
        StringBuilder sb = new StringBuilder();
        for (Article article : articles) {
            sb.append("<li class=\"article-item\">");
            sb.append("<a href=\"/articles/").append(article.getId()).append("\" class=\"article-title\">").append(article.getTitle()).append("</a>\n");
            sb.append("<span class=\"article-author\">").append(article.getAuthor().getNickname()).append("</span>\n");
            sb.append("</li>\n");
        }
        return sb.toString();
    }
}
