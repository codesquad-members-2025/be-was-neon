package webserver.util;

import java.util.Objects;

public class Route {
    private final String method;
    private final String path;

    public Route (String method, String path) {
        this.method = method;
        this.path = path;
    }

    @Override
    public boolean equals(Object obj) { //기본 equals()는 메모리 주소(==)가 같으면 true 를 리턴
        if (this == obj) return true;
        if (!(obj instanceof Route that)) return false; //타입 체크와 동시에 변수 선언
        return method.equals(that.method) && path.equals(that.path);
    }

    @Override
    public int hashCode() {
        return Objects.hash(method, path);
    }
}
