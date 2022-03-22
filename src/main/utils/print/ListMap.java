package main.utils.print;

import java.io.Serializable;
import java.util.AbstractSet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Set;

public class ListMap<K, V> implements Map<K, V>, Serializable {
    private static final long serialVersionUID = -5594713182082941289L;
    protected List<ListMap.Entry<K, V>> list;

    public ListMap() {
        this.list = new ArrayList();
    }

    public ListMap(int capacity) {
        this.list = new ArrayList(capacity);
    }

    public ListMap(Map<? extends K, ? extends V> m) {
        this(m.size());
        this.putAll(m);
    }

    public V get(Object key) {
        int index = this.findIndex(Objects.requireNonNull(key));
        return index < 0 ? null : (V) ((Entry)this.list.get(index)).getValue();
    }

    public boolean containsKey(Object key) {
        return this.findIndex(Objects.requireNonNull(key)) >= 0;
    }

    public V put(K key, V value) {
        int index = this.findIndex(Objects.requireNonNull(key));
        if (index >= 0) {
            ListMap.Entry<K, V> entry = (ListMap.Entry)this.list.get(index);
            V oldValue = entry.getValue();
            entry.setValue(value);
            return oldValue;
        } else {
            this.list.add(new ListMap.Entry(key, value));
            return null;
        }
    }

    public V remove(Object key) {
        int index = this.findIndex(Objects.requireNonNull(key));
        return index >= 0 ? (V) ((Entry)this.list.remove(index)).getValue() : null;
    }

    public int size() {
        return this.list.size();
    }

    public boolean isEmpty() {
        return this.size() == 0;
    }

    public boolean containsValue(Object value) {
        int i = 0;

        for(int n = this.list.size(); i < n; ++i) {
            if (Objects.equals(((ListMap.Entry)this.list.get(i)).getValue(), value)) {
                return true;
            }
        }

        return false;
    }

    public void putAll(Map<? extends K, ? extends V> m) {
        Iterator var2 = m.keySet().iterator();

        while(var2.hasNext()) {
            K k = (K) var2.next();
            this.put(k, m.get(k));
        }

    }

    public void clear() {
        this.list.clear();
    }

    public Set<K> keySet() {
        return new ListMap.KeySet();
    }

    public Collection<V> values() {
        return new ListMap.ValueCollection();
    }

    public Set<java.util.Map.Entry<K, V>> entrySet() {
        return new ListMap.EntrySet();
    }

    public int hashCode() {
        int result = 0;
        int i = 0;

        for(int n = this.list.size(); i < n; ++i) {
            result ^= ((ListMap.Entry)this.list.get(i)).hashCode();
        }

        return result;
    }

    public boolean equals(Object other) {
        if (other == this) {
            return true;
        } else if (!(other instanceof ListMap)) {
            return false;
        } else {
            ListMap<?, ?> otherMapping = (ListMap)other;
            if (this.list.size() != otherMapping.list.size()) {
                return false;
            } else {
                Iterator var3 = this.list.iterator();

                ListMap.Entry entry;
                do {
                    if (!var3.hasNext()) {
                        return true;
                    }

                    entry = (ListMap.Entry)var3.next();
                } while(Objects.equals(entry.getValue(), otherMapping.get(entry.getKey())));

                return false;
            }
        }
    }

    public ListMap.Entry<K, V> getEntry(int index) {
        return (ListMap.Entry)this.list.get(index);
    }

    protected int findIndex(Object key) {
        int i = 0;

        for(int n = this.list.size(); i < n; ++i) {
            if (((ListMap.Entry)this.list.get(i)).getKey().equals(key)) {
                return i;
            }
        }

        return -1;
    }

    private abstract class CollectionBase<T> extends AbstractSet<T> {
        private CollectionBase(Object o) {
        }

        public int size() {
            return ListMap.this.list.size();
        }

        public boolean remove(Object o) {
            throw new UnsupportedOperationException();
        }

        public boolean addAll(Collection<? extends T> c) {
            throw new UnsupportedOperationException();
        }

        public boolean retainAll(Collection<?> c) {
            throw new UnsupportedOperationException();
        }

