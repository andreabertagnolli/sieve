package ndr.brt;

import org.junit.Test;

import java.util.List;

import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertTrue;

public class PredicateValidatorTest {

    @Test
    public void when_validation_is_ok_returns_ok() throws Exception {
        PredicateValidator<String> validator = new StubValidator();

        List<Result> results = validator
                .validate("right")
                .collect(toList());

        assertThat(results).hasSize(1);
        assertTrue(results.get(0).isOk());
    }

    private class StubValidator extends PredicateValidator<String> {

    }
}
