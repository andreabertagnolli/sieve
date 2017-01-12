package ndr.brt.sieve;

import static ndr.brt.sieve.FieldPlaceholder.substitutePlaceholders;

public class Bran {

    private final String code;
    private final String description;

    public Bran(Object object, String code, String description) {
        this.code = code;
        this.description = substitutePlaceholders(description, object);
    }

    public String getMessage() {
        return code + ": " + description;
    }
}
