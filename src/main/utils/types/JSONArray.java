package main.utils.types;

import main.utils.print.JSONSequence;

import java.util.Collection;
import java.util.Iterator;
import java.util.Objects;

public class JSONArray extends JSONSequence<JSONValue> {
    private static final long serialVersionUID = -6963671812529472759L;

    public JSONArray() {
    }

    public JSONArray(int capacity) {
        super(capacity);
    }

    public JSONArray(JSONValue... values) {
        super(values);
    }

    public JSONArray(Collection<? extends JSONValue> collection) {
        super(collection);
    }

    public JSONArray addValue(CharSequence cs) {
        this.add(new JSONString(cs));
        return this;
    }

    public JSONArray addAlways(CharSequence cs) {
        this.add(new JSONString(cs == null ? null : new JSONString(cs)));
        return this;
    }

    public JSONArray addNonNull(CharSequence cs) {
        if (cs != null) {
            this.add(new JSONString(new JSONString(cs)));
        }

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

    public JSONArray addValue(Boolean value) {
        this.add(JSONBoolean.valueOf((Boolean)Objects.requireNonNull(value)));
        return this;
    }

    public <T extends CharSequence> JSONArray addValues(Collection<T> collection) {
        Iterator var2 = collection.iterator();

        while(var2.hasNext()) {
            CharSequence value = (CharSequence)var2.next();
            this.addValue(value);
        }

        return this;
    }

    public <T extends CharSequence> JSONArray addValues(T... values) {
        int i = 0;

        for(int n = values.length; i < n; ++i) {
            this.addValue(values[i]);
        }

        return this;
    }

    public JSONArray addValues(int... values) {
        int i = 0;

        for(int n = values.length; i < n; ++i) {
            this.addValue(values[i]);
        }

        return this;
    }

    public JSONArray addValues(long... values) {
        int i = 0;

        for(int n = values.length; i < n; ++i) {
            this.addValue(values[i]);
        }

        return this;
    }

    public JSONArray addValues(float... values) {
        int i = 0;

        for(int n = values.length; i < n; ++i) {
            this.addValue(values[i]);
        }

        return this;
    }

    public JSONArray addValues(double... values) {
        int i = 0;

        for(int n = values.length; i < n; ++i) {
            this.addValue(values[i]);
        }

        return this;
    }

    public JSONArray addNull() {
        this.add((JSONValue) null);
        return this;
    }

    public JSONArray addJSON(JSONValue json) {
        this.add(json);
        return this;
    }

    public static JSONArray create() {
        return new JSONArray();
    }
}
