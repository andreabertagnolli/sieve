package ndr.brt.sieve;

import org.junit.Before;
import org.junit.Test;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

public class PredicateValidatorTest {

    private static final String CODE = "CODE";
    private static final String DESCRIPTION = "description";
    private PredicateValidator<String> validator;

    @Before
    public void setUp() throws Exception {
        validator = PredicateValidator
                .<String>when(s -> !"right".equals(s)).returns(CODE, DESCRIPTION);
    }

    @Test
    public void returns_one_result_for_object() throws Exception {
        Stream<Bran> result = validator.validate("any");

        assertThat(result.count()).isEqualTo(1L);
    }

    @Test
    public void when_validation_is_ok_returns_nothing() throws Exception {
        Long count = validator
                .validate("right")
                .count();

        assertThat(count).isEqualTo(0L);
    }

    @Test
    public void when_validation_is_wrong_returns_error_with_message() throws Exception {
        Bran bran = validator
                .validate("wrong")
                .findFirst().get();

        assertThat(bran.getMessage()).isEqualTo("CODE: description");
    }

}
