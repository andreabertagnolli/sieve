package ndr.brt.sieve.uat;

import ndr.brt.sieve.Bran;
import ndr.brt.sieve.NestedValidator;
import ndr.brt.sieve.PredicateValidator;
import ndr.brt.sieve.uat.pojo.Mother;
import ndr.brt.sieve.uat.pojo.Person;
import org.junit.Test;

import java.util.List;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;

public class NestedLevelValidationTest {

    @Test
    public void validate_mothers_sons_age() throws Exception {
        Mother mother = new Mother("Lina", 42);
        mother.addSon(new Person("John", 15));
        mother.addSon(new Person("Laura", 19));
        mother.addSon(new Person("Bruno", 6));

        List<PredicateValidator<Person>> sonsValidators = asList(
                PredicateValidator.<Person>okWhen(p -> p.getAge() > 18).returns("AGE001", "{{name}} is not of age"),
                PredicateValidator.<Person>okWhen(p -> p.getName().startsWith("B")).returns("NAME001", "{{name}} does not start with B")
        );

        NestedValidator<Mother, Person> validator = NestedValidator.<Mother, Person>nestedValidator()
                .on(Mother::getSons).use(sonsValidators);

        List<Bran> brans = validator.validate(mother).collect(toList());

        assertThat(brans.stream().filter(b -> "AGE001".equals(b.getCode())).count()).isEqualTo(2);
        assertThat(brans.stream().filter(b -> "NAME001".equals(b.getCode())).count()).isEqualTo(2);
    }
}
