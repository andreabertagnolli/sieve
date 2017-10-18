package ndr.brt.sieve.acceptance;

import ndr.brt.sieve.Bran;
import ndr.brt.sieve.PredicateValidator;
import ndr.brt.sieve.SieveValidator;
import ndr.brt.sieve.acceptance.pojo.*;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.function.Predicate;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;
import static ndr.brt.sieve.PredicateValidator.when;
import static org.assertj.core.api.Assertions.assertThat;

public class AcceptanceTest {

    @Test
    public void validate_mothers_sons() throws Exception {
        Mother mother = new Mother("Lina", 42);
        mother.addSon(new Person("John", 15));
        mother.addSon(new Person("Laura", 19));
        mother.addSon(new Person("Bruno", 6));

        Mother anotherMother = new Mother("Nina", 39);
        anotherMother.addSon(new Person("Frank", 18));

        SieveValidator<Mother> validator = SieveValidator.<Mother>validator()
                .with(PredicateValidator.<Mother>when(m -> m.getSons().size() < 2).returns("MOT001", "{{name}} has only {{sons.size()}} sons"))
                .with(Mother::getSons, SieveValidator.<Person>validator()
                    .with(PredicateValidator.<Person>when(p -> p.getAge() < 18).returns("AGE001", "{{name}} is not of age"))
                    .with(PredicateValidator.<Person>when(p -> p.getName().startsWith("B")).returns("NAME001", "{{name}} start with B, and that's illegal!"))
                );

        List<Bran> brans = validator.validate(asList(mother, anotherMother)).collect(toList());

        assertThat(brans.stream().filter(b -> "MOT001".equals(b.getCode())).count()).isEqualTo(1);
        assertThat(brans.stream().filter(b -> "AGE001".equals(b.getCode())).count()).isEqualTo(2);
        assertThat(brans.stream().filter(b -> "NAME001".equals(b.getCode())).count()).isEqualTo(1);
    }

    @Test
    public void validate_town() throws Exception {

        Town town = new Town(
            "Objectsville",
            new Person("Laura", 54),
            asList(
                new District(
                    "Immutable",
                    new Person("Lizzy", 43),
                    asList(
                        new Block(
                            345.43,
                            asList(
                                new House("FieldStreet 45", 4, 2, false),
                                new House("ConstantStreet 1", 2, 1, true)
                            )
                        ),
                        new Block(
                            345.43,
                            asList(
                                new House("MethodStreet 45", 1, 3, true),
                                new House("FunctionStreet 1", 6, 2, false)
                            )
                        )
                    )
                )
            )
        );

        SieveValidator<House> housesValidator = SieveValidator.<House>validator().with(
                when(House::hasNoBasement).returns("BAS001", "The house in {{address}} has no basement")
        );

        SieveValidator<Block> blocksValidator = SieveValidator.<Block>validator()
                .with(Block::getHouses, housesValidator)
                .with(
                        when(lessThan3Houses()).returns("BLO001", "The block has not enough houses")
                )
        ;

        SieveValidator<District> districtsValidator = SieveValidator.<District>validator()
                .with(District::getBlocks, blocksValidator);

        SieveValidator<Town> townValidator = SieveValidator.<Town>validator()
                .with(Town::getDistricts, districtsValidator);

        List<Bran> results = townValidator.validate(town).collect(toList());

        assertThat(results.stream().filter(b -> "BAS001".equals(b.getCode())).collect(toList())).hasSize(2);
        assertThat(results.stream().filter(b -> "BLO001".equals(b.getCode())).collect(toList())).hasSize(2);
    }

    private Predicate<Block> lessThan3Houses() {
        return block -> block.getHouses().size() < 3;
    }
}
