package ndr.brt.sieve.uat;

import ndr.brt.sieve.Bran;
import ndr.brt.sieve.NestedReference;
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
    public void validate_mother_sons() throws Exception {
        Mother mother = new Mother("Lina", 42);
        mother.addSon(new Person("John", 15));
        mother.addSon(new Person("Laura", 19));
        mother.addSon(new Person("Bruno", 6));

        NestedValidator<Mother> validator = NestedValidator.<Mother>nestedValidator()
                .with(NestedReference.on(Mother::getSons)
                        .execute(PredicateValidator.<Person>when(p -> p.getAge() < 18).returns("AGE001", "{{name}} is not of age"))
                        .execute(PredicateValidator.<Person>when(p -> p.getName().startsWith("B")).returns("NAME001", "{{name}} start with B, and that's illegal!")));

        List<Bran> brans = validator.validate(mother).collect(toList());

        assertThat(brans.stream().filter(b -> "AGE001".equals(b.getCode())).count()).isEqualTo(2);
        assertThat(brans.stream().filter(b -> "NAME001".equals(b.getCode())).count()).isEqualTo(1);
    }

    @Test
    public void validate_mothers_sons() throws Exception {
        Mother mother = new Mother("Lina", 42);
        mother.addSon(new Person("John", 15));
        mother.addSon(new Person("Laura", 19));
        mother.addSon(new Person("Bruno", 6));

        Mother anotherMother = new Mother("Nina", 39);
        anotherMother.addSon(new Person("Frank", 18));

        NestedReference<Mother, Person> sonsReference = NestedReference.on(Mother::getSons)
                .execute(PredicateValidator.<Person>when(p -> p.getAge() < 18).returns("AGE001", "{{name}} is not of age"))
                .execute(PredicateValidator.<Person>when(p -> p.getName().startsWith("B")).returns("NAME001", "{{name}} start with B, and that's illegal!"));

        NestedValidator<Mother> validator = NestedValidator.<Mother>nestedValidator()
                .with(sonsReference);

        List<Bran> brans = validator.validate(asList(mother, anotherMother)).collect(toList());

        assertThat(brans.stream().filter(b -> "AGE001".equals(b.getCode())).count()).isEqualTo(2);
        assertThat(brans.stream().filter(b -> "NAME001".equals(b.getCode())).count()).isEqualTo(1);
    }
}
