package main.utils.print;

import java.util.Comparator;

public class CharMapperEntry {
    private int codePoint;
    private String string;
    public static final Comparator<CharMapperEntry> charComparator = new Comparator<CharMapperEntry>() {
        public int compare(CharMapperEntry o1, CharMapperEntry o2) {
            return o1.codePoint - o2.codePoint;
        }
    };
    public static final Comparator<CharMapperEntry> stringComparator = new Comparator<CharMapperEntry>() {
        public int compare(CharMapperEntry o1, CharMapperEntry o2) {
            return o1.string.compareTo(o2.string);
        }
    };

    public CharMapperEntry(int codePoint, String string) {
        this.codePoint = codePoint;
        this.string = string;
    }

    public int getCodePoint() {
        return this.codePoint;
    }

    public String getString() {
        return this.string;
    }
}
