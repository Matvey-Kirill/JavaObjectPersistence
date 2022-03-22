package main.utils.types;

import main.utils.print.Strings;

import java.io.IOException;

public class JSONLong extends Number implements JSONNumberValue {
    private static final long serialVersionUID = 4342545343730856828L;
    public static final JSONLong ZERO = new JSONLong(0L);
    private long value;

    public JSONLong(long value) {
        this.value = value;
    }

    public long get() {
        return this.value;
    }

    public int intValue() {
        return (int)this.value;
    }

    public long longValue() {
        return this.value;
    }

    public float floatValue() {
        return (float)this.value;
    }

    public double doubleValue() {
        return (double)this.value;
    }

    public void appendJSON(Appendable a) throws IOException {
        Strings.appendLong(a, this.value);
    }

    public String toString() {
        return this.toJSON();
    }

    public int hashCode() {
        return (int)this.value;
    }

    public boolean equals(Object other) {
        return other == this || other instanceof JSONNumberValue && ((JSONNumberValue)other).valueEquals(this.value);
    }

    public boolean valueEquals(int other) {
        return (long)other == this.value;
    }

    public boolean valueEquals(long other) {
        return other == this.value;
    }

    public boolean valueEquals(float other) {
        return other == (float)this.value;
    }

    public boolean valueEquals(double other) {
        return other == (double)this.value;
    }

    public static JSONLong valueOf(long value) {
        return value == 0L ? ZERO : new JSONLong(value);
    }

    public static JSONLong valueOf(String string) {
        try {
            return valueOf(Long.parseLong(string));
        } catch (NumberFormatException var2) {
            throw new JSONException("Illegal JSON number");
        }
    }
}
