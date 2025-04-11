package webserver.util;

public class Convertor {

    private Convertor() {
    }

    public static long convertToLong(String value) {
        try {
            return Long.parseLong(value);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid number format: " + value, e);
        }
    }

    public static int convertToIntByHex(String value) {
        try {
            return Integer.parseInt(value.trim(), 16);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid number format: " + value, e);
        }
    }

}
