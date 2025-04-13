package exception;

public class ServerException extends IllegalArgumentException{

    private final int statusCode;

    public ServerException() {
        super();
        this.statusCode=0;
    }

    public ServerException(String s,int statusCode) {
        super(s);
        this.statusCode=statusCode;
    }

    public ServerException(String message, Throwable cause,int statusCode) {
        super(message, cause);
        this.statusCode=statusCode;
    }

    public ServerException(Throwable cause,int statusCode) {
        super(cause);
        this.statusCode=statusCode;
    }

    public int getStatusCode() {
        return statusCode;
    }
}
