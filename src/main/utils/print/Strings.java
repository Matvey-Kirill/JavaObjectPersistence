package main.utils.print;

import java.io.IOException;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Objects;
import java.util.function.IntPredicate;

public class Strings {
    private static final String[] numberNamesEnglish = new String[]{"zero", "one", "two", "three", "four", "five", "six", "seven", "eight", "nine", "ten", "eleven", "twelve", "thirteen", "fourteen", "fifteen", "sixteen", "seventeen", "eighteen", "nineteen"};
    private static final String[] tensNamesEnglish = new String[]{"twenty", "thirty", "forty", "fifty", "sixty", "seventy", "eighty", "ninety"};
    private static char[] hexDigits = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
    private static final String emptyString = "";
    private static final String[] emptyStringArray = new String[0];
    private static final int MAX_INT_DIV_10 = 214748364;
    private static final int MAX_INT_MOD_10 = 7;
    private static final long MAX_LONG_DIV_10 = 922337203685477580L;
    private static final int MAX_LONG_MOD_10 = 7;
    private static char[] digits = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
    private static char[] tensDigits = new char[]{'0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '2', '2', '2', '2', '2', '2', '2', '2', '2', '2', '3', '3', '3', '3', '3', '3', '3', '3', '3', '3', '4', '4', '4', '4', '4', '4', '4', '4', '4', '4', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '6', '6', '6', '6', '6', '6', '6', '6', '6', '6', '7', '7', '7', '7', '7', '7', '7', '7', '7', '7', '8', '8', '8', '8', '8', '8', '8', '8', '8', '8', '9', '9', '9', '9', '9', '9', '9', '9', '9', '9'};

    private Strings() throws IllegalAccessException {
        throw new IllegalAccessException("Attempt to instantiate Strings class");
    }

    public static String toEnglish(int n) {
        if (n >= 0 && n < 20) {
            return numberNamesEnglish[n];
        } else {
            StringBuilder sb = new StringBuilder();

            try {
                appendEnglish(sb, n);
            } catch (IOException var3) {
            }

            return sb.toString();
        }
    }

    public static Appendable appendEnglish(Appendable a, int n) throws IOException {
        if (n >= 0 && n < 20) {
            return a.append(numberNamesEnglish[n]);
        } else {
            if (n < 0) {
                a.append("minus ");
                if (n == -2147483648) {
                    a.append("two billion, ");
                    n = 147483648;
                } else {
                    n = -n;
                }
            }

            if (n >= 1000000000) {
                appendEnglish(a, n / 1000000000).append(" billion");
                if ((n %= 1000000000) == 0) {
                    return a;
                }

                a.append(n >= 100 ? ", " : " and ");
            }

            if (n >= 1000000) {
                appendEnglish(a, n / 1000000).append(" million");
                if ((n %= 1000000) == 0) {
                    return a;
                }

                a.append(n >= 100 ? ", " : " and ");
            }

            if (n >= 1000) {
                appendEnglish(a, n / 1000).append(" thousand");
                if ((n %= 1000) == 0) {
                    return a;
                }

                a.append(n >= 100 ? ", " : " and ");
            }

            if (n >= 100) {
                a.append(numberNamesEnglish[n / 100]).append(" hundred");
                if ((n %= 100) == 0) {
                    return a;
                }

                a.append(" and ");
            }

            if (n >= 20) {
                a.append(tensNamesEnglish[n / 10 - 2]);
                if ((n %= 10) != 0) {
                    a.append('-');
                }
            }

            if (n > 0) {
                a.append(numberNamesEnglish[n]);
            }

            return a;
        }
    }

    public static String capitalise(String str) {
        int n = str.length();
        if (n > 0) {
            char ch = str.charAt(0);
            if (Character.isLowerCase(ch)) {
                StringBuilder sb = new StringBuilder(n);
                sb.append(Character.toUpperCase(ch));
                if (n > 1) {
                    sb.append(str, 1, n);
                }

                return sb.toString();
            }
        }

        return str;
    }

    public static String plural(String noun, int n) {
        StringBuilder sb = new StringBuilder();
        sb.append(n).append(' ').append(noun);
        if (n != 1) {
            sb.append('s');
        }

        return sb.toString();
    }

    public static String plural(String singularNoun, String pluralNoun, int n) {
        StringBuilder sb = new StringBuilder();
        sb.append(n).append(' ').append(n == 1 ? singularNoun : pluralNoun);
        return sb.toString();
    }

    public static String[] split(String s) {
        return split(s, 0, s.length(), Character::isWhitespace);
    }

    public static String[] split(String s, int start, int end) {
        return split(s, start, end, Character::isWhitespace);
    }

    public static String[] split(String s, IntPredicate spaceTest) {
        return split(s, 0, s.length(), spaceTest);
    }

    public static String[] split(String s, int start, int end, IntPredicate spaceTest) {
        while(start < end) {
            if (!spaceTest.test(s.charAt(start))) {
                do {
                    --end;
                } while(spaceTest.test(s.charAt(end)));

                int count = 0;
                int i = start + 1;

                while(true) {
                    do {
                        if (i >= end) {
                            String[] result = new String[count + 1];
                            i = start;

                            for(int j = 0; j < count; ++j) {
                                int k = i;

                                do {
                                    ++i;
                                } while(!spaceTest.test(s.charAt(i)));

                                result[j] = s.substring(k, i);

                                do {
                                    ++i;
                                } while(spaceTest.test(s.charAt(i)));
                            }

                            result[count] = s.substring(i, end + 1);
                            return result;
                        }
                    } while(!spaceTest.test(s.charAt(i++)));

                    ++count;

                    while(true) {
                        if (spaceTest.test(s.charAt(i++))) {
                            continue;
                        }
                    }
                }
            }

            ++start;
        }

        return emptyStringArray;
    }

    public static String[] split(String s1, String s2) {
        int count = 0;
        int i = 0;
        int n2 = s2.length();
        int stopper = s1.length() - n2;

        while(i <= stopper) {
            if (s1.regionMatches(i, s2, 0, n2)) {
                ++count;
                i += n2;
            } else {
                ++i;
            }
        }

        String[] result = new String[count + 1];
        i = 0;

        for(int j = 0; j < count; ++j) {
            int k;
            for(k = i; !s1.regionMatches(i, s2, 0, n2); ++i) {
            }

            result[j] = s1.substring(k, i);
            i += n2;
        }

        result[count] = s1.substring(i);
        return result;
    }

    public static String[] split(String s, char separator) {
        return split(s, 0, s.length(), separator, true, Character::isWhitespace);
    }

    public static String[] split(String s, char separator, boolean skipEmpty, IntPredicate spaceTest) {
        return split(s, 0, s.length(), separator, skipEmpty, spaceTest);
    }

    public static String[] split(String s, int start, int end, char separator, boolean skipEmpty, IntPredicate spaceTest) {
        int count = 0;
        int i = start;
        if (skipEmpty) {
            if (spaceTest != null) {
                while(true) {
                    boolean nonSpaceSeen;
                    for(nonSpaceSeen = false; i < end; ++i) {
                        char ch = s.charAt(i);
                        if (ch == separator) {
                            break;
                        }

                        nonSpaceSeen = nonSpaceSeen || !spaceTest.test(ch);
                    }

                    if (nonSpaceSeen) {
                        ++count;
                    }

                    if (i >= end) {
                        break;
                    }

                    ++i;
                }
            } else {
                while(true) {
                    int itemStart;
                    for(itemStart = i; i < end && s.charAt(i) != separator; ++i) {
                    }

                    if (i > itemStart) {
                        ++count;
                    }

                    if (i >= end) {
                        break;
                    }

                    ++i;
                }
            }

            if (count == 0) {
                return emptyStringArray;
            }
        } else {
            count = 1;

            while(i < end) {
                if (s.charAt(i++) == separator) {
                    ++count;
                }
            }
        }

        String[] result = new String[count];
        i = start;
        int var14 = 0;

        while(true) {
            int itemStart;
            for(itemStart = i; i < end && s.charAt(i) != separator; ++i) {
            }

            int itemEnd = i;
            if (spaceTest != null) {
                while(itemStart < itemEnd && spaceTest.test(s.charAt(itemStart))) {
                    ++itemStart;
                }

                while(itemStart < itemEnd && spaceTest.test(s.charAt(itemEnd - 1))) {
                    --itemEnd;
                }
            }

            if (itemEnd > itemStart) {
                result[var14++] = s.substring(itemStart, itemEnd);
            } else if (!skipEmpty) {
                result[var14++] = "";
            }

            if (i >= end) {
                return result;
            }

            ++i;
        }
    }

    public static <E> String join(Iterable<E> collection) {
        return join(collection.iterator());
    }

    public static <E> String join(Iterator<E> it) {
        if (!it.hasNext()) {
            return "";
        } else {
            StringBuilder sb = new StringBuilder();

            do {
                sb.append(it.next());
            } while(it.hasNext());

            return sb.length() == 0 ? "" : sb.toString();
        }
    }

    public static <E> String join(Enumeration<E> e) {
        if (!e.hasMoreElements()) {
            return "";
        } else {
            StringBuilder sb = new StringBuilder();

            do {
                sb.append(e.nextElement());
            } while(e.hasMoreElements());

            return sb.length() == 0 ? "" : sb.toString();
        }
    }

    public static <E> String join(E[] array) {
        int n = array.length;
        if (n == 0) {
            return "";
        } else {
            int i = 0;
            StringBuilder sb = new StringBuilder();

            do {
                sb.append(array[i++]);
            } while(i < n);

            return sb.length() == 0 ? "" : sb.toString();
        }
    }

    public static <E> String join(Iterable<E> collection, char separator) {
        return join(collection.iterator(), separator);
    }

    public static <E> String join(Iterator<E> it, char separator) {
        if (!it.hasNext()) {
            return "";
        } else {
            StringBuilder sb = new StringBuilder();

            while(true) {
                sb.append(it.next());
                if (!it.hasNext()) {
                    return sb.length() == 0 ? "" : sb.toString();
                }

                sb.append(separator);
            }
        }
    }

    public static <E> String join(Enumeration<E> e, char separator) {
        if (!e.hasMoreElements()) {
            return "";
        } else {
            StringBuilder sb = new StringBuilder();

            while(true) {
                sb.append(e.nextElement());
                if (!e.hasMoreElements()) {
                    return sb.length() == 0 ? "" : sb.toString();
                }

                sb.append(separator);
            }
        }
    }

    public static <E> String join(E[] array, char separator) {
        int n = array.length;
        if (n == 0) {
            return "";
        } else {
            int i = 0;
            StringBuilder sb = new StringBuilder();

            while(true) {
                sb.append(array[i++]);
                if (i >= n) {
                    return sb.length() == 0 ? "" : sb.toString();
                }

                sb.append(separator);
            }
        }
    }

    public static <E> String join(Iterable<E> collection, String separator) {
        return join(collection.iterator(), separator);
    }

    public static <E> String join(Iterator<E> it, String separator) {
        if (!it.hasNext()) {
            return "";
        } else {
            StringBuilder sb = new StringBuilder();

            while(true) {
                sb.append(it.next());
                if (!it.hasNext()) {
                    return sb.length() == 0 ? "" : sb.toString();
                }

                sb.append(separator);
            }
        }
    }

    public static <E> String join(Enumeration<E> e, String separator) {
        if (!e.hasMoreElements()) {
            return "";
        } else {
            StringBuilder sb = new StringBuilder();

            while(true) {
                sb.append(e.nextElement());
                if (!e.hasMoreElements()) {
                    return sb.length() == 0 ? "" : sb.toString();
                }

                sb.append(separator);
            }
        }
    }

    public static <E> String join(E[] array, String separator) {
        int n = array.length;
        if (n == 0) {
            return "";
        } else {
            int i = 0;
            StringBuilder sb = new StringBuilder();

            while(true) {
                sb.append(array[i++]);
                if (i >= n) {
                    return sb.length() == 0 ? "" : sb.toString();
                }

                sb.append(separator);
            }
        }
    }

    public static final String escape(String s, CharMapper mapper) {
        int i = 0;
        int n = s.length();

        String mapped;
        do {
            if (i >= n) {
                return s;
            }

            mapped = mapper.map(s.charAt(i++));
        } while(mapped == null);

        StringBuilder sb = new StringBuilder();
        sb.append(s, 0, i - 1);
        sb.append(mapped);

        try {
            appendEscaped(sb, s, i, n, mapper);
        } catch (IOException var7) {
        }

        return sb.toString();
    }

    public static final CharSequence escape(CharSequence s, CharMapper mapper) {
        int i = 0;
        int n = s.length();

        String mapped;
        do {
            if (i >= n) {
                return s;
            }

            mapped = mapper.map(s.charAt(i++));
        } while(mapped == null);

        StringBuilder sb = new StringBuilder();
        sb.append(s, 0, i - 1);
        sb.append(mapped);

        try {
            appendEscaped(sb, s, i, n, mapper);
        } catch (IOException var7) {
        }

        return sb;
    }

    public static final void appendEscaped(Appendable a, CharSequence s, int index, int end, CharMapper mapper) throws IOException {
        while(index < end) {
            char ch = s.charAt(index++);
            String mapped = mapper.map(ch);
            if (mapped != null) {
                a.append(mapped);
            } else {
                a.append(ch);
            }
        }

    }

    public static final void appendEscaped(Appendable a, CharSequence s, CharMapper mapper) throws IOException {
        appendEscaped(a, s, 0, s.length(), mapper);
    }

    public static final String escapeUTF16(String s, CharMapper mapper) {
        int i = 0;
        int n = s.length();

        int k;
        String mapped;
        do {
            if (i >= n) {
                return s;
            }

            k = i;
            char ch1 = s.charAt(i++);
            if (Character.isHighSurrogate(ch1)) {
                char ch2;
                if (i >= n || !Character.isLowSurrogate(ch2 = s.charAt(i++))) {
                    throw new IllegalArgumentException("Illegal surrogate sequence");
                }

                mapped = mapper.map(Character.toCodePoint(ch1, ch2));
            } else {
                mapped = mapper.map(ch1);
            }
        } while(mapped == null);

        StringBuilder sb = new StringBuilder();
        sb.append(s, 0, k);
        sb.append(mapped);

        try {
            appendEscapedUTF16(sb, s, i, n, mapper);
        } catch (IOException var9) {
        }

        return sb.toString();
    }

    public static final CharSequence escapeUTF16(CharSequence s, CharMapper mapper) {
        int i = 0;
        int n = s.length();

        int k;
        String mapped;
        do {
            if (i >= n) {
                return s;
            }

            k = i;
            char ch1 = s.charAt(i++);
            if (Character.isHighSurrogate(ch1)) {
                char ch2;
                if (i >= n || !Character.isLowSurrogate(ch2 = s.charAt(i++))) {
                    throw new IllegalArgumentException("Illegal surrogate sequence");
                }

                mapped = mapper.map(Character.toCodePoint(ch1, ch2));
            } else {
                mapped = mapper.map(ch1);
            }
        } while(mapped == null);

        StringBuilder sb = new StringBuilder();
        sb.append(s, 0, k);
        sb.append(mapped);

        try {
            appendEscapedUTF16(sb, s, i, n, mapper);
        } catch (IOException var9) {
        }

        return sb;
    }

    public static final void appendEscapedUTF16(Appendable a, CharSequence s, int index, int end, CharMapper mapper) throws IOException {
        while(index < end) {
            char ch1 = s.charAt(index++);
            String mapped;
            if (Character.isHighSurrogate(ch1)) {
                char ch2;
                if (index >= end || !Character.isLowSurrogate(ch2 = s.charAt(index++))) {
                    throw new IllegalArgumentException("Illegal surrogate sequence");
                }

                mapped = mapper.map(Character.toCodePoint(ch1, ch2));
                if (mapped != null) {
                    a.append(mapped);
                } else {
                    a.append(ch1).append(ch2);
                }
            } else {
                mapped = mapper.map(ch1);
                if (mapped != null) {
                    a.append(mapped);
                } else {
                    a.append(ch1);
                }
            }
        }

    }

    public static final void appendEscapedUTF16(Appendable a, CharSequence s, CharMapper mapper) throws IOException {
        appendEscapedUTF16(a, s, 0, s.length(), mapper);
    }

    public static String unescape(String s, CharUnmapper unmapper) {
        int i = 0;

        for(int n = s.length(); i < n; ++i) {
            if (unmapper.isEscape(s, i)) {
                StringBuilder sb = new StringBuilder(s.length());
                sb.append(s, 0, i);
                i += unmapper.unmap(sb, s, i);

                while(i < n) {
                    if (unmapper.isEscape(s, i)) {
                        i += unmapper.unmap(sb, s, i);
                    } else {
                        sb.append(s.charAt(i++));
                    }
                }

                return sb.toString();
            }
        }

        return s;
    }

    public static CharSequence unescape(CharSequence s, CharUnmapper unmapper) {
        int i = 0;

        for(int n = s.length(); i < n; ++i) {
            if (unmapper.isEscape(s, i)) {
                StringBuilder sb = new StringBuilder(s.length());
                sb.append(s, 0, i);
                i += unmapper.unmap(sb, s, i);

                while(i < n) {
                    if (unmapper.isEscape(s, i)) {
                        i += unmapper.unmap(sb, s, i);
                    } else {
                        sb.append(s.charAt(i++));
                    }
                }

                return sb;
            }
        }

        return s;
    }

    public static String toUTF16(int codePoint) {
        StringBuilder sb = new StringBuilder(2);

        try {
            appendUTF16(sb, codePoint);
        } catch (IOException var3) {
        }

        return sb.toString();
    }

    public static String toUTF16(int[] codePoints) {
        StringBuilder sb = new StringBuilder();

        for(int i = 0; i < codePoints.length; ++i) {
            try {
                appendUTF16(sb, codePoints[i]);
            } catch (IOException var4) {
            }
        }

        return sb.toString();
    }

    public static void appendUTF16(Appendable a, int codePoint) throws IOException {
        if (Character.isSupplementaryCodePoint(codePoint)) {
            a.append(Character.highSurrogate(codePoint));
            a.append(Character.lowSurrogate(codePoint));
        } else {
            if (!Character.isBmpCodePoint(codePoint) || Character.isSurrogate((char)codePoint)) {
                throw new IllegalArgumentException("Illegal character for UTF-16");
            }

            a.append((char)codePoint);
        }

    }

    public static byte[] toUTF8(String str) {
        if (str == null) {
            throw new IllegalArgumentException("String must not be null");
        } else {
            return toUTF8(str, 0, str.length());
        }
    }

    public static String fromUTF8(byte[] bytes) {
        if (bytes == null) {
            throw new IllegalArgumentException("Byte array must not be null");
        } else {
            return fromUTF8(bytes, 0, bytes.length);
        }
    }

    public static byte[] toUTF8(String str, int start, int end) {
        if (str == null) {
            throw new IllegalArgumentException("String must not be null");
        } else {
            ByteArrayBuilder bab = new ByteArrayBuilder((end - start) * 5 / 4);

            for(int i = start; i < end; ++i) {
                char ch = str.charAt(i);
                int codePoint;
                if (Character.isHighSurrogate(ch)) {
                    ++i;
                    char lowSurrogate;
                    if (i >= end || !Character.isLowSurrogate(lowSurrogate = str.charAt(i))) {
                        throw new IllegalArgumentException("Invalid UTF-16 surrogate sequence");
                    }

                    codePoint = Character.toCodePoint(ch, lowSurrogate);
                } else {
                    codePoint = ch;
                }

                appendUTF8(bab, codePoint);
            }

            return bab.toByteArray();
        }
    }

    public static void appendUTF8(ByteArrayBuilder bab, int codepoint) {
        if (codepoint <= 127) {
            bab.append(codepoint);
        } else if (codepoint <= 2047) {
            bab.append(codepoint >> 6 | 192);
            bab.append(codepoint & 63 | 128);
        } else if (codepoint <= 65535) {
            bab.append(codepoint >> 12 | 224);
            bab.append(codepoint >> 6 & 63 | 128);
            bab.append(codepoint & 63 | 128);
        } else {
            bab.append(codepoint >> 18 & 7 | 240);
            bab.append(codepoint >> 12 & 63 | 128);
            bab.append(codepoint >> 6 & 63 | 128);
            bab.append(codepoint & 63 | 128);
        }

    }

    public static String fromUTF8(byte[] bytes, int start, int end) {
        if (bytes == null) {
            throw new IllegalArgumentException("Byte array must not be null");
        } else if (start >= 0 && start <= bytes.length) {
            if (end >= start && end <= bytes.length) {
                StringBuilder sb = new StringBuilder();

                for(int i = start; i < end; ++i) {
                    int b = bytes[i];
                    if ((b & 128) == 0) {
                        sb.append((char)b);
                    } else {
                        if ((b & 64) == 0) {
                            throw new IllegalArgumentException("Illegal character in UTF-8 bytes");
                        }

                        int codePoint;
                        if ((b & 32) == 0) {
                            codePoint = b & 31;
                            ++i;
                            codePoint = addToCodePoint(codePoint, bytes, i, end);
                            sb.append((char)codePoint);
                        } else if ((b & 16) == 0) {
                            codePoint = b & 15;
                            ++i;
                            codePoint = addToCodePoint(codePoint, bytes, i, end);
                            ++i;
                            codePoint = addToCodePoint(codePoint, bytes, i, end);
                            sb.append((char)codePoint);
                        } else {
                            codePoint = b & 7;
                            ++i;
                            codePoint = addToCodePoint(codePoint, bytes, i, end);
                            ++i;
                            codePoint = addToCodePoint(codePoint, bytes, i, end);
                            ++i;
                            codePoint = addToCodePoint(codePoint, bytes, i, end);

                            try {
                                appendUTF16(sb, codePoint);
                            } catch (IOException var8) {
                            }
                        }
                    }
                }

                return sb.toString();
            } else {
                throw new IllegalArgumentException("End index invalid: " + end);
            }
        } else {
            throw new IllegalArgumentException("Start index invalid: " + start);
        }
    }

    private static int addToCodePoint(int codePoint, byte[] bytes, int index, int end) {
        if (index >= end) {
            throw new IllegalArgumentException("Incomplete sequence in UTF-8 bytes");
        } else {
            int b = bytes[index];
            if ((b & 192) != 128) {
                throw new IllegalArgumentException("Illegal character in UTF-8 bytes");
            } else {
                return codePoint << 6 | b & 63;
            }
        }
    }

    public static String toHex(CharSequence s) {
        if (s == null) {
            throw new IllegalArgumentException("argument must not be null");
        } else {
            int n = s.length();
            if (n == 0) {
                return "";
            } else {
                StringBuilder sb = new StringBuilder();

                for(int i = 0; i < n; ++i) {
                    try {
                        appendHex(sb, (char)s.charAt(i));
                    } catch (IOException var5) {
                    }
                }

                return sb.toString();
            }
        }
    }

    public static String toHex(CharSequence s, char separator) {
        if (s == null) {
            throw new IllegalArgumentException("argument must not be null");
        } else {
            int n = s.length();
            if (n == 0) {
                return "";
            } else {
                StringBuilder sb = new StringBuilder();
                int i = 0;

                while(true) {
                    try {
                        appendHex(sb, (char)s.charAt(i++));
                    } catch (IOException var6) {
                    }

                    if (i >= n) {
                        return sb.toString();
                    }

                    sb.append(separator);
                }
            }
        }
    }

    public static String toHex(byte[] bytes) {
        if (bytes == null) {
            throw new IllegalArgumentException("argument must not be null");
        } else {
            int n = bytes.length;
            if (n == 0) {
                return "";
            } else {
                StringBuilder sb = new StringBuilder();

                for(int i = 0; i < n; ++i) {
                    try {
                        appendHex(sb, (byte)bytes[i]);
                    } catch (IOException var5) {
                    }
                }

                return sb.toString();
            }
        }
    }

    public static String toHex(byte[] bytes, char separator) {
        if (bytes == null) {
            throw new IllegalArgumentException("argument must not be null");
        } else {
            int n = bytes.length;
            if (n == 0) {
                return "";
            } else {
                StringBuilder sb = new StringBuilder();
                byte i = 0;

                while(true) {
                    try {
                        appendHex(sb, (byte)bytes[i]);
                    } catch (IOException var6) {
                    }

                    if (i >= n) {
                        return sb.toString();
                    }

                    sb.append(separator);
                }
            }
        }
    }

    public static String toHex(byte b) {
        StringBuilder sb = new StringBuilder(2);

        try {
            appendHex(sb, (byte)b);
        } catch (IOException var3) {
        }

        return sb.toString();
    }

    public static String toHex(char ch) {
        StringBuilder sb = new StringBuilder(4);

        try {
            appendHex(sb, (char)ch);
        } catch (IOException var3) {
        }

        return sb.toString();
    }

    public static String toHex(int i) {
        StringBuilder sb = new StringBuilder(8);

        try {
            appendHex(sb, (int)i);
        } catch (IOException var3) {
        }

        return sb.toString();
    }

    public static String toHex(long n) {
        StringBuilder sb = new StringBuilder(16);

        try {
            appendHex(sb, n);
        } catch (IOException var4) {
        }

        return sb.toString();
    }

    public static void appendHex(Appendable a, byte b) throws IOException {
        a.append(hexDigits[b >>> 4 & 15]);
        a.append(hexDigits[b & 15]);
    }

    public static void appendHex(Appendable a, char ch) throws IOException {
        appendHex(a, (byte)(ch >>> 8));
        appendHex(a, (byte)ch);
    }

    public static void appendHex(Appendable a, int i) throws IOException {
        appendHex(a, (char)(i >>> 16));
        appendHex(a, (char)i);
    }

    public static void appendHex(Appendable a, int i, int digits) throws IOException {
        if (digits > 0) {
            appendHex(a, i >>> 4, digits - 1);
            a.append(hexDigits[i & 15]);
        }

    }

    public static void appendHex(Appendable a, long n) throws IOException {
        appendHex(a, (int)(n >>> 32));
        appendHex(a, (int)n);
    }

    public static void appendHex(Appendable a, long n, int digits) throws IOException {
        if (digits > 0) {
            appendHex(a, n >>> 4, digits - 1);
            a.append(hexDigits[(int)(n & 15L)]);
        }

    }

    public static int convertToInt(CharSequence text, int start, int end) {
        if (start >= 0 && end <= text.length() && start < end) {
            int result = 0;

            for(int i = start; i < end; ++i) {
                int n = convertDecDigit(text.charAt(i));
                if (result > 214748364 || result == 214748364 && n > 7) {
                    throw new NumberFormatException();
                }

                result = result * 10 + n;
            }

            return result;
        } else {
            throw new IndexOutOfBoundsException();
        }
    }

    public static long convertToLong(CharSequence text, int start, int end) {
        if (start >= 0 && end <= text.length() && start < end) {
            long result = 0L;

            for(int i = start; i < end; ++i) {
                int n = convertDecDigit(text.charAt(i));
                if (result > 922337203685477580L || result == 922337203685477580L && n > 7) {
                    throw new NumberFormatException();
                }

                result = result * 10L + (long)n;
            }

            return result;
        } else {
            throw new IndexOutOfBoundsException();
        }
    }

    public static int convertDecDigit(char ch) {
        if (ch >= '0' && ch <= '9') {
            return ch - 48;
        } else {
            throw new NumberFormatException();
        }
    }

    public static int convertHexToInt(CharSequence text, int start, int end) {
        if (start >= 0 && end <= text.length() && start < end) {
            int result = 0;

            for(int i = start; i < end; ++i) {
                if ((result & -134217728) != 0) {
                    throw new NumberFormatException();
                }

                result = result << 4 | convertHexDigit(text.charAt(i));
            }

            return result;
        } else {
            throw new IndexOutOfBoundsException();
        }
    }

    public static long convertHexToLong(CharSequence text, int start, int end) {
        if (start >= 0 && end <= text.length() && start < end) {
            long result = 0L;

            for(int i = start; i < end; ++i) {
                if ((result & -576460752303423488L) != 0L) {
                    throw new NumberFormatException();
                }

                result = result << 4 | (long)convertHexDigit(text.charAt(i));
            }

            return result;
        } else {
            throw new IndexOutOfBoundsException();
        }
    }

    public static int convertHexDigit(char ch) {
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

    public static boolean multiWildcardCompare(String pattern, CharSequence target) {
        int patIndex = 0;

        while(true) {
            int i = pattern.indexOf(124, patIndex);
            if (i < 0) {
                return wildcardCompare(pattern, patIndex, pattern.length(), target);
            }

            if (wildcardCompare(pattern, patIndex, i, target)) {
                return true;
            }

            patIndex = i + 1;
        }
    }

    public static boolean wildcardCompare(String pattern, CharSequence target) {
        return wildcardCompare(pattern, 0, pattern.length(), target);
    }

    public static boolean wildcardCompare(String pattern, int patIndex, int patEnd, CharSequence target) {
        int tarLen = target.length();
        int i = pattern.indexOf(42, patIndex);
        if (i >= 0 && i < patEnd) {
            if (i - patIndex <= tarLen && wildcardCompareSubstring(pattern, patIndex, i, target, 0)) {
                int tarIndex = i - patIndex;
                patIndex = i + 1;

                label46:
                while(true) {
                    i = pattern.indexOf(42, patIndex);
                    if (i < 0 || i >= patEnd) {
                        i = tarLen - (patEnd - patIndex);
                        return tarIndex <= i && wildcardCompareSubstring(pattern, patIndex, patEnd, target, i);
                    }

                    for(i -= patIndex; tarIndex + i <= tarLen; ++tarIndex) {
                        if (wildcardCompareSubstring(pattern, patIndex, patIndex + i, target, tarIndex)) {
                            tarIndex += i;
                            patIndex += i + 1;
                            continue label46;
                        }
                    }

                    return false;
                }
            } else {
                return false;
            }
        } else {
            return tarLen == patEnd - patIndex && wildcardCompareSubstring(pattern, patIndex, patEnd, target, 0);
        }
    }

    private static boolean wildcardCompareSubstring(String pattern, int patIndex, int patEnd, CharSequence target, int index) {
        while(true) {
            if (patIndex < patEnd) {
                char ch = pattern.charAt(patIndex++);
                if (target.charAt(index++) == ch || ch == '?') {
                    continue;
                }

                return false;
            }

            return true;
        }
    }

    public static String trim(String s, IntPredicate spaceTest) {
        Objects.requireNonNull(spaceTest);
        int start = 0;

        for(int end = s.length(); start < end; ++start) {
            if (!spaceTest.test(s.charAt(start))) {
                while(spaceTest.test(s.charAt(end - 1))) {
                    --end;
                }

                return start == 0 && end == s.length() ? s : s.substring(start, end);
            }
        }

        return "";
    }

    public static CharSequence trim(CharSequence cs, IntPredicate spaceTest) {
        Objects.requireNonNull(spaceTest);
        int start = 0;

        for(int end = cs.length(); start < end; ++start) {
            if (!spaceTest.test(cs.charAt(start))) {
                while(spaceTest.test(cs.charAt(end - 1))) {
                    --end;
                }

                return (CharSequence)(start == 0 && end == cs.length() ? cs : new SubSequence(cs, start, end));
            }
        }

        return "";
    }

    public static String trim(String s) {
        return trim(s, Character::isWhitespace);
    }

    public static CharSequence trim(CharSequence cs) {
        return trim(cs, Character::isWhitespace);
    }

    public static String trimUTF16(String s, IntPredicate spaceTest) {
        Objects.requireNonNull(spaceTest);
        int start = 0;
        int end = s.length();

        char lo;
        char hi;
        while(true) {
            if (start >= end) {
                return "";
            }

            lo = s.charAt(start);
            if (Character.isHighSurrogate(lo) && start + 1 < end) {
                hi = s.charAt(start + 1);
                if (Character.isLowSurrogate(hi)) {
                    if (!spaceTest.test(Character.toCodePoint(lo, hi))) {
                        break;
                    }

                    start += 2;
                    continue;
                }
            }

            if (!spaceTest.test(lo)) {
                break;
            }

            ++start;
        }

        while(end > start) {
            lo = s.charAt(end - 1);
            if (Character.isLowSurrogate(lo) && end - 1 > start) {
                hi = s.charAt(end - 2);
                if (Character.isHighSurrogate(hi)) {
                    if (!spaceTest.test(Character.toCodePoint(hi, lo))) {
                        break;
                    }

                    end -= 2;
                    continue;
                }
            }

            if (!spaceTest.test(lo)) {
                break;
            }

            --end;
        }

        return start == 0 && end == s.length() ? s : s.substring(start, end);
    }

    public static StringBuilder build() {
        return new StringBuilder();
    }

    public static StringBuilder build(CharSequence cs) {
        return new StringBuilder(cs);
    }

    public static String toIdentifier(int i) {
        StringBuilder sb = new StringBuilder();
        i = Math.abs(i);

        do {
            sb.insert(0, (char)(i % 26 + 65));
            i = i / 26 - 1;
        } while(i >= 0);

        return sb.toString();
    }

    public static void appendInt(Appendable a, int i) throws IOException {
        if (i < 0) {
            if (i == -2147483648) {
                a.append("-2147483648");
                return;
            }

            a.append('-');
            appendPositiveInt(a, -i);
        } else {
            appendPositiveInt(a, i);
        }

    }

    public static void appendPositiveInt(Appendable a, int i) throws IOException {
        if (i >= 100) {
            int n = i / 100;
            appendPositiveInt(a, n);
            i -= n * 100;
            a.append(tensDigits[i]);
            a.append(digits[i]);
        } else if (i >= 10) {
            a.append(tensDigits[i]);
            a.append(digits[i]);
        } else {
            a.append(digits[i]);
        }

    }

    public static void appendLong(Appendable a, long n) throws IOException {
        if (n < 0L) {
            if (n == -9223372036854775808L) {
                a.append("-9223372036854775808");
                return;
            }

            a.append('-');
            appendPositiveLong(a, -n);
        } else {
            appendPositiveLong(a, n);
        }

    }

    public static void appendPositiveLong(Appendable a, long n) throws IOException {
        if (n >= 100L) {
            long m = n / 100L;
            appendPositiveLong(a, m);
            int i = (int)(n - m * 100L);
            a.append(tensDigits[i]);
            a.append(digits[i]);
        } else if (n >= 10L) {
            a.append(tensDigits[(int)n]);
            a.append(digits[(int)n]);
        } else {
            a.append(digits[(int)n]);
        }

    }

    public static void append2Digits(Appendable a, int i) throws IOException {
        a.append(tensDigits[i]);
        a.append(digits[i]);
    }

    public static void append3Digits(Appendable a, int i) throws IOException {
        int n = i / 100;
        a.append(digits[n]);
        i -= n * 100;
        a.append(tensDigits[i]);
        a.append(digits[i]);
    }
}
