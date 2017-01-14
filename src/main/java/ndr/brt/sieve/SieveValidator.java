package ndr.brt.sieve;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Stream.concat;

public class SieveValidator<T> {

    private List<PredicateValidator<T>> validators = new ArrayList<>();
    private List<NestedReference<T, ?>> nestedReferences = new ArrayList<>();

    public static <T> SieveValidator<T> validator() {
        return new SieveValidator<>();
    }

    public Stream<Bran> validate(T object) {
        return validateStream(Stream.of(object));
    }

    public Stream<Bran> validate(Collection<T> objects) {
        return validateStream(objects.stream());
    }

    private Stream<Bran> validateStream(Stream<T> stream) {
        List<T> objects = stream.collect(toList());

        return concat(
                objects.stream().
                        map(this::validateTLevel)
                        .flatMap(e -> e),
                objects.stream()
                        .map(this::validateNested)
                        .flatMap(e -> e)
        );
    }

    private Stream<Bran> validateTLevel(T object) {
        return validators.stream()
                .map(v -> v.validate(object))
                .flatMap(e -> e);
    }

    private Stream<Bran> validateNested(T object) {
        List<Bran> result = new ArrayList<>();
        for (NestedReference<T, ?> nestedReference : nestedReferences) {
            Stream<Bran> branStream = nestedReference.getValidators().stream()
                    .map(v -> v.validate((List) nestedReference.getObjects(object)))
                    .flatMap(e -> e)
                    .map(Bran.class::cast);
            result.addAll(branStream.collect(toList()));
        }
        return result.stream();
    }

    public SieveValidator<T> with(PredicateValidator<T> validator) {
        this.validators.add(validator);
        return this;
    }

    public SieveValidator<T> with(NestedReference<T, ?> nestedReference) {
        this.nestedReferences.add(nestedReference);
        return this;
    }

}