        public boolean removeAll(Collection<?> c) {
            throw new UnsupportedOperationException();
        }

        public void clear() {
            throw new UnsupportedOperationException();
        }
    }

    private class ValueCollection extends ListMap<K, V>.CollectionBase<V> {
        private ValueCollection() {
            super(null);
        }

        public boolean contains(Object o) {
            return ListMap.this.containsValue(o);
        }

        public Iterator<V> iterator() {
            return ListMap.this.new ValueIterator();
        }

        public boolean containsAll(Collection<?> c) {
            Iterator var2 = c.iterator();

            Object o;
            do {
                if (!var2.hasNext()) {
                    return true;
                }

                o = var2.next();
            } while(ListMap.this.containsValue(o));

            return false;
        }
    }

    private class KeySet extends ListMap<K, V>.CollectionBase<K> {
        private KeySet() {
            super(null);
        }

        public boolean contains(Object o) {
            return ListMap.this.containsKey(o);
        }

        public Iterator<K> iterator() {
            return ListMap.this.new KeyIterator();
        }

        public boolean containsAll(Collection<?> c) {
            Iterator var2 = c.iterator();

            Object o;
            do {
                if (!var2.hasNext()) {
                    return true;
                }

                o = var2.next();
            } while(ListMap.this.containsKey(o));

            return false;
        }
    }

    private class EntrySet extends ListMap<K, V>.CollectionBase<java.util.Map.Entry<K, V>> {
        private EntrySet() {
            super(null);
        }

        public boolean contains(Object o) {
            return ListMap.this.list.contains(o);
        }

        public Iterator<java.util.Map.Entry<K, V>> iterator() {
            return ListMap.this.new EntryIterator();
        }

        public boolean containsAll(Collection<?> c) {
            Iterator var2 = c.iterator();

            Object o;
            do {
                if (!var2.hasNext()) {
                    return true;
                }

                o = var2.next();
            } while(ListMap.this.list.contains(o));

            return false;
        }
    }

    private abstract class BaseIterator<T> implements Iterator<T> {
        private int index = 0;

        public BaseIterator() {
        }

        public ListMap.Entry<K, V> nextEntry() {
            if (!this.hasNext()) {
                throw new NoSuchElementException();
            } else {
                return (ListMap.Entry)ListMap.this.list.get(this.index++);
            }
        }

        public boolean hasNext() {
            return this.index < ListMap.this.list.size();
        }
    }

    private class ValueIterator extends ListMap<K, V>.BaseIterator<V> {
        private ValueIterator() {
            super();
        }

        public V next() {
            return this.nextEntry().getValue();
        }
    }

    private class KeyIterator extends ListMap<K, V>.BaseIterator<K> {
        private KeyIterator() {
            super();
        }

        public K next() {
            return this.nextEntry().getKey();
        }
    }

    private class EntryIterator extends ListMap<K, V>.BaseIterator<java.util.Map.Entry<K, V>> {
        private EntryIterator() {
            super();
        }

        public ListMap.Entry<K, V> next() {
            return this.nextEntry();
        }
    }

    public static class Entry<KK, VV> implements java.util.Map.Entry<KK, VV>, Serializable {
        private static final long serialVersionUID = -7610378954393786210L;
        private KK key;
        private VV value;

        public Entry(KK key, VV value) {
            this.key = key;
            this.value = value;
        }

        public KK getKey() {
            return this.key;
        }

        public VV getValue() {
            return this.value;
        }

        public VV setValue(VV value) {
            VV oldValue = this.value;
            this.value = value;
            return oldValue;
        }

        public boolean equals(Object other) {
            if (other == this) {
                return true;
            } else if (!(other instanceof java.util.Map.Entry)) {
                return false;
            } else {
                java.util.Map.Entry<?, ?> otherEntry = (java.util.Map.Entry)other;
                return Objects.equals(this.key, otherEntry.getKey()) && Objects.equals(this.value, otherEntry.getValue());
            }
        }

        public int hashCode() {
            return Objects.hash(new Object[]{this.key, this.value});
        }
    }
}