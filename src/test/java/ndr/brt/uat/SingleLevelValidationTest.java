package ndr.brt.uat;

import ndr.brt.PredicateValidator;
import ndr.brt.Result;
import org.junit.Test;

import java.util.List;
import java.util.function.Function;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

public class SingleLevelValidationTest {

    @Test
    public void validate_age_of_some_people() throws Exception {
        PredicateValidator<Person> validator = new OfAge();

        List<Person> people = asList(
                new Person(17),
                new Person(19),
                new Person(16)
        );

        List<Result> results = people.stream()
                .map(validator::validate)
                .flatMap(e -> e)
                .collect(toList());

        assertThat(results.stream().filter(Result::isError).count()).isEqualTo(2);
        assertThat(results.stream().filter(Result::isOk).count()).isEqualTo(1);
    }

    private class OfAge extends PredicateValidator<Person> {

        @Override
        protected Function<Person, Boolean> predicate() {
            return person -> person.getAge() >= 18;
        }
        @Override
        protected String code() {
            return "AGE001";
        }

        @Override
        protected String description() {
            return "This person is not of age.";
        }

    }

    private class Person {
        private final int age;

        private Person(int age) {
            this.age = age;
        }

        public int getAge() {
            return age;
        }
    }
}
