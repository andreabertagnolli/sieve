package ndr.brt.sieve;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

import static java.util.Collections.singletonList;
import static java.util.function.Function.identity;
import static java.util.stream.Stream.concat;

public class SieveValidator<T> {

    private List<PredicateValidator<T>> validators = new ArrayList<>();
    private List<NestedReference<T, ?>> nestedReferences = new ArrayList<>();

    public static <T> SieveValidator<T> validator() {
        return new SieveValidator<>();
    }

    public Stream<Bran> validate(T object) {
        return validate(singletonList(object));
    }

    public Stream<Bran> validate(Collection<T> objects) {
        return concat(
                objects.stream()
                        .map(this::validateRoot)
                        .flatMap(identity()),
                objects.stream()
                        .map(this::validateNested)
                        .flatMap(identity())
        );
    }

    private Stream<Bran> validateRoot(T object) {
        return validators.stream()
                .map(v -> v.validate(object))
                .flatMap(identity());
    }

    private Stream<Bran> validateNested(T object) {
        return nestedReferences.stream()
                .map(n -> n.getValidators().stream()
                    .map(v -> v.validate((List) n.getObjects(object)))
                    .flatMap(identity())
                    .map(Bran.class::cast)
                )
                .flatMap(e -> e);
    }

    public SieveValidator<T> with(PredicateValidator<T> validator) {
        this.validators.add(validator);
        return this;
    }

    public <N> SieveValidator<T> with(Function<T, List<N>> getNested, SieveValidator<N> validator) {
        this.nestedReferences.add(new NestedReference<>(getNested, validator));
        return this;
    }

    List<PredicateValidator<T>> getValidators() {
        return validators;
    }

    private static class NestedReference<T, N> {

        private final Function<T, List<N>> getNested;
        private List<PredicateValidator<N>> validators = new ArrayList<>();

        NestedReference(Function<T, List<N>> nested, SieveValidator<N> validator) {
            this.getNested = nested;
            this.validators.addAll(validator.getValidators());
        }

        List<N> getObjects(T object) {
            return getNested.apply(object);
        }

        List<PredicateValidator<N>> getValidators() {
            return validators;
        }
    }
}
