package exception;

public class ClientException extends IllegalArgumentException {

    private final int statusCode; // statusCode 필드 추가

    // 기본 생성자 (statusCode를 기본값으로 설정)
    public ClientException() {
        super();
        this.statusCode = 0; // 기본값 설정
    }

    // message와 statusCode를 받는 생성자
    public ClientException(String message, int statusCode) {
        super(message);
        this.statusCode = statusCode;
    }

    // message, cause와 statusCode를 받는 생성자
    public ClientException(String message, Throwable cause, int statusCode) {
        super(message, cause);
        this.statusCode = statusCode;
    }

    // cause와 statusCode를 받는 생성자
    public ClientException(Throwable cause, int statusCode) {
        super(cause);
        this.statusCode = statusCode;
    }

    // statusCode만 받는 생성자
    public ClientException(int statusCode) {
        super();
        this.statusCode = statusCode;
    }

    // statusCode 값을 반환하는 getter 메서드
    public int getStatusCode() {
        return statusCode;
    }
}
