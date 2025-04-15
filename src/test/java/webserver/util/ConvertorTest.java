package webserver.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ConvertorTest {

    static class TestObject {
        String name;
        int age;

        TestObject(String name, int age) {
            this.name = name;
            this.age = age;
        }
    }

    @Test
    @DisplayName("문자열을 바이트 배열로 올바르게 변환되어야 한다")
    void 문자열을_바이트배열로_변환_테스트() {
        // given
        String givenString = "hello";

        // when
        byte[] result = Convertor.convertToByteArray(givenString);

        // then
        assertThat(result).isEqualTo("hello".getBytes());
    }

    @Test
    @DisplayName("바이트 배열이 그대로 반환되어야 한다")
    void 바이트배열_그대로반환_테스트() {
        // given
        byte[] givenBytes = {1, 2, 3};

        // when
        byte[] result = Convertor.convertToByteArray(givenBytes);

        // then
        assertThat(result).isSameAs(givenBytes);
    }

    @Test
    @DisplayName("사용자 정의 객체가 바이트 배열(JSON 형태)로 변환되어야 한다")
    void 객체_JSON_바이트배열_변환_테스트() {
        // given
        TestObject givenObject = new TestObject("이몽룡", 25);

        // when
        byte[] result = Convertor.convertToByteArray(givenObject);

        // then
        assertThat(new String(result)).isEqualTo("{\"name\":\"이몽룡\",\"age\":25}");
    }

}
