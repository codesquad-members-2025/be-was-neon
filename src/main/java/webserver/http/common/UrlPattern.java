package webserver.http.common;

public enum UrlPattern {

    CREATE_USER("/create/user");

    private String pattern;

    UrlPattern(String pattern) {
        this.pattern = pattern;
    }

    public String getPattern() {
        return pattern;
    }

    public static boolean contain(String path) {
        for (UrlPattern urlPattern : UrlPattern.values()) {
            String pattern = urlPattern.getPattern();
            return pattern.equals(path);
        }

        return false;
    }
}
