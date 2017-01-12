package ndr.brt;

import java.lang.reflect.Field;
import java.util.Objects;
import java.util.function.Function;

public class FieldPlaceholder {

    public static Function<String, String> fieldPlaceholder(Object object) {
        return string -> {
            StringBuilder actual = new StringBuilder();
            String left = string;
            while (left.contains("{{")) {
                int start = left.indexOf("{{");
                int end = left.indexOf("}}");

                actual.append(left.substring(0, start));
                String fieldName = left.substring(start + 2, end);
                left = left.substring(end + 2);

                actual.append(getFieldValue(object, fieldName));
            }
            actual.append(left);
            return actual.toString();
        };
    }

    private static String getFieldValue(Object object, String fieldName) {
        Class<?> clazz = object.getClass();
        String fieldValue = "";
        try {
            Field field = clazz.getDeclaredField(fieldName);
            field.setAccessible(true);
            fieldValue = Objects.toString(field.get(object));
        } catch (Exception e) {
            fieldValue = "_error_";
        }
        return fieldValue;
    }
}
