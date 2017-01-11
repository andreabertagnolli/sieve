package ndr.brt;

import org.junit.Before;
import org.junit.Test;

import java.util.function.Function;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertTrue;

public class PredicateValidatorTest {

    private static final String CODE = "CODE";
    private static final String DESCRIPTION = "description";
    private PredicateValidator<String> validator;

    @Before
    public void setUp() throws Exception {
        validator = new StubValidator();
    }

    @Test
    public void returns_one_result_for_object() throws Exception {
        Stream<Result> result = validator.validate("any");

        assertThat(result.count()).isEqualTo(1L);
    }

    @Test
    public void when_validation_is_ok_returns_ok() throws Exception {
        Result result = validator
                .validate("right")
                .findFirst().get();

        assertTrue(result.isOk());
    }

    @Test
    public void when_validation_is_wrong_returns_error() throws Exception {
        Result result = validator
                .validate("wrong")
                .findFirst().get();

        assertTrue(result.isError());
        assertThat(result.getMessage()).isEqualTo("CODE: description");
    }

    private class StubValidator extends PredicateValidator<String> {

        @Override
        protected Function<String, Boolean> predicate() {
            return "right"::equals;
        }

        @Override
        protected String code() {
            return CODE;
        }

        @Override
        protected String description() {
            return DESCRIPTION;
        }
    }
}
