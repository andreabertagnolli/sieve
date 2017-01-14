package ndr.brt.sieve;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Objects;

public class Placeholder {
    private final Object object;
    private final String placeholder;

    public Placeholder(Object object, String placeholder) {
        this.object = object;
        this.placeholder = placeholder;
    }

    public String getValue() {
        try {
            if (!placeholder.contains(".")) {
                Field field = getField(object.getClass(), placeholder);
                return Objects.toString(field.get(object));
            }
            else {
                String[] split = placeholder.split("\\.");
                Field field = getField(object.getClass(), split[0]);
                Class<?> type = field.getType();
                String methodName = split[1].substring(0, split[1].indexOf("("));
                Method method = type.getDeclaredMethod(methodName);
                method.setAccessible(true);
                return String.valueOf(method.invoke(field.get(object)));
            }
        } catch (Exception e) {
            return "_error_";
        }
    }

    private Field getField(Class<?> clazz, String fieldName) {
        try {
            Field field = clazz.getDeclaredField(fieldName);
            field.setAccessible(true);
            return field;
        } catch (Exception e) {
            return getField(clazz.getSuperclass(), fieldName);
        }
    }
}
