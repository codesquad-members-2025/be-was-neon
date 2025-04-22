package handler;

import static handler.Handler.SESSION_USER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import java.util.HashMap;
import model.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import template.TemplateRenderer;
import webserver.common.HttpStatus;
import webserver.exception.UnauthorizedUserException;
import webserver.loader.ResourceLoader;
import webserver.request.Request;
import webserver.response.Response;
import webserver.session.Session;

class TemplateHandlerTest {
    private final ResourceLoader resourceLoader = mock(ResourceLoader.class);
    private final TemplateRenderer renderer = mock(TemplateRenderer.class);
    private final Request request = mock(Request.class);

    @Test
    @DisplayName("인증이 필요한 페이지에서 로그인하지 않은 사용자가 접근하면 예외가 발생한다.")
    void handleRequireAuthNotLoginTest() {
        // given
        TemplateHandler handler = new TemplateHandler(resourceLoader, renderer, true);
        given(request.getRequestUrl()).willReturn("/user/list");

        // when + then
        assertThatThrownBy(() -> handler.handle(request))
                .isInstanceOf(UnauthorizedUserException.class)
                .hasMessage("로그인하지 않은 사용자 입니다.");
    }

    @Test
    @DisplayName("인증이 필요한 페이지에서 로그인한 사용자가 접근하면 정상적으로 처리된다.")
    void handleRequireAuthLoginTest() throws Exception {
        // given
        User user = new User("testId", "password", "testName", "test@example.com");
        Session session = mock(Session.class);

        byte[] template = "template".getBytes();
        byte[] rendered = "rendered".getBytes();

        TemplateHandler handler = new TemplateHandler(resourceLoader, renderer, true) {
            @Override
            public Session getSessionByCookie(Request req) {
                return session;
            }
        };
        given(request.getRequestUrl()).willReturn("/user/list");
        given(resourceLoader.fileToBytes("/user/list", true)).willReturn(template);
        given(session.getAttribute(SESSION_USER)).willReturn(user);
        given(renderer.render(user, template)).willReturn(rendered);

        // when
        Response response = handler.handle(request);

        // then
        assertThat(response.getHttpStatus()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(rendered);
    }

    @Test
    @DisplayName("인증이 필요하지 않은 페이지는 로그인하지 않은 사용자도 접근할 수 있다.")
    void handleNotRequireAuthTest() throws Exception {
        // given
        TemplateHandler handler = new TemplateHandler(resourceLoader, renderer, false);
        byte[] template = "template".getBytes();
        byte[] rendered = "rendered".getBytes();

        given(request.getHeaders()).willReturn(new HashMap<>());
        given(request.getRequestUrl()).willReturn("/");
        given(resourceLoader.fileToBytes("/", true)).willReturn(template);
        given(renderer.render(null, template)).willReturn(rendered);

        // when
        Response response = handler.handle(request);

        // then
        assertThat(response.getHttpStatus()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(rendered);
    }
}