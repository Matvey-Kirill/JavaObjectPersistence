package main.utils.print;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;

public class ReaderBuffer implements CharSequence {
    public static final int defaultBufferSize = 4096;
    private int bufferSize;
    private char[][] buffers;
    private int length;
    private String str;

    public ReaderBuffer(Reader rdr, int bufferSize) throws IOException {
        if (bufferSize <= 0) {
            throw new IllegalArgumentException("Buffer size must be positive");
        } else {
            this.bufferSize = bufferSize;
            this.str = null;
            this.length = 0;
            ArrayList bufferList = new ArrayList();

            int len;
            do {
                char[] buffer = new char[bufferSize];
                len = bufferSize;
                int offset = 0;

                do {
                    int n = rdr.read(buffer, offset, len);
                    if (n < 0) {
                        break;
                    }

                    offset += n;
                    len -= n;
                } while(len > 0);

                if (offset == 0) {
                    break;
                }

                bufferList.add(buffer);
                this.length += bufferSize - len;
            } while(len <= 0);

            this.buffers = (char[][])bufferList.toArray(new char[bufferList.size()][]);
        }
    }

    public ReaderBuffer(Reader rdr) throws IOException {
        this(rdr, 4096);
    }

    public int length() {
        return this.length;
    }

    public char charAt(int index) {
        if (index >= 0 && index < this.length) {
            return this.buffers[index / this.bufferSize][index % this.bufferSize];
        } else {
            throw new IndexOutOfBoundsException(String.valueOf(index));
        }
    }

    public CharSequence subSequence(int start, int end) {
        return new SubSequence(this, start, end);
    }

    public String toString() {
        if (this.str == null) {
            StringBuilder sb = new StringBuilder(this.length);
            int lastbuffer = this.buffers.length - 1;

            for(int i = 0; i < lastbuffer; ++i) {
                sb.append(this.buffers[i]);
            }

            if (lastbuffer >= 0) {
                sb.append(this.buffers[lastbuffer], 0, this.length - lastbuffer * this.bufferSize);
            }

            this.str = sb.toString();
        }

        return this.str;
    }

    public static String toString(Reader rdr) throws IOException {
        return (new ReaderBuffer(rdr)).toString();
    }

    public static String toString(Reader rdr, int bufferSize) throws IOException {
        return (new ReaderBuffer(rdr, bufferSize)).toString();
    }
}
