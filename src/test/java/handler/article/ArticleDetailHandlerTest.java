package handler.article;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import db.Database;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import model.Article;
import model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import webserver.common.HttpStatus;
import webserver.loader.FileResourceLoader;
import webserver.request.Request;
import webserver.response.Response;

class ArticleDetailHandlerTest {

    @AfterEach
    public void clearDB() {
        Database.deleteAllArticles();
        Database.deleteAll();
    }

    @Test
    @DisplayName("최신 게시글을 가져오고 이전/다음 게시글 ID도 함계 응답한다")
    void currentArticleWithPreviousAndNextArticleIds() {
        //given
        User user = new User("testId", "password", "testName", "test@example.com");
        User savedUser = Database.addUser(user);
        Database.addArticle(new Article("title1", "test1", savedUser));// id = 1
        Database.addArticle(new Article("title2", "test2", savedUser));// id = 2
        Database.addArticle(new Article("title3", "test3", savedUser));// id = 3

        ArticleDetailHandler handler = new ArticleDetailHandler(new FileResourceLoader());

        //when
        String[] requestLine = new String[]{"GET", "/2", "HTTP/1.1"};
        Response response = handler.handle(new Request(new Request(requestLine, null, Map.of("Cookie", List.of("test")), null), Map.of("id", "2")));
        String body = new String(response.getBody(), StandardCharsets.UTF_8);

        //then
        assertThat(response.getHttpStatus()).isEqualTo(HttpStatus.OK);

        assertTrue(body.contains("<p class=\"post__title\">title2</p>"), "현재 글 제목이 포함되어야 함");
        assertTrue(body.contains("<p class=\"post__article\">test2</p>"), "현재 글 내용이 포함되어야 함");
        assertTrue(body.contains("href=\"/1\""), "이전 글 링크가 포함되어야 함");
        assertTrue(body.contains("href=\"/3\""), "다음 글 링크가 포함되어야 함");

    }

}