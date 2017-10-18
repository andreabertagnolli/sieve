package ndr.brt.sieve;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class FieldPlaceholderTest {

    @Test
    void replace_placeholders() throws Exception {
        String string = "This object has id {{id}} and it's called {{name}}";

        String result = FieldPlaceholder.substitutePlaceholders(string, new Pojo("any name", 42L));

        assertThat(result).isEqualTo("This object has id 42 and it's called any name");
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