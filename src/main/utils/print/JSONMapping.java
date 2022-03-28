package main.utils.print;

import main.utils.types.*;

import java.io.IOException;
import java.util.Iterator;
import java.util.Objects;

public class JSONMapping<V extends JSONValue> extends ListMap<String, V> implements JSONComposite, Iterable<String> {
    private static final long serialVersionUID = 4424892153019501302L;

    public JSONMapping() {
    }

    public JSONMapping(int capacity) {
        super(capacity);
    }

    public JSONMapping(JSONMapping<V> other) {
        super(other);
    }

    public Iterator<String> iterator() {
        return this.keySet().iterator();
    }

    public void appendJSON(Appendable a) throws IOException {
        a.append('{');
        int n = this.list.size();
        if (n > 0) {
            int i = 0;

            while(true) {
                Entry<String, V> entry = (Entry)this.list.get(i++);
                a.append('"');
                Strings.appendEscaped(a, (CharSequence)entry.getKey(), JSON.charMapper);
                a.append('"').append(':');
                JSON.appendJSON(a, (JSONValue)entry.getValue());
                if (i >= n) {
                    break;
                }

                a.append(',');
            }
        }

        a.append('}');
    }

    public boolean isSimple() {
        for(int i = 0; i < this.list.size(); ++i) {
            if (((Entry)this.list.get(i)).getValue() instanceof JSONComposite) {
                return false;
            }
        }

        return true;
    }

    public String toString() {
        return this.toJSON();
    }

    public int hashCode() {
        int result = 0;
        int i = 0;

        for(int n = this.list.size(); i < n; ++i) {
            result ^= ((Entry)this.list.get(i)).hashCode();
        }

        return result;
    }

    public boolean equals(Object other) {
        if (other == this) {
            return true;
        } else if (!(other instanceof JSONMapping)) {
            return false;
        } else {
            JSONMapping<?> otherMapping = (JSONMapping)other;
            if (this.list.size() != otherMapping.list.size()) {
                return false;
            } else {
                Iterator var3 = this.list.iterator();

                Entry entry;
                do {
                    if (!var3.hasNext()) {
                        return true;
                    }

                    entry = (Entry)var3.next();
                } while(Objects.equals(entry.getValue(), otherMapping.get(entry.getKey())));

                return false;
            }
        }
    }
}
