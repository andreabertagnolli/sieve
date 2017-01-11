package ndr.brt;

import java.lang.reflect.Field;
import java.util.Objects;

public class FieldPlaceholder {

    private final Object object;

    public FieldPlaceholder(Object object) {
        this.object = object;
    }

    public String substituteOn(String description) {
        StringBuilder stringBuilder = new StringBuilder();
        String remainDescription = description;
        while (remainDescription.contains("{{")) {
            int start = remainDescription.indexOf("{{");
            int end = remainDescription.indexOf("}}");

            stringBuilder.append(remainDescription.substring(0, start));
            String fieldName = remainDescription.substring(start + 2, end);
            remainDescription = remainDescription.substring(end + 2);

            stringBuilder.append(getFieldValue(object, fieldName));
        }
        stringBuilder.append(remainDescription);
        return stringBuilder.toString();
    }

    private String getFieldValue(Object object, String fieldName) {
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
