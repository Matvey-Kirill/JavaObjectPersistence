package main.utils.types;

public class JSONException extends IllegalArgumentException {
    private static final long serialVersionUID = -2645697224315774936L;

    public JSONException(String message) {
        super(message);
    }

    public JSONException(String message, Exception nested) {
        super(message, nested);
    }
}
