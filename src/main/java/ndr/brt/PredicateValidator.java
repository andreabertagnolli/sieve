package ndr.brt;

import java.lang.reflect.Field;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Stream;

public class PredicateValidator<T> {

    private final Function<T, Boolean> predicate;
    private final String code;
    private final String description;

    private PredicateValidator(String code, String description, Function<T, Boolean> predicate) {
        this.code = code;
        this.description = description;
        this.predicate = predicate;
    }

    public Stream<Result> validate(T object) {
        return Stream.of(object)
                .map(predicate)
                .map(Result::new)
                .map(setResultMessage(object));
    }

    private Function<Result, Result> setResultMessage(T object) {
        return r -> r.withMessage(code + ": " + descriptionWith(object));
    }

    private String descriptionWith(T object) {
        StringBuilder stringBuilder = new StringBuilder();
        String remainDescription = description;
        while (remainDescription.contains("{{")) {
            int start = remainDescription.indexOf("{{");
            int end = remainDescription.indexOf("}}");
            stringBuilder.append(remainDescription.substring(0, start));
            String fieldName = remainDescription.substring(start + 2, end);
            remainDescription = remainDescription.substring(end + 2);
            String fieldValue = getFieldValue(object, fieldName);
            stringBuilder.append(fieldValue);
        }
        stringBuilder.append(remainDescription);
        return stringBuilder.toString();
    }

    private String getFieldValue(T object, String fieldName) {
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

    public static <T> PredicateValidatorBuilder<T> okWhen(Function<T, Boolean> predicate) {
        return new PredicateValidatorBuilder<T>(predicate);
    }

    public static class PredicateValidatorBuilder<T> {

        private final Function<T, Boolean> predicate;

        PredicateValidatorBuilder(Function<T, Boolean> predicate) {
            this.predicate = predicate;
        }

        public PredicateValidator<T> returns(String code, String description) {
            return new PredicateValidator<T>(code, description, predicate);
        }
    }
}
