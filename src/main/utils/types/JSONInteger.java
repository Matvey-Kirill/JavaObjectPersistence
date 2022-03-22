package main.utils.types;

import main.utils.print.Strings;

import java.io.IOException;

public class JSONInteger extends Number implements JSONNumberValue {
    private static final long serialVersionUID = -4271512047065758655L;
    public static final JSONInteger ZERO = new JSONInteger(0);
    private int value;

    public JSONInteger(int value) {
        this.value = value;
    }

    public int get() {
        return this.value;
    }

    public int intValue() {
        return this.value;
    }

    public long longValue() {
        return (long)this.value;
    }

    public float floatValue() {
        return (float)this.value;
    }

    public double doubleValue() {
        return (double)this.value;
    }

    public void appendJSON(Appendable a) throws IOException {
        Strings.appendInt(a, this.value);
    }

    public String toString() {
        return this.toJSON();
    }

    public int hashCode() {
        return this.value;
    }

    public boolean equals(Object other) {
        return other == this || other instanceof JSONNumberValue && ((JSONNumberValue)other).valueEquals(this.value);
    }

    public boolean valueEquals(int other) {
        return other == this.value;
    }

    public boolean valueEquals(long other) {
        return other == (long)this.value;
    }

    public boolean valueEquals(float other) {
        return other == (float)this.value;
    }

    public boolean valueEquals(double other) {
        return other == (double)this.value;
    }

    public static JSONInteger valueOf(int value) {
        return value == 0 ? ZERO : new JSONInteger(value);
    }

    public static JSONInteger valueOf(String string) {
        try {
            return valueOf(Integer.parseInt(string));
        } catch (NumberFormatException var2) {
            throw new JSONException("Illegal JSON number");
        }
    }
}
