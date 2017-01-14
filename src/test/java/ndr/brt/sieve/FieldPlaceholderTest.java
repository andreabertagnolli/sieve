package ndr.brt.sieve;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class FieldPlaceholderTest {

    @Test
    public void replace_placeholders() throws Exception {
        String string = "This object has id {{id}} and it's called {{name}}";

        String result = FieldPlaceholder.substitutePlaceholders(string, new Pojo("any name", 42L));

        assertThat(result).isEqualTo("This object has id 42 and it's called any name");
    }

    @Test
    public void replace_placeholders_on_superclass_fields() throws Exception {
        String string = "This object has id {{id}} and it's called {{name}}, located in {{address}}";

        String result = FieldPlaceholder.substitutePlaceholders(string, new SubPojo("any name", 42L, "this place"));

        assertThat(result).isEqualTo("This object has id 42 and it's called any name, located in this place");
    }

    @Test
    public void print_error_when_field_does_not_exixt() throws Exception {
        String string = "This field {{notexisting}} does not exist!";

        String result = FieldPlaceholder.substitutePlaceholders(string, new Pojo("any name", 42L));

        assertThat(result).isEqualTo("This field _error_ does not exist!");
    }

    private class SubPojo extends Pojo {

        private final String address;

        private SubPojo(String name, Long id, String address) {
            super(name, id);
            this.address = address;
        }
    }

    private class Pojo {
        private final String name;
        private final Long id;

        private Pojo(String name, Long id) {
            this.name = name;
            this.id = id;
        }
    }
}