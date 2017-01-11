package ndr.brt;

import java.util.stream.Stream;

public class PredicateValidator<T> {

    public Stream<Result> validate(T object) {
        return Stream.of(new Result());
    }

}
