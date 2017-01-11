package ndr.brt;

public class Result {

    private final Boolean ok;
    private String message;

    public Result(Boolean ok) {
        this.ok = ok;
    }

    public boolean isOk() {
        return ok;
    }

    public boolean isError() {
        return !isOk();
    }

    public String getMessage() {
        return message;
    }

    public Result withMessage(String message) {
        this.message = message;
        return this;
    }
}
