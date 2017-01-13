package ndr.brt.sieve;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

public class NestedValidator<T> {

    private List<NestedReference<T, ?>> nestedReferences = new ArrayList<>();

    public NestedValidator() {

    }

    public static <T> NestedValidator<T> nestedValidator() {
        return new NestedValidator<>();
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
            List nesteds = nestedReference.getObjects(object);
            for (PredicateValidator<?> validator : nestedReference.getValidators()) {
                List<Bran> collect = (List<Bran>) validator.validate(nesteds).collect(toList());
                result.addAll(collect);
            }
        }
        return result.stream();
    }

    public NestedValidator<T> with(NestedReference<T, ?> nestedReference) {
        this.nestedReferences.add(nestedReference);
        return this;
    }

}
