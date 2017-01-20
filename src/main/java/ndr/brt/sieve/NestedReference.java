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

    public NestedReference(Function<T, List<N>> nested, SieveValidator<N> validator) {
        this.getNested = nested;
        this.validators.addAll(validator.getValidators());
    }

    public List<N> getObjects(T object) {
        return getNested.apply(object);
    }

    public List<PredicateValidator<N>> getValidators() {
        return validators;
    }
}
