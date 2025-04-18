package util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import webserver.RequestHandler;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

public class ParserTest {

    @Test
    @DisplayName("create요청 파싱 후 DB에 저장되는지 확인한다")
    void parseRequestParam(){
        // given
        /*
        String request = "GET /create?userId=test123&name=홍길동&email=test%40email.com&password=1234 HTTP/1.1\r\n" +
                "Host: localhost:8080\r\n" +
                "\r\n";
        InputStream in = new ByteArrayInputStream(request.getBytes());
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        RequestHandler handler = new RequestHandler(in, out);

        // when
        handler.run();

        // then
        User saved = Database.findUserById("test123");
        assertThat(saved).isNotNull();
        assertThat(saved.getName()).isEqualTo("홍길동");
        assertThat(saved.getEmail()).isEqualTo("test@email.com");
        assertThat(saved.getPassword()).isEqualTo("1234");

        String response = out.toString();
        assertThat(response).contains("HTTP/1.1 200 OK");
        assertThat(response).contains("hello"); // 간단한 응답 본문 체크
         */
    }
}
