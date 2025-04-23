package template;

import static org.assertj.core.api.Assertions.assertThat;

import java.nio.charset.StandardCharsets;
import model.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class HeaderRendererTest {
    private final HeaderRenderer headerRenderer = new HeaderRenderer();

    @Test
    @DisplayName("로그인하지 않으면 로그인버튼과 회원가입 버튼을 보여준다.")
    void renderNotLoginTest() {
        //given
        byte[] body = "{{header}}".getBytes();

        //when
        byte[] bytes = headerRenderer.render(null, body);
        String html = new String(bytes, StandardCharsets.UTF_8);

        //then
        assertThat(html).contains("로그인");
        assertThat(html).contains("회원 가입");
        assertThat(html).doesNotContain("로그아웃");
    }

    @Test
    @DisplayName("로그인하면 로그아웃버튼과 이름을 보여준다.")
    void renderLoginTest() {
        //given
        User mockUser = new User("testId", "password", "testName", "test@example.com");
        byte[] body = "{{header}}".getBytes();

        //when
        byte[] bytes = headerRenderer.render(mockUser, body);
        String html = new String(bytes, StandardCharsets.UTF_8);

        //then
        assertThat(html).contains("로그아웃");
        assertThat(html).contains("testName");
        assertThat(html).doesNotContain("로그인");
    }
} 