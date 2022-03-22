package main.utils.types;

import main.utils.print.Strings;

import java.io.IOException;
import java.util.Objects;

public class JSONString implements JSONValue, CharSequence {
    private static final long serialVersionUID = -7870545532058668339L;
    private String value;

    public JSONString(CharSequence cs) {
        this.value = ((CharSequence) Objects.requireNonNull(cs)).toString();
    }

    public String get() {
        return this.value;
    }

    public int length() {
        return this.value.length();
    }

    public char charAt(int index) {
        return this.value.charAt(index);
    }

    public CharSequence subSequence(int start, int end) {
        return this.value.subSequence(start, end);
    }

    public void appendJSON(Appendable a) throws IOException {
        a.append('"');
        int i = 0;
        int n = this.value.length();

        while(true) {
            while(true) {
                while(i < n) {
                    char ch = this.value.charAt(i++);
                    if (ch != '"' && ch != '\\') {
                        if (ch >= ' ' && ch < 127) {
                            a.append(ch);
                        } else if (ch == '\b') {
                            a.append('\\');
                            a.append('b');
                        } else if (ch == '\f') {
                            a.append('\\');
                            a.append('f');
                        } else if (ch == '\n') {
                            a.append('\\');
                            a.append('n');
                        } else if (ch == '\r') {
                            a.append('\\');
                            a.append('r');
                        } else if (ch == '\t') {
                            a.append('\\');
                            a.append('t');
                        } else {
                            a.append('\\');
                            a.append('u');
                            Strings.appendHex(a, ch);
                        }
                    } else {
                        a.append('\\');
                        a.append(ch);
                    }
                }

                a.append('"');
                return;
            }
        }
    }

    public String toString() {
        return this.value;
    }

    public int hashCode() {
        return this.value.hashCode();
    }

    public boolean equals(Object other) {
        return other == this || other instanceof JSONString && this.value.equals(((JSONString)other).value);
    }
}
