package webserver.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class QueryStringParserTest {

    @ParameterizedTest
    @CsvSource({
            "name=John&age=30, name, John, age, 30",
            "param=value, param, value, , ",
            "key=value&key2=value2, key, value, key2, value2",
            "item=, item, '', , "
    })
    @DisplayName("쿼리파람 있는 올바른 메서드, URI, 프로토콜 및 헤더를 추출해야 한다.")
    void 정상_쿼리파람_요청라인_파싱_테스트(String queryString, String key1, String value1, String key2, String value2) {
        // When
        Map<String, String> params = QueryStringParser.parse(queryString);

        // Then
        assertThat(params).containsEntry(key1, value1);
        if (key2 != null && !key2.isEmpty()) {
            assertThat(params).containsEntry(key2, value2);
        }
    }

}
