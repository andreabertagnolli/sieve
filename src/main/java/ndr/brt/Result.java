package ndr.brt;

public class Result {

    private final Boolean ok;

    public Result(Boolean ok) {
        this.ok = ok;
    }

    public boolean isOk() {
        return ok;
    }

    public boolean isError() {
        return !isOk();
    }
}
