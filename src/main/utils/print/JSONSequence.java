package main.utils.print;

import main.utils.types.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

public class JSONSequence<V extends JSONValue> extends ArrayList<V> implements JSONComposite {
    private static final long serialVersionUID = 34670706002893562L;

    public JSONSequence() {
    }

    public JSONSequence(int capacity) {
        super(capacity);
    }

    @SafeVarargs
    public JSONSequence(V... values) {
        super(values.length);
        int i = 0;

        for(int n = values.length; i < n; ++i) {
            this.add(values[i]);
        }

    }

    public JSONSequence(Collection<? extends V> collection) {
        super(collection);
    }


    public Iterable<String> strings() {
        return new Iterable<String>() {
            public Iterator<String> iterator() {
                return JSONSequence.this.new StringIterator();
            }
        };
    }

    public void appendJSON(Appendable a) throws IOException {
        a.append('[');
        if (this.size() > 0) {
            int i = 0;

            while(true) {
                JSON.appendJSON(a, (JSONValue)this.get(i++));
                if (i >= this.size()) {
                    break;
                }

                a.append(',');
            }
        }

        a.append(']');
    }

    public boolean isSimple() {
        for(int i = 0; i < this.size(); ++i) {
            if (this.get(i) instanceof JSONComposite) {
                return false;
            }
        }

        return true;
    }

    public String toString() {
        return this.toJSON();
    }

    public boolean equals(Object other) {
        return other == this || other instanceof JSONSequence && super.equals(other);
    }

    public class ObjectIterator extends JSONSequence<V>.BaseIterator<JSONObject> {
        public ObjectIterator() {
            super();
        }

        public JSONObject next() {
            return JSON.getObject((JSONValue)this.iterator.next());
        }
    }

    public class ArrayIterator extends JSONSequence<V>.BaseIterator<JSONArray> {
        public ArrayIterator() {
            super();
        }

        public JSONArray next() {
            return JSON.getArray((JSONValue)this.iterator.next());
        }
    }

    public class BooleanIterator extends JSONSequence<V>.BaseIterator<Boolean> {
        public BooleanIterator() {
            super();
        }

        public Boolean next() {
            JSONValue value = (JSONValue)this.iterator.next();
            if (value == null) {
                return null;
            } else if (!(value instanceof JSONBoolean)) {
                throw new JSONException("Not a JSON boolean");
            } else {
                return ((JSONBoolean)value).booleanValue();
            }
        }
    }

    public class FloatIterator extends JSONSequence<V>.BaseIterator<Float> {
        public FloatIterator() {
            super();
        }

        public Float next() {
            JSONValue value = (JSONValue)this.iterator.next();
            if (value == null) {
                return null;
            } else if (!(value instanceof Number)) {
                throw new JSONException("Not a JSON number");
            } else {
                return ((Number)value).floatValue();
            }
        }
    }

    public class DoubleIterator extends JSONSequence<V>.BaseIterator<Double> {
        public DoubleIterator() {
            super();
        }

        public Double next() {
            JSONValue value = (JSONValue)this.iterator.next();
            if (value == null) {
                return null;
            } else if (!(value instanceof Number)) {
                throw new JSONException("Not a JSON number");
            } else {
                return ((Number)value).doubleValue();
            }
        }
    }

    public class LongIterator extends JSONSequence<V>.BaseIterator<Long> {
        public LongIterator() {
            super();
        }

        public Long next() {
            JSONValue value = (JSONValue)this.iterator.next();
            if (value == null) {
                return null;
            } else if (!(value instanceof Number)) {
                throw new JSONException("Not a JSON number");
            } else {
                return ((Number)value).longValue();
            }
        }
    }

    public class IntegerIterator extends JSONSequence<V>.BaseIterator<Integer> {
        public IntegerIterator() {
            super();
        }

        public Integer next() {
            JSONValue value = (JSONValue)this.iterator.next();
            if (value == null) {
                return null;
            } else if (!(value instanceof Number)) {
                throw new JSONException("Not a JSON number");
            } else {
                return ((Number)value).intValue();
            }
        }
    }

    public class StringIterator extends JSONSequence<V>.BaseIterator<String> {
        public StringIterator() {
            super();
        }

        public String next() {
            return JSON.getString((JSONValue)this.iterator.next());
        }
    }

    public abstract class BaseIterator<T> implements Iterator<T> {
        protected Iterator<V> iterator = JSONSequence.this.iterator();

        public BaseIterator() {
        }

        public boolean hasNext() {
            return this.iterator.hasNext();
        }

        public void remove() {
            this.iterator.remove();
        }
    }
}
