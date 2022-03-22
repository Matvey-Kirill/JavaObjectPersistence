package main.utils.types;

import java.io.IOException;

public class JSONFloat extends Number implements JSONNumberValue {
    private static final long serialVersionUID = 5622220776852501864L;
    public static final JSONFloat ZERO = new JSONFloat(0.0F);
    private float value;

    public JSONFloat(float value) {
        if (Float.isNaN(value)) {
            throw new JSONException("Can't store NaN as JSON");
        } else if (Float.isInfinite(value)) {
            throw new JSONException("Can't store infinity as JSON");
        } else {
            this.value = value;
        }
    }

    public float get() {
        return this.value;
    }

    public int intValue() {
        return (int)this.value;
    }

    public long longValue() {
        return (long)this.value;
    }

    public float floatValue() {
        return this.value;
    }

    public double doubleValue() {
        return (double)this.value;
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
        return (float)other == this.value;
    }

    public boolean valueEquals(long other) {
        return (float)other == this.value;
    }

    public boolean valueEquals(float other) {
        return other == this.value;
    }

    public boolean valueEquals(double other) {
        return other == (double)this.value;
    }

    public static JSONFloat valueOf(float value) {
        return value == 0.0F ? ZERO : new JSONFloat(value);
    }

    public static JSONFloat valueOf(String string) {
        try {
            return valueOf(Float.parseFloat(string));
        } catch (NumberFormatException var2) {
            throw new JSONException("Illegal JSON number");
        }
    }
}
