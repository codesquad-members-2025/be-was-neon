package template;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mockStatic;

import db.Database;
import java.nio.charset.StandardCharsets;
import java.util.List;
import model.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

class UserListRendererTest {
    private final UserListRenderer userListRenderer = new UserListRenderer();

    @Test
    @DisplayName("유저 리스트를 테이블로 렌더링한다.")
    void renderUserListTest() {
        // given
        User user1 = new User("testId1", "password", "testName1", "test@example.com");
        User user2 = new User("testId2", "password", "testName2", "test@example.com");
        byte[] body = "{{userList}}".getBytes();

        try (MockedStatic<Database> mockedDatabase = mockStatic(Database.class)) {
            mockedDatabase.when(Database::findAll).thenReturn(List.of(user1, user2));

            // when
            byte[] result = userListRenderer.render(null, body);
            String html = new String(result, StandardCharsets.UTF_8);

            // then
            assertThat(html).contains("<td>testId1</td>");
            assertThat(html).contains("<td>testName1</td>");
            assertThat(html).contains("<td>testId2</td>");
            assertThat(html).contains("<td>testName2</td>");
        }
    }
} 