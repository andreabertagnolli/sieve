package ndr.brt.sieve;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class NestedReference<T, N> {

    private final Function<T, List<N>> getNested;
    private List<PredicateValidator<N>> validators = new ArrayList<>();

    public NestedReference(Function<T, List<N>> getNested) {
        this.getNested = getNested;
    }

    public static <T, N> NestedReference on(Function<T, List<N>> getNested) {
        return new NestedReference<>(getNested);
    }

    public NestedReference<T, N> execute(List<PredicateValidator<N>> nestedValidators) {
        validators.addAll(nestedValidators);
        return this;
    }

    public List<N> getObjects(T object) {
        return getNested.apply(object);
    }

    public List<PredicateValidator<N>> getValidators() {
        return validators;
    }
}
