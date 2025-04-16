package handler.errorHandler;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static webserver.common.Constants.EMPTY;

import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import webserver.common.HttpStatus;
import webserver.loader.ResourceLoader;
import webserver.request.Request;
import webserver.response.Response;

class MethodNotAllowedHandlerTest {
    @Test
    @DisplayName("지원하지 않는 메소드에 대해 405 error 페이지와 함께 Method Not Allowed를 응답한다.")
    void testHandlerReturns405Response() {
        // given
        byte[] expectedBody = "<html><body>405 Method Not Allowed</body></html>".getBytes(StandardCharsets.UTF_8);

        // mock ResourceLoader
        ResourceLoader resourceLoader = Mockito.mock(ResourceLoader.class);
        Mockito.when(resourceLoader.fileToBytes("/error/405.html")).thenReturn(expectedBody);

        MethodNotAllowedHandler handler = new MethodNotAllowedHandler(resourceLoader);
        Request mockRequest = Mockito.mock(Request.class);

        // when
        Response response = handler.handle(mockRequest);

        // then
        assertThat(response.getHttpStatus()).isEqualTo(HttpStatus.NOT_ALLOWED);
        assertThat(response.getBody()).isEqualTo(expectedBody);
        assertThat(response.getRedirectPath()).isEqualTo(EMPTY);
    }

}