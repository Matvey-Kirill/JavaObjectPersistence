package main.utils.types;

import main.utils.print.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;

public class JSON {
    public static final String INVALID_CHAR_SEQ = "Invalid JSON character sequence";
    public static final String EXCESS_CHARS = "Excess characters after JSON value";
    public static final String ILLEGAL_KEY = "Illegal key in JSON object";
    public static final String DUPLICATE_KEY = "Duplicate key in JSON object";
    public static final String MISSING_COLON = "Missing colon in JSON object";
    public static final String MISSING_CLOSING_BRACE = "Missing closing brace in JSON object";
    public static final String MISSING_CLOSING_BRACKET = "Missing closing bracket in JSON array";
    public static final String ILLEGAL_NUMBER = "Illegal JSON number";
    public static final String ILLEGAL_SYNTAX = "Illegal JSON syntax";
    public static final String ILLEGAL_STRING_TERM = "Unterminated JSON string";
    public static final String ILLEGAL_STRING_CHAR = "Illegal character in JSON string";
    public static final String ILLEGAL_STRING_UNICODE = "Illegal Unicode sequence in JSON string";
    public static final String ILLEGAL_STRING_ESCAPE = "Illegal escape sequence in JSON string";
    public static final String NOT_A_STRING = "Not a JSON string";
    public static final String NOT_A_NUMBER = "Not a JSON number";
    public static final String NOT_A_BOOLEAN = "Not a JSON boolean";
    public static final String NOT_AN_ARRAY = "Not a JSON array";
    public static final String NOT_AN_OBJECT = "Not a JSON object";
    public static final CharMapper charMapper = (codePoint) -> {
        if (codePoint == 34) {
            return "\\\"";
        } else if (codePoint == 92) {
            return "\\\\";
        } else if (codePoint >= 32 && codePoint < 127) {
            return null;
        } else if (codePoint == 8) {
            return "\\b";
        } else if (codePoint == 12) {
            return "\\f";
        } else if (codePoint == 10) {
            return "\\n";
        } else if (codePoint == 13) {
            return "\\r";
        } else if (codePoint == 9) {
            return "\\t";
        } else {
            StringBuilder sb = new StringBuilder("\\u");

            try {
                if (Character.isBmpCodePoint(codePoint)) {
                    Strings.appendHex(sb, (char)codePoint);
                } else {
                    Strings.appendHex(sb, Character.highSurrogate(codePoint));
                    sb.append("\\u");
                    Strings.appendHex(sb, Character.lowSurrogate(codePoint));
                }
            } catch (IOException var3) {
            }

            return sb.toString();
        }
    };
    public static final CharUnmapper charUnmapper = new CharUnmapper() {
        public boolean isEscape(CharSequence s, int offset) {
            return s.charAt(offset) == '\\';
        }

        public int unmap(StringBuilder sb, CharSequence s, int offset) {
            if (offset + 1 >= s.length()) {
                throw new JSONException("Invalid JSON character sequence");
            } else {
                char ch = s.charAt(offset + 1);
                if (ch == '"') {
                    sb.append('"');
                    return 2;
                } else if (ch == '\\') {
                    sb.append('\\');
                    return 2;
                } else if (ch == '/') {
                    sb.append('/');
                    return 2;
                } else if (ch == 'b') {
                    sb.append('\b');
                    return 2;
                } else if (ch == 'f') {
                    sb.append('\f');
                    return 2;
                } else if (ch == 'n') {
                    sb.append('\n');
                    return 2;
                } else if (ch == 'r') {
                    sb.append('\r');
                    return 2;
                } else if (ch == 't') {
                    sb.append('\t');
                    return 2;
                } else if (ch == 'u' && offset + 6 <= s.length()) {
                    int n = Strings.convertHexToInt(s, offset + 2, offset + 6);
                    sb.append((char)n);
                    return 6;
                } else {
                    throw new JSONException("Invalid JSON character sequence");
                }
            }
        }
    };

    private JSON() throws IllegalAccessException {
        throw new IllegalAccessException("Attempt to instantiate JSON");
    }

