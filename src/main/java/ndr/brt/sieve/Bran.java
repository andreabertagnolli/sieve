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

    public boolean isOk() {
        return false;
    }

    public boolean isError() {
        return !isOk();
    }

    public String getMessage() {
        return code + ": " + FieldPlaceholder.fieldPlaceholder(object).apply(description);
    }
}
