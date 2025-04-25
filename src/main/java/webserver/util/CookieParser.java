package webserver.util;

public class CookieParser {
    public static String getCookieValue(String cookieHeader, String key) {
        if (cookieHeader == null || cookieHeader.isEmpty()) {
            return null;
        }
        String[] pairs = cookieHeader.split(":");
        for (String pair : pairs) {
            String[] kv = pair.trim().split("=",2);
            if (kv[0].trim().equals(key)) {
                return kv[1].trim();
            }
        }
        return null;
    }
    //todo 전체 쿠키릂 파싱해서 map으로 리턴하는것도 필요할까?
}
