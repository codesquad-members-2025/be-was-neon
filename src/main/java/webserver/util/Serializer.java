package webserver.util;

import java.lang.reflect.Array;
import java.lang.reflect.Field;

public class Serializer {

    // RFC 8259 (JSON 데이터 interchange format)에서도 모든 키와 문자열 값은 반드시 큰따옴표로 감싸야 한다고 명시하고 있습니다.
    public static String toJson(Object obj) throws IllegalAccessException {
        if (obj == null) {
            return "null";
        }

        Class<?> clazz = obj.getClass();

        // String 처리: 따옴표와 내부 따옴표 이스케이프 처리
        if (clazz == String.class) {
            return "\"" + escape((String) obj) + "\"";
        }

        if (clazz == Integer.class || clazz == Long.class || clazz == Double.class ||
                clazz == Float.class || clazz == Boolean.class || Number.class.isAssignableFrom(clazz)) {
            return obj.toString();
        }

        // 배열 처리
        if (clazz.isArray()) {
            StringBuilder sb = new StringBuilder();
            sb.append("[");
            int length = Array.getLength(obj);
            for (int i = 0; i < length; i++) {
                Object element = Array.get(obj, i);
                sb.append(toJson(element));
                if (i < length - 1) {
                    sb.append(",");
                }
            }
            sb.append("]");
            return sb.toString();
        }

        // 객체(빈 객체 또는 POJO) 처리: 모든 필드를 JSON 객체로 변환
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        Field[] fields = clazz.getDeclaredFields();
        boolean first = true;
        for (Field field : fields) {
            field.setAccessible(true);
            Object value = field.get(obj);
            if (!first) {
                sb.append(",");
            }
            sb.append("\"").append(field.getName()).append("\":");
            sb.append(toJson(value));
            first = false;
        }
        sb.append("}");
        return sb.toString();
    }

    private static String escape(String s) {
        return s.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }

}
