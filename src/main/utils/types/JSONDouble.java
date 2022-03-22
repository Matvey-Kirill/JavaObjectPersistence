package main.utils.types;

import java.io.IOException;

public class JSONDouble extends Number implements JSONNumberValue {
    private static final long serialVersionUID = -1538839664142527465L;
    public static final JSONDouble ZERO = new JSONDouble(0.0D);
    private double value;

    public JSONDouble(double value) {
        if (Double.isNaN(value)) {
            throw new JSONException("Can't store NaN as JSON");
        } else if (Double.isInfinite(value)) {
            throw new JSONException("Can't store infinity as JSON");
        } else {
            this.value = value;
        }
    }

    public double get() {
        return this.value;
    }

    public int intValue() {
        return (int)this.value;
    }

    public long longValue() {
        return (long)this.value;
    }

    public float floatValue() {
        return (float)this.value;
    }

    public double doubleValue() {
        return this.value;
    }

    public String toJSON() {
        return String.valueOf(this.value);
    }

    public void appendJSON(Appendable a) throws IOException {
        a.append(this.toJSON());
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
        return (double)other == this.value;
    }

    public boolean valueEquals(long other) {
        return (double)other == this.value;
    }

    public boolean valueEquals(float other) {
        return (double)other == this.value;
    }

    public boolean valueEquals(double other) {
        return other == this.value;
    }

    public static JSONDouble valueOf(double value) {
        return value == 0.0D ? ZERO : new JSONDouble(value);
    }

    public static JSONDouble valueOf(String string) {
        try {
            return valueOf(Double.parseDouble(string));
        } catch (NumberFormatException var2) {
            throw new JSONException("Illegal JSON number");
        }
    }
}
