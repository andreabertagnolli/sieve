package ndr.brt.sieve;

import static ndr.brt.sieve.FieldPlaceholder.SUBSTITUTE_PLACEHOLDERS;

public class Bran {

    private final String code;
    private final String description;

    public Bran(Object object, String code, String description) {
        this.code = code;
        this.description = SUBSTITUTE_PLACEHOLDERS.apply(description, object);
    }

    public String getMessage() {
        return code + ": " + description;
    }

    public String getCode() {
        return code;
    }

    @Override
    public String toString() {
        return getMessage();
    }
}
