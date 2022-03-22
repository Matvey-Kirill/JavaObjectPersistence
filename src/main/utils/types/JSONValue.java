package main.utils.types;

import java.io.IOException;
import java.io.Serializable;

public interface JSONValue extends Serializable {
    void appendJSON(Appendable var1) throws IOException;

    default String toJSON() {
        StringBuilder sb = new StringBuilder(12);

        try {
            this.appendJSON(sb);
        } catch (IOException var3) {
        }

        return sb.toString();
    }
}
