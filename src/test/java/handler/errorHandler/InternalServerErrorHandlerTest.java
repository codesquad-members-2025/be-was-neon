package handler.errorHandler;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static webserver.common.Constants.EMPTY;

import handler.Handler;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import webserver.common.HttpStatus;
import webserver.loader.ResourceLoader;
import webserver.request.Request;
import webserver.response.Response;

class InternalServerErrorHandlerTest {
    @Test
    @DisplayName("500 Internal Server Error 페이지를 반환해야 한다.")
    void handle_ReturnsInternalServerErrorPage() {
        // given
        ResourceLoader mockLoader = mock(ResourceLoader.class);
        byte[] expectedBody = "<html><body>500 Error</body></html>".getBytes();
        when(mockLoader.fileToBytes("/error/500.html", false)).thenReturn(expectedBody);

        Handler handler = new InternalServerErrorHandler(mockLoader);
        Request mockRequest = mock(Request.class);

        // when
        Response response = handler.handle(mockRequest);

        // then
        assertThat(response.getHttpStatus()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(response.getBody()).isEqualTo(expectedBody);
        assertThat(response.getRedirectPath()).isEqualTo(EMPTY);
    }

}