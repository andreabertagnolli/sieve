package ndr.brt.sieve;

import org.junit.Test;

import java.util.List;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;

public class PlaceholderTest {

    @Test
    public void replace_placeholders() throws Exception {
        Pojo object = new Pojo("any name", 42L);

        String value = new Placeholder(object, "name").getValue();

        assertThat(value).isEqualTo("any name");
    }

    @Test
    public void replace_placeholders_on_superclass_fields() throws Exception {
        SubPojo object = new SubPojo("any name", 42L, "this place");

        String value = new Placeholder(object, "name").getValue();

        assertThat(value).isEqualTo("any name");
    }

    @Test
    public void print_error_when_field_does_not_exixt() throws Exception {
        Pojo object = new Pojo("any name", 42L);

        String value = new Placeholder(object, "notexisting").getValue();

        assertThat(value).isEqualTo("_error_");
    }

    @Test
    public void understand_first_level_without_parameters_objects() throws Exception {
        ListPojo object = new ListPojo(asList("one", "two", "three"));

        String value = new Placeholder(object, "list.size()").getValue();

        assertThat(value).isEqualTo("3");
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

    private class ListPojo {
        private final List<String> list;

        private ListPojo(List<String> list) {
            this.list = list;
        }
    }
}