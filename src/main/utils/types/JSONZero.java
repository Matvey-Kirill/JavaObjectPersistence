package main.utils.types;

import java.io.IOException;

public class JSONZero extends Number implements JSONNumberValue {
    private static final long serialVersionUID = -2972159538314277194L;
    public static final JSONZero ZERO = new JSONZero();

    public JSONZero() {
    }

    public int get() {
        return 0;
    }

    public int intValue() {
        return 0;
    }

    public long longValue() {
        return 0L;
    }

    public float floatValue() {
        return 0.0F;
    }

    public double doubleValue() {
        return 0.0D;
    }

    public String toJSON() {
        return "0";
    }

    public void appendJSON(Appendable a) throws IOException {
        a.append('0');
    }

    public String toString() {
        return "0";
    }

    public int hashCode() {
        return 0;
    }

    public boolean equals(Object other) {
        return other == this || other instanceof JSONNumberValue && ((JSONNumberValue)other).valueEquals(0);
    }

    public boolean valueEquals(int other) {
        return other == 0;
    }

    public boolean valueEquals(long other) {
        return other == 0L;
    }

    public boolean valueEquals(float other) {
        return other == 0.0F;
    }

    public boolean valueEquals(double other) {
        return other == 0.0D;
    }
}
