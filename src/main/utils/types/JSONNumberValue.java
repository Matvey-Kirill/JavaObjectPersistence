package main.utils.types;

public interface JSONNumberValue extends JSONValue {
    boolean valueEquals(int var1);

    boolean valueEquals(long var1);

    boolean valueEquals(float var1);

    boolean valueEquals(double var1);
}
