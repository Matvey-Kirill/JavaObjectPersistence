package main.utils.print;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;

public class ParseText {
    private CharSequence text;
    private int index;
    private int start;
    private static final int MAX_INT_DIV_10 = 214748364;
    private static final int MAX_INT_MOD_10 = 7;
    private static final long MAX_LONG_DIV_10 = 922337203685477580L;
    private static final int MAX_LONG_MOD_10 = 7;
    private static final int MAX_INT_MASK = -134217728;
    private static final long MAX_LONG_MASK = -576460752303423488L;

    public ParseText(CharSequence text, int index) {
        this.setText(text, index);
    }

    public ParseText(CharSequence text) {
        this.setText(text, 0);
    }

    public ParseText setText(CharSequence text, int index) {
        if (text == null) {
            throw new NullPointerException("ParseText data invalid");
        } else {
            this.text = text;
            this.setIndex(index);
            this.start = index;
            return this;
        }
    }

    public ParseText setText(CharSequence text) {
        this.setText(text, 0);
        return this;
    }

    public CharSequence getText() {
        return this.text;
    }

    public int getTextLength() {
        return this.text.length();
    }

    public boolean isExhausted() {
        return this.index >= this.text.length();
    }

    public int getIndex() {
        return this.index;
    }

    public ParseText setIndex(int index) {
        if (index >= 0 && index <= this.text.length()) {
            this.index = index;
            return this;
        } else {
            throw new StringIndexOutOfBoundsException("ParseText index invalid");
        }
    }

    protected boolean matchSuccess(int i) {
        this.start = this.index;
        this.index = i;
        return true;
    }

    public char getChar() {
        this.start = this.index;
        if (this.index >= this.text.length()) {
            throw new StringIndexOutOfBoundsException("ParseText exhausted");
        } else {
            return this.text.charAt(this.index++);
        }
    }

    public int getCodePoint() {
        this.start = this.index;
        if (this.index >= this.text.length()) {
            throw new StringIndexOutOfBoundsException("ParseText exhausted");
        } else {
            char ch = this.text.charAt(this.index++);
            if (Character.isHighSurrogate(ch) && this.index < this.text.length()) {
                char ch2 = this.text.charAt(this.index);
                if (Character.isLowSurrogate(ch2)) {
                    ++this.index;
                    return Character.toCodePoint(ch, ch2);
                }
            }

            return ch;
        }
    }

    public String getString(int from, int to) {
        return this.text.subSequence(from, to).toString();
    }

    public int getStart() {
        return this.start;
    }

    public ParseText setStart(int start) {
        if (start >= 0 && start <= this.index) {
            this.start = start;
            return this;
        } else {
            throw new StringIndexOutOfBoundsException("ParseText start index invalid");
        }
    }

    public int getResultInt() {
        return this.getInt(this.start, this.index);
    }

    public int getInt(int from, int to) {
        if (to <= from) {
            throw new NumberFormatException();
        } else {
            int result = 0;

            for(int i = from; i < to; ++i) {
                int n = this.convertDecDigit(this.text.charAt(i));
                if (result > 214748364 || result == 214748364 && n > 7) {
                    throw new NumberFormatException();
                }

                result = result * 10 + n;
            }

            return result;
        }
    }

    public long getResultLong() {
        return this.getLong(this.start, this.index);
    }

    public long getLong(int from, int to) {
        if (to <= from) {
            throw new NumberFormatException();
        } else {
            long result = 0L;

            for(int i = from; i < to; ++i) {
                int n = this.convertDecDigit(this.text.charAt(i));
                if (result > 922337203685477580L || result == 922337203685477580L && n > 7) {
                    throw new NumberFormatException();
                }

                result = result * 10L + (long)n;
            }

            return result;
        }
    }

    public int convertDecDigit(char ch) {
        if (ch >= '0' && ch <= '9') {
            return ch - 48;
        } else {
            throw new NumberFormatException();
        }
    }

    public int getResultHexInt() {
        return this.getHexInt(this.start, this.index);
    }

