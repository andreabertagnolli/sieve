package ndr.brt.sieve;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Objects;

public class FieldPlaceholder {

    private static final String PLACEHOLDER_OPEN = "{{";
    private static final String PLACEHOLDER_CLOSE = "}}";

    public static String  substitutePlaceholders(String string, Object object) {
        StringBuilder actual = new StringBuilder();
        String left = string;
        while (left.contains(PLACEHOLDER_OPEN)) {
            int start = left.indexOf(PLACEHOLDER_OPEN);
            int end = left.indexOf(PLACEHOLDER_CLOSE);

            actual.append(left.substring(0, start));
            String placeholder = left.substring(start + PLACEHOLDER_OPEN.length(), end);

            left = left.substring(end + PLACEHOLDER_CLOSE.length());
            actual.append(getPlaceholderValue(object, placeholder));
        }
        actual.append(left);
        return actual.toString();
    }

    private static String getPlaceholderValue(Object object, String placeholder) {
        try {
            if (!placeholder.contains(".")) {
                Field field = getField(object.getClass(), placeholder);
                field.setAccessible(true);
                return Objects.toString(field.get(object));
            }
            else {
                String[] split = placeholder.split("\\.");
                Field field = getField(object.getClass(), split[0]);
                field.setAccessible(true);
                Class<?> declaringClass = field.getType();
                Method method = declaringClass.getDeclaredMethod(split[1].substring(0, split[1].indexOf("(")));
                method.setAccessible(true);
                return String.valueOf(method.invoke(field.get(object)));
            }
        } catch (Exception e) {
            return "_error_";
        }
    }

    private static Field getField(Class<?> clazz, String fieldName) {
        try {
            return clazz.getDeclaredField(fieldName);
        } catch (Exception e) {
            return getField(clazz.getSuperclass(), fieldName);
        }
    }
}
