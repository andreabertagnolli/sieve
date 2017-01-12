package ndr.brt;

import java.util.function.Function;
import java.util.stream.Stream;

import static ndr.brt.FieldPlaceholder.fieldPlaceholder;

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
        return r -> r.withMessage(code + ": " + fieldPlaceholder(object).apply(description));
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
