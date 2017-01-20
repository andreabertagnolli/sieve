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

    public <T> NestedReference<T, N> execute(SieveValidator<N> validator) {
        this.validators.addAll(validator.getValidators());
        return (NestedReference<T, N>) this;
    }

    public static <T, N> NestedReference<T, N> on(Function<T, List<N>> getNested) {
        return new NestedReference<>(getNested);
    }

    public List<N> getObjects(T object) {
        return getNested.apply(object);
    }

    public List<PredicateValidator<N>> getValidators() {
        return validators;
    }
}
