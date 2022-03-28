package main.utils.types;

import main.utils.print.JSONSequence;

import java.util.Collection;
import java.util.Iterator;
import java.util.Objects;

public class JSONArray extends JSONSequence<JSONValue> {
    private static final long serialVersionUID = -6963671812529472759L;

    public JSONArray() {
    }

    public JSONArray addValue(CharSequence cs) {
        this.add(new JSONString(cs));
        return this;
    }

    public JSONArray addValue(int value) {
        this.add(JSONInteger.valueOf(value));
        return this;
    }

    public JSONArray addValue(long value) {
        this.add(JSONLong.valueOf(value));
        return this;
    }

    public JSONArray addValue(float value) {
        this.add(JSONFloat.valueOf(value));
        return this;
    }
    public JSONArray addValue(double value) {
        this.add(JSONDouble.valueOf(value));
        return this;
    }

    public JSONArray addValue(boolean value) {
        this.add(JSONBoolean.valueOf(value));
        return this;
    }
}
