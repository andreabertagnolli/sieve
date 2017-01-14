package ndr.brt.sieve;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

public class SieveValidator<T> {

    private List<NestedReference<T, ?>> nestedReferences = new ArrayList<>();

    public static <T> SieveValidator<T> validator() {
        return new SieveValidator<>();
    }

    public Stream<Bran> validate(T object) {
        return Stream.of(object)
                .map(this::validateNested)
                .flatMap(e -> e);
    }

    public Stream<Bran> validate(Collection<T> object) {
        return object.stream()
                .map(this::validateNested)
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

    public SieveValidator<T> with(NestedReference<T, ?> nestedReference) {
        this.nestedReferences.add(nestedReference);
        return this;
    }

}
