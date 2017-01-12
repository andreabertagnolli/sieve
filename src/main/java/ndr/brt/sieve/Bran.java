package ndr.brt.sieve;

public class Bran {

    private final Object object;
    private final String code;
    private final String description;

    public Bran(Object object, String code, String description) {
        this.object = object;
        this.code = code;
        this.description = description;
    }

    public String getMessage() {
        return code + ": " + FieldPlaceholder.fieldPlaceholder(object).apply(description);
    }
}
