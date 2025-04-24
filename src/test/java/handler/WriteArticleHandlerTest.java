package handler;

import static handler.Handler.SESSION_USER;
import static org.assertj.core.api.Assertions.*;

import db.Database;
import handler.article.WriteArticleHandler;
import java.util.Map;
import model.Article;
import model.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import webserver.exception.UnauthorizedUserException;
import webserver.request.Request;
import webserver.session.Session;

class WriteArticleHandlerTest {

    @Test
    @DisplayName("로그인 하지 않은 경우 에러가 발생한다.")
    void writeArticleNotLoginTest() {
        //given
        Session session = new Session("testSession");
        Handler handler = new WriteArticleHandler() {
            @Override
            public Session getSessionByCookie(Request req) {
                return session;
            }
        };

        String[] requestLine = new String[]{"POST", "/article", "HTTP/1.1"};
        Request request = new Request(requestLine, null, null, null);

        //when && then
        assertThatThrownBy(() -> handler.handle(request))
                .isInstanceOf(UnauthorizedUserException.class)
                .hasMessage("로그인하지 않은 사용자 입니다.");
    }

    @Test
    @DisplayName("로그인한 사용자는 글을 DB에 저장할 수 있다.")
    void writeArticleTest() {
        //given
        Session session = new Session("testSession");
        User user = new User("testId", "password", "testName", "test@example.com");
        session.setAttribute(SESSION_USER, user);
        Handler handler = new WriteArticleHandler() {
            @Override
            public Session getSessionByCookie(Request req) {
                return session;
            }
        };

        String[] requestLine = new String[]{"POST", "/article", "HTTP/1.1"};
        Map<String, String> body = Map.of("content", "test");
        Request request = new Request(requestLine, null, null, body);


        //when
        handler.handle(request);
        Article article = Database.findArticleById(1);

        //then
        assertThat(article.getContent()).isEqualTo("test");
    }
}