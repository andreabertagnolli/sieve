package ndr.brt.sieve;

public class Result {

    private final Object object;
    private final String code;
    private final String description;

    public Result(Object object, String code, String description) {
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
