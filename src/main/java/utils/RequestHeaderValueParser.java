package utils;

public class RequestHeaderValueParser {
    public static String[] parseKeyValuePairs(String headerValue, String delimiter) {
        String[] parts = headerValue.split(delimiter);
        for(int i = 0; i < parts.length; i++) {
            parts[i] = parts[i].trim();
        }
        return parts;
    }

    public static String getValueByKey(String[] keyValuePairs, String key, String delimiter) {
        for (String pair : keyValuePairs) {
            String[] parts = pair.split(delimiter, 2);
            if (parts.length == 2 && parts[0].equalsIgnoreCase(key)) {
                return parts[1];
            }
        }
        return null;
    }
}
