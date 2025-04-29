package templates;

import Exceptions.HttpException;
import model.Article;
import model.User;
import response.Status;

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
        sb.append("""
                <table class="user-table">
                <caption>User Information</caption>
                <thead>
                <tr>
                <th>User ID</th>
                <th>Password</th>
                <th>Nickname</th>
                <th>Email</th>
                </tr>
                </thead>
                <tbody>
            """);
        for (User user : users) {
            sb.append("<tr>");
            sb.append("<td>" + user.getUserId() + "</td>");
            sb.append("<td>" + user.getPassword() + "</td>");
            sb.append("<td>" + user.getNickname() + "</td>");
            sb.append("<td>" + user.getEmail() + "</td>");
            sb.append("</tr>");
        }
        sb.append("</tbody>");
        sb.append("</table>");
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

    public static String errorMessage(HttpException e){
        StringBuilder sb = new StringBuilder();
        Status status = e.getStatus();

        sb.append("<div class=\"error-container\">\n" +
                "                <div class=\"error-info\">\n" +
                "                <span class=\"error-code\">").append(status.getCode()).append("</span> -\n" +
                "                <span class=\"error-description\">").append(status.getMessage()).append("</span>\n" +
                "                </div>\n" +
                "                <div class=\"error-message\">\n")
                                 .append(e.getMessage()).append("\n" +
                "                </div>\n" +
                "                </div>");
        return sb.toString();
    }
}
