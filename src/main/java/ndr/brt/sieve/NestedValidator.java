package ndr.brt.sieve;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

import static java.util.Collections.singletonList;

public class NestedValidator<T, N> {

    private final Function<T, List<N>> getNested;
    private final List<PredicateValidator<N>> validators;

    public NestedValidator(Function<T, List<N>> getNested, List<PredicateValidator<N>> validators) {
        this.getNested = getNested;
        this.validators = validators;
    }

    public static <T, N> NestedValidatorBuilder<T, N> nestedValidator() {
        return new NestedValidatorBuilder<T, N>();
    }

    public Stream<Bran> validate(T object) {
        return validators.stream()
                .map(v -> v.validate(getNested.apply(object)))
                .flatMap(e -> e);
    }

    public static class NestedValidatorBuilder<T, N> {
        private Function<T, List<N>> getNested;

        public NestedValidatorBuilder<T, N> on(Function<T, List<N>> getNested) {
            this.getNested = getNested;
            return this;
        }

        public NestedValidator<T, N> use(PredicateValidator<N> validator) {
            return new NestedValidator<T, N>(getNested, singletonList(validator));
        }

        public NestedValidator<T, N> use(List<PredicateValidator<N>> validators) {
            return new NestedValidator<T, N>(getNested, validators);
        }
    }
}
