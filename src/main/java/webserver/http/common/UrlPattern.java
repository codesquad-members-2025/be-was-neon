package webserver.http.common;

public enum UrlPattern {

    USER_CREATE("/user/create"),
    USER_LOGIN("/user/login");

    private final String pattern;

    UrlPattern(String pattern) {
        this.pattern = pattern;
    }

    public String getPattern() {
        return pattern;
    }

    public static boolean contain(String path) {
        boolean result;
        for (UrlPattern urlPattern : UrlPattern.values()) {
            String pattern = urlPattern.getPattern();
            result = pattern.equals(path);
            if (result) return true;
        }

        return false;
    }
}
