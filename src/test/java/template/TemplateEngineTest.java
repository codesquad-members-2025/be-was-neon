package template;

import static handler.Handler.SESSION_USER;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

import db.Database;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import model.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import webserver.exception.UnauthorizedUserException;
import webserver.session.Session;
import webserver.session.SessionManager;

class TemplateEngineTest {


    @Test
    @DisplayName("로그인하지 않으면 로그인버튼과 회원가입 버튼을 보여준다.")
    void renderingHeaderNotLoginTest() {
        //given
        Session session = mock(Session.class);
        byte[] body = "{{header}}".getBytes();
        when(session.getAttribute(SESSION_USER)).thenReturn(null);

        //when
        byte[] bytes = TemplateEngine.renderingHeader(session, body);
        String html = new String(bytes, StandardCharsets.UTF_8);

        //then
        assertThat(html).contains("로그인");
        assertThat(html).contains("회원 가입");
        assertThat(html).doesNotContain("로그아웃");
    }

    @Test
    @DisplayName("로그인하면 로그아웃버튼과 이름을 보여준다.")
    void renderingHeaderLoginTest() {
        //given
        Session session = mock(Session.class);
        User mockUser = new User("testId", "password", "testName", "test@example.com");
        when(session.getAttribute(SESSION_USER)).thenReturn(mockUser);
        byte[] body = "{{header}}".getBytes();

        //when
        byte[] bytes = TemplateEngine.renderingHeader(session, body);
        String html = new String(bytes, StandardCharsets.UTF_8);

        //then
        assertThat(html).contains("로그아웃");
        assertThat(html).contains("testName");
        assertThat(html).doesNotContain("로그인");
    }

    @Test
    void renderingUserList_loginUser_returnsUserTableHtml() {
        // given
        Session session = mock(Session.class);
        User mockSessionUser = new User("testId1", "password", "testName", "test@example.com");
        when(session.getAttribute(SESSION_USER)).thenReturn(mockSessionUser); // SESSION_USER = "user"

        User user1 = new User("testId1", "password", "testName1", "test@example.com");
        User user2 = new User("testId2", "password", "testName2", "test@example.com");

        byte[] body = "{{userList}}".getBytes();

        try (MockedStatic<Database> mockedDatabase = mockStatic(Database.class)) {
            mockedDatabase.when(Database::findAll).thenReturn(List.of(user1, user2));

            // when
            byte[] result = TemplateEngine.renderingUserList(session, body);
            String html = new String(result, StandardCharsets.UTF_8);

            // then
            assertThat(html).contains("<td>testId1</td>");
            assertThat(html).contains("<td>testName1</td>");
            assertThat(html).contains("<td>testId2</td>");
            assertThat(html).contains("<td>testName2</td>");
        }
    }

    @Test
    void renderingUserList_notLoggedIn_throwsUnauthorizedUserException() {
        // given
        Session session = mock(Session.class);
        when(session.getAttribute(SESSION_USER)).thenReturn(null);

        byte[] body = "{{userList}}".getBytes();

        // when + then
        assertThatThrownBy(() -> TemplateEngine.renderingUserList(session, body))
                .isInstanceOf(UnauthorizedUserException.class)
                .hasMessage("로그인하지 않은 사용자 입니다.");
    }
}