    public int getHexInt(int from, int to) {
        if (to <= from) {
            throw new NumberFormatException();
        } else {
            int result = 0;

            for(int i = from; i < to; ++i) {
                if ((result & -134217728) != 0) {
                    throw new NumberFormatException();
                }

                result = result << 4 | this.convertHexDigit(this.text.charAt(i));
            }

            return result;
        }
    }

    public long getResultHexLong() {
        return this.getHexLong(this.start, this.index);
    }

    public long getHexLong(int from, int to) {
        if (to <= from) {
            throw new NumberFormatException();
        } else {
            long result = 0L;

            for(int i = from; i < to; ++i) {
                if ((result & -576460752303423488L) != 0L) {
                    throw new NumberFormatException();
                }

                result = result << 4 | (long)this.convertHexDigit(this.text.charAt(i));
            }

            return result;
        }
    }

    public int convertHexDigit(char ch) {
        if (ch >= '0' && ch <= '9') {
            return ch - 48;
        } else if (ch >= 'A' && ch <= 'F') {
            return ch - 65 + 10;
        } else if (ch >= 'a' && ch <= 'f') {
            return ch - 97 + 10;
        } else {
            throw new NumberFormatException();
        }
    }

    public int getResultLength() {
        return this.index - this.start;
    }

    public char getResultChar() {
        return this.text.charAt(this.start);
    }

    public String getResultString() {
        return this.text.subSequence(this.start, this.index).toString();
    }

    public StringBuilder appendResultTo(StringBuilder sb) {
        return sb.append(this.text, this.start, this.index);
    }

    public Appendable appendResultTo(Appendable a) throws IOException {
        return a.append(this.text, this.start, this.index);
    }

    public boolean available(int len) {
        return this.index + len <= this.text.length();
    }

    public int length() {
        return this.text.length();
    }

    public char charAt(int index) {
        return this.text.charAt(index);
    }

    public boolean match(int cp) {
        int i = this.index;
        if (i >= this.text.length()) {
            return false;
        } else {
            char ch = this.text.charAt(i++);
            if (Character.isHighSurrogate(ch)) {
                if (i >= this.text.length()) {
                    return false;
                }

                char ch2 = this.text.charAt(i++);
                if (!Character.isLowSurrogate(ch2)) {
                    return false;
                }

                if (Character.toCodePoint(ch, ch2) != cp) {
                    return false;
                }
            } else if (ch != cp) {
                return false;
            }

            this.start = this.index;
            this.index = i;
            return true;
        }
    }

    public boolean match(char ch) {
        if (this.index < this.text.length() && this.text.charAt(this.index) == ch) {
            this.start = this.index++;
            return true;
        } else {
            return false;
        }
    }

    public boolean matchIgnoreCase(char ch) {
        if (this.index < this.text.length() && equalIgnoreCase(this.text.charAt(this.index), ch)) {
            this.start = this.index++;
            return true;
        } else {
            return false;
        }
    }

    private static boolean equalIgnoreCase(char a, char b) {
        return a == b || a == (Character.isLowerCase(a) ? Character.toLowerCase(b) : Character.toUpperCase(b));
    }

    public boolean matchRange(char from, char to) {
        if (this.index >= this.text.length()) {
            return false;
        } else {
            char ch = this.text.charAt(this.index);
            if (ch >= from && ch <= to) {
                this.start = this.index++;
                return true;
            } else {
                return false;
            }
        }
    }

    public boolean matchAnyOf(String str) {
        if (this.index >= this.text.length()) {
            return false;
        } else if (str.indexOf(this.text.charAt(this.index)) < 0) {
            return false;
        } else {
            this.start = this.index++;
            return true;
        }
    }

    public boolean matchAnyOf(char... array) {
        if (array.length == 0) {
            throw new IllegalArgumentException("Array must not be empty");
        } else if (this.index >= this.text.length()) {
            return false;
        } else {
            char ch = this.text.charAt(this.index);
            int i = 0;

            for(int n = array.length; i < n; ++i) {
                if (ch == array[i]) {
                    this.start = this.index++;
                    return true;
                }
            }

            return false;
        }
    }

    public boolean match(CharSequence target) {
        int len = target.length();
        if (this.index + len > this.text.length()) {
            return false;
        } else {
            int i = this.index;

            for(int var4 = 0; len > 0; --len) {
                if (this.text.charAt(i++) != target.charAt(var4++)) {
                    return false;
                }
            }

            this.start = this.index;
            this.index = i;
            return true;
        }
    }

