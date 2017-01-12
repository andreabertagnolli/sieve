package ndr.brt.sieve;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class ListValidator<T> {

    private final String code;
    private final String description;
    private final Predicate<T> predicate;

    public static <T> ListValidatorBuilder okWhen(Predicate<T> predicate) {
        return new ListValidatorBuilder<T>(predicate);
    }

    public ListValidator(String code, String description, Predicate<T> predicate) {
        this.code = code;
        this.description = description;
        this.predicate = predicate;
    }

    public Stream<Result> validate(List<T> objects) {
        return objects.stream()
                .filter(predicate.negate())
                .map(o -> new Result(o, code, description));
    }

    public static class ListValidatorBuilder<T> {
        private final Predicate<T> predicate;

        public ListValidatorBuilder(Predicate<T> predicate) {
            this.predicate = predicate;
        }

        public ListValidator<T> returns(String code, String description) {
            return new ListValidator<T>(code, description, predicate);
        }
    }
}
