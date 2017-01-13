package ndr.brt.sieve;

import java.util.Collection;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class PredicateValidator<T> {

    private final Predicate<T> predicate;
    private final String code;
    private final String description;

    private PredicateValidator(String code, String description, Predicate<T> predicate) {
        this.code = code;
        this.description = description;
        this.predicate = predicate;
    }

    public Stream<Bran> validate(T object) {
        return validate(Stream.of(object));
    }

    public Stream<Bran> validate(Collection<T> collection) {
        return validate(collection.stream());
    }

    private Stream<Bran> validate(Stream<T> stream) {
        return stream
                .filter(predicate)
                .map(o -> new Bran(o, code, description));
    }

    public static <T> PredicateValidatorBuilder<T> when(Predicate<T> predicate) {
        return new PredicateValidatorBuilder<T>(predicate);
    }

    public static class PredicateValidatorBuilder<T> {

        private final Predicate<T> predicate;

        PredicateValidatorBuilder(Predicate<T> predicate) {
            this.predicate = predicate;
        }

        public PredicateValidator<T> returns(String code, String description) {
            return new PredicateValidator<T>(code, description, predicate);
        }
    }
}
