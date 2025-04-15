package webserver.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class SerializerTest {

    static class TestObject {
        String name;
        int age;

        TestObject(String name, int age) {
            this.name = name;
            this.age = age;
        }
    }

    @Test
    @DisplayName("문자열 객체가 JSON 문자열로 올바르게 변환되어야 한다")
    void 문자열_JSON_변환_테스트() throws IllegalAccessException {
        // given
        String givenString = "안녕하세요\"테스트\"";

        // when
        String result = Serializer.toJson(givenString);

        // then
        assertThat(result).isEqualTo("\"안녕하세요\\\"테스트\\\"\"");
    }

    @Test
    @DisplayName("숫자 객체가 JSON 숫자 문자열로 올바르게 변환되어야 한다")
    void 숫자_JSON_변환_테스트() throws IllegalAccessException {
        // given
        int givenNumber = 123;

        // when
        String result = Serializer.toJson(givenNumber);

        // then
        assertThat(result).isEqualTo("123");
    }

    @Test
    @DisplayName("boolean 값이 JSON 문자열로 올바르게 변환되어야 한다")
    void boolean_JSON_변환_테스트() throws IllegalAccessException {
        // given
        boolean givenBoolean = true;

        // when
        String result = Serializer.toJson(givenBoolean);

        // then
        assertThat(result).isEqualTo("true");
    }

    @Test
    @DisplayName("객체 배열이 JSON 배열 문자열로 올바르게 변환되어야 한다")
    void 객체배열_JSON_변환_테스트() throws IllegalAccessException {
        // given
        String[] givenArray = {"가", "나", "다"};

        // when
        String result = Serializer.toJson(givenArray);

        // then
        assertThat(result).isEqualTo("[\"가\",\"나\",\"다\"]");
    }

    @Test
    @DisplayName("사용자 정의 객체가 JSON 객체 문자열로 올바르게 변환되어야 한다")
    void 사용자정의객체_JSON_변환_테스트() throws IllegalAccessException {
        // given
        TestObject givenObject = new TestObject("홍길동", 30);

        // when
        String result = Serializer.toJson(givenObject);

        // then
        assertThat(result).isEqualTo("{\"name\":\"홍길동\",\"age\":30}");
    }

    @Test
    @DisplayName("null 값이 'null' 문자열로 올바르게 변환되어야 한다")
    void null_JSON_변환_테스트() throws IllegalAccessException {
        // given
        Object givenNull = null;

        // when
        String result = Serializer.toJson(givenNull);

        // then
        assertThat(result).isEqualTo("null");
    }

}