    public boolean matchName(CharSequence target) {
        int len = target.length();
        if (this.index + len > this.text.length()) {
            return false;
        } else {
            int i = this.index;

            for(int var4 = 0; len > 0; --len) {
                if (this.text.charAt(i++) != target.charAt(var4++)) {
                    return false;
                }
            }

            if (i < this.text.length() && this.isNameContinuation(this.text.charAt(i))) {
                return false;
            } else {
                this.start = this.index;
                this.index = i;
                return true;
            }
        }
    }

    public boolean matchAnyOf(CharSequence... array) {
        if (array.length == 0) {
            throw new IllegalArgumentException("Array must not be empty");
        } else {
            CharSequence[] var2 = array;
            int var3 = array.length;

            for(int var4 = 0; var4 < var3; ++var4) {
                CharSequence str = var2[var4];
                if (this.match(str)) {
                    return true;
                }
            }

            return false;
        }
    }

    public boolean matchAnyOf(Collection<? extends CharSequence> collection) {
        Iterator var2 = collection.iterator();

        CharSequence str;
        do {
            if (!var2.hasNext()) {
                return false;
            }

            str = (CharSequence)var2.next();
        } while(!this.match(str));

        return true;
    }

    public boolean matchIgnoreCase(CharSequence target) {
        int len = target.length();
        if (this.index + len > this.text.length()) {
            return false;
        } else {
            int i = this.index;

            for(int var4 = 0; len > 0; --len) {
                if (!equalIgnoreCase(this.text.charAt(i++), target.charAt(var4++))) {
                    return false;
                }
            }

            this.start = this.index;
            this.index = i;
            return true;
        }
    }

    public boolean matchDec(int maxDigits, int minDigits) {
        int i = this.index;
        int stopper = this.text.length();
        if (maxDigits > 0) {
            stopper = Math.min(stopper, i + maxDigits);
        }

        while(i < stopper && this.isDigit(this.text.charAt(i))) {
            ++i;
        }

        if (i - this.index < minDigits) {
            return false;
        } else {
            this.start = this.index;
            this.index = i;
            return true;
        }
    }

    public boolean matchDec(int maxDigits) {
        return this.matchDec(maxDigits, 1);
    }

    public boolean matchDec() {
        return this.matchDec(0, 1);
    }

    public boolean matchDecFixed(int numDigits) {
        return this.matchDec(numDigits, numDigits);
    }

    public boolean matchHex(int maxDigits, int minDigits) {
        int i = this.index;
        int stopper = this.text.length();
        if (maxDigits > 0) {
            stopper = Math.min(stopper, i + maxDigits);
        }

        while(i < stopper && this.isHexDigit(this.text.charAt(i))) {
            ++i;
        }

        if (i - this.index < minDigits) {
            return false;
        } else {
            this.start = this.index;
            this.index = i;
            return true;
        }
    }

    public boolean matchHex(int maxDigits) {
        return this.matchHex(maxDigits, 1);
    }

    public boolean matchHex() {
        return this.matchHex(0, 1);
    }

    public boolean matchHexFixed(int numDigits) {
        return this.matchHex(numDigits, numDigits);
    }

    public ParseText revert() {
        this.index = this.start;
        return this;
    }

    public ParseText reset() {
        this.index = 0;
        return this;
    }

    public ParseText skip(int n) {
        this.start = this.index;
        this.setIndex(this.index + n);
        return this;
    }

    public ParseText back(int n) {
        this.setIndex(this.index - n);
        return this;
    }

    public ParseText skipTo(char ch) {
        int i = this.index;

        for(this.start = i; i < this.text.length() && this.text.charAt(i) != ch; ++i) {
        }

        this.index = i;
        return this;
    }

    public ParseText skipToAnyOf(char... array) {
        if (array.length == 0) {
            throw new IllegalArgumentException("Array must not be empty");
        } else {
            int i = this.index;

            label26:
            for(this.start = i; i < this.text.length(); ++i) {
                char ch = this.text.charAt(i);

                for(int j = 0; j < array.length; ++j) {
                    if (ch == array[j]) {
                        break label26;
                    }
                }
            }

            this.index = i;
            return this;
        }
    }

