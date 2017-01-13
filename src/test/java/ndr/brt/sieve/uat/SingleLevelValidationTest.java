package ndr.brt.sieve.uat;

import ndr.brt.sieve.PredicateValidator;
import ndr.brt.sieve.Bran;
import ndr.brt.sieve.uat.pojo.Person;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static java.util.Arrays.asList;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

public class SingleLevelValidationTest {

    private PredicateValidator<Person> validator;

    @Before
    public void setUp() throws Exception {
        validator = PredicateValidator
                .<Person>when(person -> person.getAge() < 18)
                .returns("AGE001", "This person is not of age. {{name}}'s age is {{age}}");
    }

    @Test
    public void validate_age_of_some_people() throws Exception {
        List<Person> people = asList(
                new Person("John", 17),
                new Person("Asia", 19),
                new Person("Mark", 16)
        );

        long errors = validator.validate(people).count();

        assertThat(errors).isEqualTo(2L);
    }

    @Test
    public void use_placeholder_to_insert_into_message_object_information() throws Exception {
        Person person = new Person("Mark", 16);

        Bran bran = validator.validate(person).findFirst().get();

        assertThat(bran.getMessage()).isEqualTo("AGE001: This person is not of age. Mark's age is 16");
    }

}
