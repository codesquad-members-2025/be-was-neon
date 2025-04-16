package webserver.request;

import model.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

public class RequestParserTest {

    @Test
    @DisplayName("회원가입 할 때 입력한 정보 그대로 User 객체가 만들어지는지 테스트")
    void registrationParsingTest() {
        String uri = "/create?userId=hawoon724&name=윤하운&password=2222&email=hawoon724%40naver.com";

        RequestParser requestParser = new RequestParser();
        User user = requestParser.parseRegistrationData(uri);

        assertThat(user.getUserId()).isEqualTo("hawoon724");
        assertThat(user.getName()).isEqualTo("윤하운");
        assertThat(user.getPassword()).isEqualTo("2222");
        assertThat(user.getEmail()).isEqualTo("hawoon724@naver.com");
    }
}
