package main.utils.types;

import java.io.IOException;
import java.util.Objects;

public class JSONBoolean implements JSONValue {
    private static final long serialVersionUID = -3294980363221183247L;
    public static final JSONBoolean FALSE = new JSONBoolean(false);
    public static final JSONBoolean TRUE = new JSONBoolean(true);
    private boolean value;

    public JSONBoolean(boolean value) {
        this.value = value;
    }

    public JSONBoolean(Boolean value) {
        this.value = (Boolean)Objects.requireNonNull(value);
    }

    public boolean get() {
        return this.value;
    }

    public boolean booleanValue() {
        return this.value;
    }

    public String toJSON() {
        return this.value ? "true" : "false";
    }

    public void appendJSON(Appendable a) throws IOException {
        a.append(this.toJSON());
    }

    public String toString() {
        return this.toJSON();
    }

    public int hashCode() {
        return this.value ? 1 : 0;
    }

    public boolean equals(Object other) {
        return other == this || other instanceof JSONBoolean && this.value == ((JSONBoolean)other).get();
    }

    public static JSONBoolean valueOf(boolean b) {
        return b ? TRUE : FALSE;
    }
}
