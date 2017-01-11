package ndr.brt;

import java.util.function.Function;
import java.util.stream.Stream;

public abstract class PredicateValidator<T> {

    protected abstract Function<T, Boolean> predicate();

    public Stream<Result> validate(T object) {
        return Stream.of(object)
                .map(predicate())
                .map(Result::new);
    }

}