    public static JSONValue parse(File f) throws IOException {
        InputStream is = new FileInputStream(f);
        Throwable var2 = null;

        JSONValue var3;
        try {
            var3 = parse((InputStream)is);
        } catch (Throwable var12) {
            var2 = var12;
            throw var12;
        } finally {
            if (is != null) {
                if (var2 != null) {
                    try {
                        is.close();
                    } catch (Throwable var11) {
                        var2.addSuppressed(var11);
                    }
                } else {
                    is.close();
                }
            }

        }

        return var3;
    }

    public static JSONArray parseArray(File f) throws IOException {
        return (JSONArray)parse(f);
    }

    public static JSONObject parseObject(File f) throws IOException {
        return (JSONObject)parse(f);
    }

    public static JSONValue parse(File f, Charset charSet) throws IOException {
        InputStream is = new FileInputStream(f);
        Throwable var3 = null;

        JSONValue var4;
        try {
            var4 = parse((InputStream)is, (Charset)charSet);
        } catch (Throwable var13) {
            var3 = var13;
            throw var13;
        } finally {
            if (is != null) {
                if (var3 != null) {
                    try {
                        is.close();
                    } catch (Throwable var12) {
                        var3.addSuppressed(var12);
                    }
                } else {
                    is.close();
                }
            }

        }

        return var4;
    }

    public static JSONArray parseArray(File f, Charset charSet) throws IOException {
        return (JSONArray)parse(f, charSet);
    }

    public static JSONObject parseObject(File f, Charset charSet) throws IOException {
        return (JSONObject)parse(f, charSet);
    }

    public static JSONValue parse(File f, String csName) throws IOException {
        InputStream is = new FileInputStream(f);
        Throwable var3 = null;

        JSONValue var4;
        try {
            var4 = parse((InputStream)is, (String)csName);
        } catch (Throwable var13) {
            var3 = var13;
            throw var13;
        } finally {
            if (is != null) {
                if (var3 != null) {
                    try {
                        is.close();
                    } catch (Throwable var12) {
                        var3.addSuppressed(var12);
                    }
                } else {
                    is.close();
                }
            }

        }

        return var4;
    }

    public static JSONArray parseArray(File f, String csName) throws IOException {
        return (JSONArray)parse(f, csName);
    }

    public static JSONObject parseObject(File f, String csName) throws IOException {
        return (JSONObject)parse(f, csName);
    }

    public static JSONValue parse(InputStream is) throws IOException {
        Reader rdr = new InputStreamReader(is);
        Throwable var2 = null;

        JSONValue var3;
        try {
            var3 = parse((Reader)rdr);
        } catch (Throwable var12) {
            var2 = var12;
            throw var12;
        } finally {
            if (rdr != null) {
                if (var2 != null) {
                    try {
                        rdr.close();
                    } catch (Throwable var11) {
                        var2.addSuppressed(var11);
                    }
                } else {
                    rdr.close();
                }
            }

        }

        return var3;
    }

    public static JSONArray parseArray(InputStream is) throws IOException {
        return (JSONArray)parse(is);
    }

    public static JSONObject parseObject(InputStream is) throws IOException {
        return (JSONObject)parse(is);
    }

    public static JSONValue parse(InputStream is, Charset charSet) throws IOException {
        Reader rdr = new InputStreamReader(is, charSet);
        Throwable var3 = null;

        JSONValue var4;
        try {
            var4 = parse((Reader)rdr);
        } catch (Throwable var13) {
            var3 = var13;
            throw var13;
        } finally {
            if (rdr != null) {
                if (var3 != null) {
                    try {
                        rdr.close();
                    } catch (Throwable var12) {
                        var3.addSuppressed(var12);
                    }
                } else {
                    rdr.close();
                }
            }

        }

        return var4;
    }

    public static JSONArray parseArray(InputStream is, Charset charSet) throws IOException {
        return (JSONArray)parse(is, charSet);
    }

    public static JSONObject parseObject(InputStream is, Charset charSet) throws IOException {
        return (JSONObject)parse(is, charSet);
    }

    public static JSONValue parse(InputStream is, String csName) throws IOException {
        Reader rdr = new InputStreamReader(is, csName);
        Throwable var3 = null;

        JSONValue var4;
        try {
            var4 = parse((Reader)rdr);
        } catch (Throwable var13) {
            var3 = var13;
            throw var13;
        } finally {
            if (rdr != null) {
                if (var3 != null) {
                    try {
                        rdr.close();
                    } catch (Throwable var12) {
                        var3.addSuppressed(var12);
                    }
                } else {
                    rdr.close();
                }
            }

        }

        return var4;
    }

