package ndr.brt.sieve;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Objects;

public class ReflectionExpression {
    private final Object object;
    private final String expression;

    public ReflectionExpression(Object object, String expression) {
        this.object = object;
        this.expression = expression;
    }

    public String getValue() {
        try {
            if (!expression.contains(".")) {
                Field field = getField(object.getClass(), expression);
                return Objects.toString(field.get(object));
            }
            else {
                String[] split = expression.split("\\.");
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
