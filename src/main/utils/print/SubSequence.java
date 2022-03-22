package main.utils.print;

import java.util.Objects;

public class SubSequence implements CharSequence {
    private CharSequence seq;
    private int start;
    private int end;
    private String string;

    public SubSequence(CharSequence seq, int start, int end) {
        Objects.requireNonNull(seq);
        if (start >= 0 && start <= seq.length()) {
            if (end >= start && end <= seq.length()) {
                this.seq = seq;
                this.start = start;
                this.end = end;
                this.string = null;
            } else {
                throw new IllegalArgumentException("SubSequence end incorrect");
            }
        } else {
            throw new IllegalArgumentException("SubSequence start incorrect");
        }
    }

    public int length() {
        return this.end - this.start;
    }

    public char charAt(int index) {
        int i = index + this.start;
        if (i >= this.end) {
            throw new StringIndexOutOfBoundsException("Subsequence index incorrect");
        } else {
            return this.seq.charAt(i);
        }
    }

    public CharSequence subSequence(int start, int end) {
        int len = this.length();
        if (start >= 0 && start <= len) {
            if (end >= start && end <= len) {
                return new SubSequence(this.seq, start + this.start, end + this.start);
            } else {
                throw new StringIndexOutOfBoundsException("Subsequence end index incorrect");
            }
        } else {
            throw new StringIndexOutOfBoundsException("Subsequence start index incorrect");
        }
    }

    public String toString() {
        if (this.string == null) {
            int len = this.length();
            if (len == 0) {
                this.string = "";
            } else {
                char[] array = new char[len];

                for(int i = 0; i < len; ++i) {
                    array[i] = this.seq.charAt(i + this.start);
                }

                this.string = new String(array);
            }
        }

        return this.string;
    }
}