    public static JSONArray parseArray(InputStream is, String csName) throws IOException {
        return (JSONArray)parse(is, csName);
    }

    public static JSONObject parseObject(InputStream is, String csName) throws IOException {
        return (JSONObject)parse(is, csName);
    }

    public static JSONValue parse(Reader rdr) throws IOException {
        return parse((CharSequence)(new ReaderBuffer(rdr)));
    }

    public static JSONArray parseArray(Reader rdr) throws IOException {
        return (JSONArray)parse(rdr);
    }

    public static JSONObject parseObject(Reader rdr) throws IOException {
        return (JSONObject)parse(rdr);
    }

    public static JSONValue parse(CharSequence cs) {
        ParseText p = new ParseText(cs);
        JSONValue result = parse(p);
        if (!p.skipSpaces().isExhausted()) {
            throw new JSONException("Excess characters after JSON value");
        } else {
            return result;
        }
    }

    public static JSONArray parseArray(CharSequence cs) {
        return (JSONArray)parse(cs);
    }

    public static JSONObject parseObject(CharSequence cs) {
        return (JSONObject)parse(cs);
    }

    public static JSONValue parse(ParseText p) {
        p.skipSpaces();
        if (p.match('{')) {
            JSONObject object = new JSONObject();
            if (!p.skipSpaces().match('}')) {
                while(true) {
                    if (!p.match('"')) {
                        throw new JSONException("Illegal key in JSON object");
                    }

                    String key = decodeString(p);
                    if (object.containsKey(key)) {
                        throw new JSONException("Duplicate key in JSON object");
                    }

                    if (!p.skipSpaces().match(':')) {
                        throw new JSONException("Missing colon in JSON object");
                    }

                    object.put(key, parse(p));
                    if (!p.skipSpaces().match(',')) {
                        if (!p.match('}')) {
                            throw new JSONException("Missing closing brace in JSON object");
                        }
                        break;
                    }

                    p.skipSpaces();
                }
            }

            return object;
        } else if (!p.match('[')) {
            if (p.match('"')) {
                return new JSONString(decodeString(p));
            } else {
                int numberStart = p.getIndex();
                if (p.match('-')) {
                }

                if (p.matchDec()) {
                    boolean zero = false;
                    if (p.getResultChar() == '0') {
                        if (p.getResultLength() > 1) {
                            throw new JSONException("Illegal JSON number");
                        }

                        zero = true;
                    }

                    boolean floating = false;
                    if (p.match('.')) {
                        floating = true;
                        if (!p.matchDec()) {
                            throw new JSONException("Illegal JSON number");
                        }
                    }

                    if (p.matchIgnoreCase('e')) {
                        floating = true;
                        if (!p.match('+') && p.match('-')) {
                        }

                        if (!p.matchDec()) {
                            throw new JSONException("Illegal JSON number");
                        }
                    }

                    if (floating) {
                        return JSONDouble.valueOf(p.getString(numberStart, p.getIndex()));
                    } else if (zero) {
                        return JSONZero.ZERO;
                    } else {
                        long longValue = Long.parseLong(p.getString(numberStart, p.getIndex()));
                        int intValue = (int)longValue;
                        return (JSONValue)((long)intValue == longValue ? JSONInteger.valueOf(intValue) : JSONLong.valueOf(longValue));
                    }
                } else if (p.getIndex() > numberStart) {
                    throw new JSONException("Illegal JSON number");
                } else if (p.matchName("true")) {
                    return JSONBoolean.TRUE;
                } else if (p.matchName("false")) {
                    return JSONBoolean.FALSE;
                } else if (p.matchName("null")) {
                    return null;
                } else {
                    throw new JSONException("Illegal JSON syntax");
                }
            }
        } else {
            JSONArray array = new JSONArray();
            if (!p.skipSpaces().match(']')) {
                do {
                    array.add(parse(p));
                } while(p.skipSpaces().match(','));

                if (!p.match(']')) {
                    throw new JSONException("Missing closing bracket in JSON array");
                }
            }

            return array;
        }
    }

    public static JSONArray parseArray(ParseText p) {
        return (JSONArray)parse(p);
    }