    public ParseText skipToAnyOf(CharSequence stoppers) {
        if (stoppers.length() == 0) {
            throw new IllegalArgumentException("String must not be empty");
        } else {
            int i = this.index;

            label26:
            for(this.start = i; i < this.text.length(); ++i) {
                char ch = this.text.charAt(i);

                for(int j = 0; j < stoppers.length(); ++j) {
                    if (ch == stoppers.charAt(j)) {
                        break label26;
                    }
                }
            }

            this.index = i;
            return this;
        }
    }

    public ParseText skipTo(CharSequence target) {
        int len = target.length();
        int i = this.index;
        this.start = i;
        int stopper = this.text.length() - len;

        label24:
        while(true) {
            if (i > stopper) {
                i = this.text.length();
                break;
            }

            int j = 0;

            while(true) {
                if (j >= len) {
                    break label24;
                }

                if (this.text.charAt(i + j) != target.charAt(j)) {
                    ++i;
                    break;
                }

                ++j;
            }
        }

        this.index = i;
        return this;
    }

    public boolean matchSpaces() {
        int i = this.index;
        int len = this.text.length();
        if (i < len && this.isSpace(this.text.charAt(i))) {
            this.start = i;

            do {
                ++i;
            } while(i < len && this.isSpace(this.text.charAt(i)));

            this.index = i;
            return true;
        } else {
            return false;
        }
    }

    public ParseText skipSpaces() {
        int i = this.index;
        this.start = i;

        for(int len = this.text.length(); i < len && this.isSpace(this.text.charAt(i)); ++i) {
        }

        this.index = i;
        return this;
    }

    public ParseText skipToSpace() {
        int i = this.index;
        this.start = i;

        for(int len = this.text.length(); i < len && !this.isSpace(this.text.charAt(i)); ++i) {
        }

        this.index = i;
        return this;
    }

    public ParseText skipToEnd() {
        this.start = this.index;
        this.index = this.text.length();
        return this;
    }

    public boolean matchName() {
        int i = this.index;
        int len = this.text.length();
        if (i < len && this.isNameStart(this.text.charAt(i))) {
            this.start = i;

            do {
                ++i;
            } while(i < len && this.isNameContinuation(this.text.charAt(i)));

            this.index = i;
            return true;
        } else {
            return false;
        }
    }

    public boolean isSpace(char ch) {
        return ch == ' ' || ch == '\t' || ch == '\n' || ch == '\r';
    }

    public boolean isDigit(char ch) {
        return ch >= '0' && ch <= '9';
    }

    public boolean isHexDigit(char ch) {
        return ch >= '0' && ch <= '9' || ch >= 'A' && ch <= 'F' || ch >= 'a' && ch <= 'f';
    }

    public boolean isNameStart(char ch) {
        return ch >= 'A' && ch <= 'Z' || ch >= 'a' && ch <= 'z' || ch == '_' || ch == '$';
    }

    public boolean isNameContinuation(char ch) {
        return this.isNameStart(ch) || ch >= '0' && ch <= '9';
    }

    public String toString() {
        int n = this.text.length();
        StringBuilder sb = new StringBuilder(n + 4);
        sb.append('[');
        int i = 0;

        while(true) {
            if (i == this.start) {
                sb.append('~');
            }

            if (i == this.index) {
                sb.append('^');
            }

            if (i >= n) {
                sb.append(']');
                return sb.toString();
            }

            sb.append(this.text.charAt(i++));
        }
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (!(o instanceof ParseText)) {
            return false;
        } else {
            ParseText pt = (ParseText)o;
            if (this.text.length() == pt.text.length() && this.index == pt.index && this.start == pt.start) {
                for(int i = 0; i < this.text.length(); ++i) {
                    if (this.text.charAt(i) != pt.text.charAt(i)) {
                        return false;
                    }
                }

                return true;
            } else {
                return false;
            }
        }
    }

    public int hashCode() {
        int result = this.text.length() + this.index + this.start;

        for(int i = 0; i < this.text.length(); ++i) {
            result += this.text.charAt(i);
        }

        return result;
    }
}
