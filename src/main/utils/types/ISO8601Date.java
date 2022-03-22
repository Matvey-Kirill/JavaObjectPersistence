package main.utils.types;

import main.utils.print.ParseText;
import main.utils.print.Strings;

import java.io.IOException;
import java.text.DecimalFormatSymbols;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class ISO8601Date extends Date {
    private static final long serialVersionUID = 7662874565511305042L;
    private static final int[] parseDays = new int[]{-1, 2, 3, 4, 5, 6, 7, 1, -1, -1};
    private static final char dateSeparator = '-';
    private static final char weekNumberSeparator = 'W';
    private static final char dateTimeSeparator = 'T';
    private static final char zeroTimeZoneIndicator = 'Z';
    private static final char timeSeparator = ':';
    private static final char commaDecimalSeparator = ',';
    private static final char dotDecimalSeparator = '.';
    private static final char defaultDecimalSeparator = (new DecimalFormatSymbols()).getDecimalSeparator();
    private static final char[] decimalSeparators = new char[]{',', '.'};
    private static final char plusSign = '+';
    private static final char minusSign = '-';
    private static final char[] plusOrMinus = new char[]{'+', '-'};
    public static final int YEAR_MASK = 2;
    public static final int MONTH_MASK = 4;
    public static final int WEEK_OF_YEAR_MASK = 8;
    public static final int DAY_OF_MONTH_MASK = 32;
    public static final int DAY_OF_YEAR_MASK = 64;
    public static final int DAY_OF_WEEK_MASK = 128;
    public static final int HOUR_OF_DAY_MASK = 2048;
    public static final int MINUTE_MASK = 4096;
    public static final int SECOND_MASK = 8192;
    public static final int MILLISECOND_MASK = 16384;
    public static final int ZONE_OFFSET_MASK = 32768;
    private TimeZone timeZone;

    public ISO8601Date() {
        this.timeZone = null;
    }

    public ISO8601Date(long date) {
        super(date);
        this.timeZone = null;
    }

    public ISO8601Date(String date) {
        this(decode(date));
    }

    public ISO8601Date(String date, TimeZone timeZone) {
        super(decode(date).getTimeInMillis());
        this.setTimeZone(timeZone);
    }

    public ISO8601Date(Date date, TimeZone timeZone) {
        super(date.getTime());
        this.setTimeZone(timeZone);
    }

    public ISO8601Date(Calendar cal) {
        super(cal.getTimeInMillis());
        this.setTimeZone(cal.getTimeZone());
    }

    public ISO8601Date(ISO8601Date date) {
        this((Date)date, date.getTimeZone());
    }

    public TimeZone getTimeZone() {
        return this.timeZone;
    }

    public void setTimeZone(TimeZone timeZone) {
        this.timeZone = timeZone;
    }

    public void setTime(long time) {
        super.setTime(time);
        this.timeZone = null;
    }

    public String toString() {
        return toString(this, this.timeZone);
    }

    public static String toString(Date date, TimeZone timeZone) {
        StringBuilder sb = new StringBuilder(10);

        try {
            Calendar cal = Calendar.getInstance(timeZone != null ? timeZone : TimeZone.getDefault());
            cal.setTime(date);
            sb.append(cal.get(1));
            sb.append('-');
            Strings.append2Digits(sb, cal.get(2) + 1);
            sb.append('-');
            Strings.append2Digits(sb, cal.get(5));
        } catch (IOException var4) {
        }

        return sb.toString();
    }

    public static String toString(Date date) {
        return toString(date, (TimeZone)null);
    }

    public static Calendar getCalendar() {
        Calendar cal = Calendar.getInstance();
        cal.setLenient(false);
        cal.setMinimalDaysInFirstWeek(4);
        cal.setFirstDayOfWeek(2);
        return cal;
    }

    public static Calendar decode(CharSequence str) {
        if (str == null) {
            throw new IllegalArgumentException("ISO8601 string must not be null");
        } else {
            Calendar cal = getCalendar();
            cal.clear();
            boolean complete = false;
            ParseText text = new ParseText(str);
            if (text.matchDecFixed(4)) {
                cal.set(1, text.getResultInt());
                if (text.isExhausted()) {
                    return cal;
                }

                StringBuilder sb;
                char sign;
                int hours;
                int mins;
                if (text.match('-')) {
                    if (text.match('W')) {
                        if (!text.matchDecFixed(2)) {
                            throw new IllegalArgumentException("Illegal ISO8601 date string");
                        }

                        cal.set(3, text.getResultInt());
                        if (text.match('-')) {
                            if (!text.matchDecFixed(1)) {
                                throw new IllegalArgumentException("Illegal ISO8601 date string");
                            }

                            cal.set(7, parseDays[text.getResultInt()]);
                            complete = true;
                        }
                    } else if (text.matchDecFixed(3)) {
                        cal.set(6, text.getResultInt());
                        complete = true;
                    } else {
                        if (!text.matchDecFixed(2)) {
                            throw new IllegalArgumentException("Illegal ISO8601 date string");
                        }

                        cal.set(2, text.getResultInt() - 1);
                        if (text.match('-')) {
                            if (!text.matchDecFixed(2)) {
                                throw new IllegalArgumentException("Illegal ISO8601 date string");
                            }

                            cal.set(5, text.getResultInt());
                            complete = true;
                        }
                    }

                    if (complete && text.match('T')) {
                        if (!text.matchDecFixed(2)) {
                            throw new IllegalArgumentException("Illegal ISO8601 date string");
                        }

                        cal.set(11, text.getResultInt());
                        if (text.match(':')) {
                            if (!text.matchDecFixed(2)) {
                                throw new IllegalArgumentException("Illegal ISO8601 date string");
                            }

                            cal.set(12, text.getResultInt());
                            if (text.match(':')) {
                                if (!text.matchDecFixed(2)) {
                                    throw new IllegalArgumentException("Illegal ISO8601 date string");
                                }

                                cal.set(13, text.getResultInt());
                                if (text.matchAnyOf(decimalSeparators)) {
                                    fractionSeconds(cal, text);
                                }
                            } else if (text.matchAnyOf(decimalSeparators)) {
                                fractionMinutes(cal, text);
                            }
                        } else if (text.matchAnyOf(decimalSeparators)) {
                            fractionHours(cal, text);
                        }
                    }

                    if (text.match('Z')) {
                        cal.setTimeZone(TimeZone.getTimeZone("GMT"));
                        cal.set(15, 0);
                    } else if (text.matchAnyOf(plusOrMinus)) {
                        sb = new StringBuilder("GMT");
                        sign = text.getResultChar();
                        sb.append(sign);
                        if (!text.matchDecFixed(2)) {
                            throw new IllegalArgumentException("Illegal ISO8601 date string");
                        }

                        hours = text.getResultInt();
                        sb.append(hours);
                        sb.append(':');
                        mins = 0;
                        if (text.match(':')) {
                            if (!text.matchDecFixed(2)) {
                                throw new IllegalArgumentException("Illegal ISO8601 date string");
                            }

                            mins = text.getResultInt();
                        }

                        sb.append(mins);
                        cal.setTimeZone(TimeZone.getTimeZone(sb.toString()));
                        mins += hours * 60;
                        if (sign == '-') {
                            mins = -mins;
                        }

                        cal.set(15, mins * 60 * 1000);
                    }
                } else {
                    if (text.match('W')) {
                        if (!text.matchDecFixed(2)) {
                            throw new IllegalArgumentException("Illegal ISO8601 date string");
                        }

                        cal.set(3, text.getResultInt());
                        if (text.matchDecFixed(1)) {
                            cal.set(7, parseDays[text.getResultInt()]);
                            complete = true;
                        }
                    } else if (text.matchDecFixed(4)) {
                        int i = text.getResultInt();
                        cal.set(2, i / 100 - 1);
                        cal.set(5, i % 100);
                        complete = true;
                    } else if (text.matchDecFixed(3)) {
                        cal.set(6, text.getResultInt());
                        complete = true;
                    } else if (text.matchDecFixed(2)) {
                        cal.set(2, text.getResultInt() - 1);
                    }

                    if (complete && text.match('T')) {
                        if (!text.matchDecFixed(2)) {
                            throw new IllegalArgumentException("Illegal ISO8601 date string");
                        }

                        cal.set(11, text.getResultInt());
                        if (text.matchDecFixed(2)) {
                            cal.set(12, text.getResultInt());
                            if (text.matchDecFixed(2)) {
                                cal.set(13, text.getResultInt());
                                if (text.matchAnyOf(decimalSeparators)) {
                                    fractionSeconds(cal, text);
                                }
                            } else if (text.matchAnyOf(decimalSeparators)) {
                                fractionMinutes(cal, text);
                            }
                        } else if (text.matchAnyOf(decimalSeparators)) {
                            fractionHours(cal, text);
                        }
                    }

                    if (text.match('Z')) {
                        cal.setTimeZone(TimeZone.getTimeZone("GMT"));
                        cal.set(15, 0);
                    } else if (text.matchAnyOf(plusOrMinus)) {
                        sb = new StringBuilder("GMT");
                        sign = text.getResultChar();
                        sb.append(sign);
                        if (!text.matchDecFixed(2)) {
                            throw new IllegalArgumentException("Illegal ISO8601 date string");
                        }

                        hours = text.getResultInt();
                        sb.append(hours);
                        sb.append(':');
                        mins = 0;
                        if (text.matchDecFixed(2)) {
                            mins = text.getResultInt();
                        }

                        sb.append(mins);
                        cal.setTimeZone(TimeZone.getTimeZone(sb.toString()));
                        mins += hours * 60;
                        if (sign == '-') {
                            mins = -mins;
                        }

                        cal.set(15, mins * 60 * 1000);
                    }
                }

                if (text.isExhausted()) {
                    return cal;
                }
            }

            throw new IllegalArgumentException("Illegal ISO8601 date string");
        }
    }

    private static void fractionHours(Calendar cal, ParseText text) {
        checkFraction(text);
        long millis = adjustFraction(text.getResultLong() * 3600L, text.getResultLength());
        cal.set(12, (int)(millis / 60000L));
        millis %= 60000L;
        if (millis != 0L) {
            cal.set(13, (int)(millis / 1000L));
            millis %= 1000L;
            if (millis != 0L) {
                cal.set(14, (int)millis);
            }
        }

    }

    private static void fractionMinutes(Calendar cal, ParseText text) {
        checkFraction(text);
        long millis = adjustFraction(text.getResultLong() * 60L, text.getResultLength());
        cal.set(13, (int)(millis / 1000L));
        millis %= 1000L;
        if (millis != 0L) {
            cal.set(14, (int)millis);
        }

    }

    private static void fractionSeconds(Calendar cal, ParseText text) {
        checkFraction(text);
        cal.set(14, (int)adjustFraction(text.getResultLong(), text.getResultLength()));
    }

    private static void checkFraction(ParseText text) {
        if (!text.matchDec(9)) {
            throw new IllegalArgumentException("Illegal fraction in ISO8601 date string");
        }
    }

    private static long adjustFraction(long value, int len) {
        if (len <= 3) {
            while(len < 3) {
                value *= 10L;
                ++len;
            }
        } else {
            while(true) {
                if (len <= 4) {
                    value = (value + 5L) / 10L;
                    break;
                }

                value /= 10L;
                --len;
            }
        }

        return value;
    }

    public static String toString(Calendar cal) {
        return toString(cal, true);
    }

    public static String toString(Calendar cal, boolean extended) {
        int fields = 0;
        if (cal.isSet(1)) {
            fields |= 2;
        }

        if (cal.isSet(2)) {
            fields |= 4;
        }

        if (cal.isSet(5)) {
            fields |= 32;
        }

        if (cal.isSet(3)) {
            fields |= 8;
        }

        if (cal.isSet(7)) {
            fields |= 128;
        }

        if (cal.isSet(6)) {
            fields |= 64;
        }

        if (cal.isSet(11)) {
            fields |= 2048;
        }

        if (cal.isSet(12)) {
            fields |= 4096;
        }

        if (cal.isSet(13)) {
            fields |= 8192;
        }

        if (cal.isSet(14)) {
            fields |= 16384;
        }

        if (cal.isSet(15)) {
            fields |= 32768;
        }

        return toString(cal, extended, fields);
    }

    public static String toString(Calendar cal, boolean extended, int fields) {
        int anyTimeMask = 30720;
        StringBuilder sb = new StringBuilder();

        try {
            int d;
            if (!fieldSet(fields, 2)) {
                appendTime(sb, cal, extended, fields);
            } else {
                sb.append(cal.get(1));
                if (fieldSet(fields, 4)) {
                    if (extended) {
                        sb.append('-');
                    }

                    Strings.append2Digits(sb, cal.get(2) + 1);
                    if (fieldSet(fields, 32)) {
                        if (extended) {
                            sb.append('-');
                        }

                        Strings.append2Digits(sb, cal.get(5));
                        if (fieldSet(fields, anyTimeMask)) {
                            sb.append('T');
                            appendTime(sb, cal, extended, fields);
                        }
                    }
                } else if (!fieldSet(fields, 8)) {
                    if (fieldSet(fields, 64)) {
                        if (extended) {
                            sb.append('-');
                        }

                        Strings.append3Digits(sb, cal.get(6));
                        if (fieldSet(fields, anyTimeMask)) {
                            sb.append('T');
                            appendTime(sb, cal, extended, fields);
                        }
                    }
                } else {
                    if (extended) {
                        sb.append('-');
                    }

                    sb.append('W');
                    Strings.append2Digits(sb, cal.get(3));
                    if (fieldSet(fields, 128)) {
                        d = cal.get(7);

                        for(int i = 1; i < 8; ++i) {
                            if (parseDays[i] == d) {
                                if (extended) {
                                    sb.append('-');
                                }

                                sb.append(i);
                                break;
                            }
                        }

                        if (fieldSet(fields, anyTimeMask)) {
                            sb.append('T');
                            appendTime(sb, cal, extended, fields);
                        }
                    }
                }
            }

            if (fieldSet(fields, 32768)) {
                d = cal.get(15);
                if (cal.getTimeZone().inDaylightTime(cal.getTime())) {
                    d += cal.get(16);
                }

                d /= 60000;
                if (d == 0) {
                    sb.append('Z');
                } else {
                    sb.append((char)(d < 0 ? '-' : '+'));
                    d = Math.abs(d);
                    Strings.append2Digits(sb, d / 60);
                    d %= 60;
                    if (extended) {
                        sb.append(':');
                    }

                    Strings.append2Digits(sb, d);
                }
            }
        } catch (IOException var7) {
        }

        return sb.toString();
    }

    private static boolean fieldSet(int fields, int mask) {
        return (fields & mask) != 0;
    }

    private static void appendTime(StringBuilder sb, Calendar cal, boolean extended, int fields) throws IOException {
        Strings.append2Digits(sb, cal.get(11));
        if ((fields & 28672) != 0) {
            if (extended) {
                sb.append(':');
            }

            Strings.append2Digits(sb, cal.get(12));
            if ((fields & 24576) != 0) {
                if (extended) {
                    sb.append(':');
                }

                Strings.append2Digits(sb, cal.get(13));
                if ((fields & 16384) != 0) {
                    sb.append(defaultDecimalSeparator);
                    Strings.append3Digits(sb, cal.get(14));
                }
            }
        }

    }
}