    public static JSONObject parseObject(ParseText p) {
        return (JSONObject)parse(p);
    }

    private static String decodeString(ParseText p) {
        int start = p.getIndex();

        while(!p.isExhausted()) {
            char ch = p.getChar();
            if (ch == '"') {
                return p.getString(start, p.getStart());
            }

            if (ch == '\\') {
                StringBuilder sb = new StringBuilder(p.getString(start, p.getStart()));

                label74:
                while(!p.isExhausted()) {
                    char ch1 = p.getChar();
                    if (ch1 == '"') {
                        sb.append('"');
                    } else if (ch1 == '\\') {
                        sb.append('\\');
                    } else if (ch1 == '/') {
                        sb.append('/');
                    } else if (ch1 == 'b') {
                        sb.append('\b');
                    } else if (ch1 == 'f') {
                        sb.append('\f');
                    } else if (ch1 == 'n') {
                        sb.append('\n');
                    } else if (ch1 == 'r') {
                        sb.append('\r');
                    } else if (ch1 == 't') {
                        sb.append('\t');
                    } else {
                        if (ch1 != 'u') {
                            throw new JSONException("Illegal escape sequence in JSON string");
                        }

                        if (!p.matchHexFixed(4)) {
                            throw new JSONException("Illegal Unicode sequence in JSON string");
                        }

                        sb.append((char)p.getResultHexInt());
                    }

                    while(!p.isExhausted()) {
                        ch1 = p.getChar();
                        if (ch1 == '"') {
                            return sb.toString();
                        }

                        if (ch1 == '\\') {
                            continue label74;
                        }

                        if (ch1 < ' ') {
                            throw new JSONException("Illegal character in JSON string");
                        }

                        sb.append(ch1);
                    }

                    throw new JSONException("Unterminated JSON string");
                }

                throw new JSONException("Unterminated JSON string");
            }

            if (ch < ' ') {
                throw new JSONException("Illegal character in JSON string");
            }
        }

        throw new JSONException("Unterminated JSON string");
    }

    public static String toJSON(JSONValue value) {
        return value == null ? "null" : value.toJSON();
    }

    public static void appendJSON(Appendable a, JSONValue value) throws IOException {
        if (value == null) {
            a.append("null");
        } else {
            value.appendJSON(a);
        }

    }

    public static String getString(JSONValue value) {
        if (value == null) {
            return null;
        } else if (!(value instanceof JSONString)) {
            throw new JSONException("Not a JSON string");
        } else {
            return ((JSONString)value).toString();
        }
    }

    public static int getInt(JSONValue value) {
        if (value == null) {
            return 0;
        } else if (!(value instanceof Number)) {
            throw new JSONException("Not a JSON number");
        } else {
            return ((Number)value).intValue();
        }
    }

    public static long getLong(JSONValue value) {
        if (value == null) {
            return 0L;
        } else if (!(value instanceof Number)) {
            throw new JSONException("Not a JSON number");
        } else {
            return ((Number)value).longValue();
        }
    }

    public static float getFloat(JSONValue value) {
        if (value == null) {
            return 0.0F;
        } else if (!(value instanceof Number)) {
            throw new JSONException("Not a JSON number");
        } else {
            return ((Number)value).floatValue();
        }
    }

    public static double getDouble(JSONValue value) {
        if (value == null) {
            return 0.0D;
        } else if (!(value instanceof Number)) {
            throw new JSONException("Not a JSON number");
        } else {
            return ((Number)value).doubleValue();
        }
    }

    public static boolean getBoolean(JSONValue value) {
        if (value == null) {
            return false;
        } else if (!(value instanceof JSONBoolean)) {
            throw new JSONException("Not a JSON boolean");
        } else {
            return ((JSONBoolean)value).booleanValue();
        }
    }

    public static JSONArray getArray(JSONValue value) {
        if (value == null) {
            return null;
        } else if (!(value instanceof JSONArray)) {
            throw new JSONException("Not a JSON array");
        } else {
            return (JSONArray)value;
        }
    }

    public static JSONObject getObject(JSONValue value) {
        if (value == null) {
            return null;
        } else if (!(value instanceof JSONObject)) {
            throw new JSONException("Not a JSON object");
        } else {
            return (JSONObject)value;
        }
    }
}