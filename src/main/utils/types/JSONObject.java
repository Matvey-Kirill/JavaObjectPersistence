package main.utils.types;

import main.utils.print.JSONMapping;

public class JSONObject extends JSONMapping<JSONValue> {
    private static final long serialVersionUID = 4424892153019501302L;

    public JSONObject() {
    }

    public JSONObject(int capacity) {
        super(capacity);
    }

    public JSONObject(JSONObject other) {
        super(other);
    }

    public JSONObject putValue(String key, CharSequence value) {
        this.put(key, new JSONString(value));
        return this;
    }

    public JSONObject putAlways(String key, CharSequence value) {
        this.put(key, value == null ? null : new JSONString(value));
        return this;
    }

    public JSONObject putNonNull(String key, CharSequence value) {
        if (value != null) {
            this.put(key, new JSONString(value));
        }

        return this;
    }

    public JSONObject putValue(String key, int value) {
        this.put(key, JSONInteger.valueOf(value));
        return this;
    }

    public JSONObject putValue(String key, long value) {
        this.put(key, JSONLong.valueOf(value));
        return this;
    }

    public JSONObject putValue(String key, float value) {
        this.put(key, JSONFloat.valueOf(value));
        return this;
    }

    public JSONObject putValue(String key, double value) {
        this.put(key, JSONDouble.valueOf(value));
        return this;
    }

    public JSONObject putValue(String key, boolean value) {
        this.put(key, JSONBoolean.valueOf(value));
        return this;
    }

    public JSONObject putNull(String key) {
        this.put(key, (JSONValue) null);
        return this;
    }

    public JSONObject putJSON(String key, JSONValue json) {
        this.put(key, json);
        return this;
    }

    public static JSONObject create() {
        return new JSONObject();
    }
}

