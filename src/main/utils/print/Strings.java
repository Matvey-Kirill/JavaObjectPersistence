package main.utils.print;

import java.io.IOException;

public class Strings {
    private static char[] hexDigits = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
    private static char[] digits = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
    private static char[] tensDigits = new char[]{'0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '2', '2', '2', '2', '2', '2', '2', '2', '2', '2', '3', '3', '3', '3', '3', '3', '3', '3', '3', '3', '4', '4', '4', '4', '4', '4', '4', '4', '4', '4', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '6', '6', '6', '6', '6', '6', '6', '6', '6', '6', '7', '7', '7', '7', '7', '7', '7', '7', '7', '7', '8', '8', '8', '8', '8', '8', '8', '8', '8', '8', '9', '9', '9', '9', '9', '9', '9', '9', '9', '9'};

    private Strings() throws IllegalAccessException {
        throw new IllegalAccessException("Attempt to instantiate Strings class");
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

    public static void appendHex(Appendable a, byte b) throws IOException {
        a.append(hexDigits[b >>> 4 & 15]);
        a.append(hexDigits[b & 15]);
    }

    public static void appendHex(Appendable a, char ch) throws IOException {
        appendHex(a, (byte)(ch >>> 8));
        appendHex(a, (byte)ch);
    }

    public static void appendHex(Appendable a, int i, int digits) throws IOException {
        if (digits > 0) {
            appendHex(a, i >>> 4, digits - 1);
            a.append(hexDigits[i & 15]);
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
