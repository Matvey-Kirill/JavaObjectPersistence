package main.utils.print;

public interface CharUnmapper {
    boolean isEscape(CharSequence var1, int var2);

    int unmap(StringBuilder var1, CharSequence var2, int var3);
}
