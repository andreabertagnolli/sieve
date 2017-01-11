package ndr.brt.uat;

import ndr.brt.PredicateValidator;
import ndr.brt.Result;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

public class SingleLevelValidationTest {

    private PredicateValidator<Person> validator;

    @Before
    public void setUp() throws Exception {
        validator = PredicateValidator
                .<Person>okWhen(person -> person.getAge() >= 18)
                .returns("AGE001", "This person is not of age. {{name}}'s age is {{age}}");
    }

    @Test
    public void validate_age_of_some_people() throws Exception {
        List<Person> people = asList(
                new Person("John", 17),
                new Person("Asia", 19),
                new Person("Mark", 16)
        );

        List<Result> results = people.stream()
                .map(validator::validate)
                .flatMap(e -> e)
                .collect(toList());

        assertThat(results.stream().filter(Result::isError).count()).isEqualTo(2);
        assertThat(results.stream().filter(Result::isOk).count()).isEqualTo(1);
    }

    @Test
    public void use_placeholder_to_insert_into_message_object_information() throws Exception {
        Result result = validator.validate(new Person("Mark", 16)).findFirst().get();

        assertThat(result.getMessage()).isEqualTo("AGE001: This person is not of age. Mark's age is 16");
    }

    private class Person {
        private final String name;
        private final int age;

        private Person(String name, int age) {
            this.name = name;
            this.age = age;
        }

        public int getAge() {
            return age;
        }
    }
}
