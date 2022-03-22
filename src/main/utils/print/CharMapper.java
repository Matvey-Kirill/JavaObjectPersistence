package main.utils.print;

import java.io.IOException;

@FunctionalInterface
public interface CharMapper {
    String map(int var1);

    static String arrayMapping(String[] array, int codePoint, int base) {
        return codePoint >= base && codePoint < base + array.length ? array[codePoint - base] : null;
    }

    static String lookupMapping(String[][] table, int codePoint) {
        int hi = table.length;
        if (hi > 0 && codePoint >= table[0][0].charAt(0) && codePoint <= table[hi - 1][0].charAt(0)) {
            int lo = 0;

            while(lo < hi) {
                int mid = lo + hi >>> 1;
                String[] entry = table[mid];
                char ch = entry[0].charAt(0);
                if (codePoint == ch) {
                    return entry[1];
                }

                if (codePoint < ch) {
                    hi = mid;
                } else {
                    lo = mid + 1;
                }
            }
        }

        return null;
    }

    static String lookupMapping(CharMapperEntry[] table, int codePoint) {
        int hi = table.length;
        if (hi > 0 && codePoint >= table[0].getCodePoint() && codePoint <= table[hi - 1].getCodePoint()) {
            int lo = 0;

            while(lo < hi) {
                int mid = lo + hi >>> 1;
                CharMapperEntry entry = table[mid];
                int entryCodePoint = entry.getCodePoint();
                if (codePoint == entryCodePoint) {
                    return entry.getString();
                }

                if (codePoint < entryCodePoint) {
                    hi = mid;
                } else {
                    lo = mid + 1;
                }
            }
        }

        return null;
    }

    static String decimalMapping(int codePoint, String prefix, String suffix) {
        StringBuilder sb = new StringBuilder(prefix.length() + 7 + (suffix != null ? suffix.length() : 0));
        sb.append(prefix);

        try {
            Strings.appendPositiveInt(sb, codePoint);
        } catch (IOException var5) {
        }

        if (suffix != null) {
            sb.append(suffix);
        }

        return sb.toString();
    }

    static String hexMapping(int codePoint, int length, String prefix, String suffix) {
        StringBuilder sb = new StringBuilder(prefix.length() + length + (suffix != null ? suffix.length() : 0));
        sb.append(prefix);

        try {
            Strings.appendHex(sb, codePoint, length);
        } catch (IOException var6) {
        }

        if (suffix != null) {
            sb.append(suffix);
        }

        return sb.toString();
    }

    static String hexMapping(int codePoint, int length, String prefix) {
        StringBuilder sb = new StringBuilder(prefix.length() + length);
        sb.append(prefix);

        try {
            Strings.appendHex(sb, codePoint, length);
        } catch (IOException var5) {
        }

        return sb.toString();
    }
}
