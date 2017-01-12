package ndr.brt.sieve;

import java.lang.reflect.Field;
import java.util.Objects;

public class FieldPlaceholder {

    private static final String PLACEHOLDER_OPEN = "{{";
    private static final String PLACEHOLDER_CLOSE = "}}";

    public static String substitutePlaceholders(String string, Object object) {
        StringBuilder actual = new StringBuilder();
        String left = string;
        while (left.contains(PLACEHOLDER_OPEN)) {
            int start = left.indexOf(PLACEHOLDER_OPEN);
            int end = left.indexOf(PLACEHOLDER_CLOSE);

            actual.append(left.substring(0, start));
            String fieldName = left.substring(start + PLACEHOLDER_OPEN.length(), end);

            left = left.substring(end + PLACEHOLDER_CLOSE.length());
            actual.append(getFieldValue(object, fieldName));
        }
        actual.append(left);
        return actual.toString();
    }

    private static String getFieldValue(Object object, String fieldName) {
        try {
            Class<?> clazz = object.getClass();
            Field field = clazz.getDeclaredField(fieldName);
            field.setAccessible(true);
            return Objects.toString(field.get(object));
        } catch (Exception e) {
            return "_error_";
        }
    }
}
