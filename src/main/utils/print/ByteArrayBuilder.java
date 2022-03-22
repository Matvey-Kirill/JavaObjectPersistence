package main.utils.print;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

public class ByteArrayBuilder {
    public static final int MINIMUM_READ_BUFFER_SIZE = 4096;
    private byte[] buf;
    private int count;
    private int increment;

    public ByteArrayBuilder(int size, int increment) {
        if (size < 0) {
            throw new IllegalArgumentException("Size must be >= 0");
        } else if (increment <= 0) {
            throw new IllegalArgumentException("Increment must be > 0");
        } else {
            this.buf = new byte[size];
            this.count = 0;
            this.increment = increment;
        }
    }

    public ByteArrayBuilder(int size) {
        this(size, Math.max(size / 2, 1));
    }

    public ByteArrayBuilder() {
        this(20);
    }

    public ByteArrayBuilder(byte[] array) {
        this(array != null ? array.length : 20);
        this.append(array);
    }

    public ByteArrayBuilder(ByteArrayBuilder bab) {
        this(bab != null ? bab.length() : 20);
        this.append(bab);
    }

    public int length() {
        return this.count;
    }

    public byte[] toByteArray() {
        return Arrays.copyOf(this.buf, this.count);
    }

    public byte[] toByteArray(int start) {
        if (start >= 0 && start <= this.count) {
            return this.copyOf(start, this.count);
        } else {
            throw new IndexOutOfBoundsException("start=" + start + "; count=" + this.count);
        }
    }

    public byte[] toByteArray(int start, int end) {
        if (start >= 0 && start <= end && end <= this.count) {
            return this.copyOf(start, end);
        } else {
            throw new IndexOutOfBoundsException("start=" + start + "; end=" + end + "; count=" + this.count);
        }
    }

    private byte[] copyOf(int start, int end) {
        int n = end - start;
        byte[] result = new byte[n];
        System.arraycopy(this.buf, start, result, 0, n);
        return result;
    }

    public byte set(int index, int value) {
        if (index >= 0 && index < this.count) {
            byte result = this.buf[index];
            this.buf[index] = (byte)value;
            return result;
        } else {
            throw new IndexOutOfBoundsException("index=" + index + "; count=" + this.count);
        }
    }

    public byte get(int index) {
        if (index >= 0 && index < this.count) {
            return this.buf[index];
        } else {
            throw new IndexOutOfBoundsException("index=" + index + "; count=" + this.count);
        }
    }

    private void ensureCapacity(int newCapacity) {
        int oldCapacity = this.buf.length;
        if (newCapacity > oldCapacity) {
            int proposed = oldCapacity + this.increment;
            if (proposed < newCapacity) {
                proposed = newCapacity;
            }

            this.buf = Arrays.copyOf(this.buf, proposed);
        }

    }

    public ByteArrayBuilder append(int b) {
        this.ensureCapacity(this.count + 1);
        this.buf[this.count++] = (byte)b;
        return this;
    }

    public ByteArrayBuilder append(byte[] array) {
        if (array != null) {
            int n = array.length;
            this.ensureCapacity(this.count + n);
            System.arraycopy(array, 0, this.buf, this.count, n);
            this.count += n;
        }

        return this;
    }

    public ByteArrayBuilder append(byte[] array, int start, int end) {
        if (array != null) {
            if (start < 0 || start > end || end > array.length) {
                throw new IndexOutOfBoundsException("start=" + start + "; end=" + end + "; length=" + array.length);
            }

            int n = end - start;
            this.ensureCapacity(this.count + n);
            System.arraycopy(array, start, this.buf, this.count, n);
            this.count += n;
        }

        return this;
    }

    public ByteArrayBuilder append(ByteArrayBuilder bab) {
        if (bab != null) {
            int n = bab.length();
            this.ensureCapacity(this.count + n);
            System.arraycopy(bab.buf, 0, this.buf, this.count, n);
            this.count += n;
        }

        return this;
    }

    public ByteArrayBuilder append(InputStream is) throws IOException {
        if (is != null) {
            int readSize = Math.max(this.buf.length, 4096);
            byte[] readBuf = new byte[readSize];

            while(true) {
                int i = is.read(readBuf);
                if (i < 0) {
                    break;
                }

                this.append(readBuf, 0, i);
            }
        }

        return this;
    }

    public ByteArrayBuilder insert(int index, int b) {
        if (index >= 0 && index <= this.count) {
            this.ensureCapacity(this.count + 1);
            if (index < this.count) {
                System.arraycopy(this.buf, index, this.buf, index + 1, this.count - index);
            }

            this.buf[index] = (byte)b;
            ++this.count;
            return this;
        } else {
            throw new IndexOutOfBoundsException("index=" + index + "; count=" + this.count);
        }
    }

    public ByteArrayBuilder insert(int index, byte[] array) {
        if (index >= 0 && index <= this.count) {
            if (array != null) {
                int n = array.length;
                this.ensureCapacity(this.count + n);
                if (index < this.count) {
                    System.arraycopy(this.buf, index, this.buf, index + n, this.count - index);
                }

                System.arraycopy(array, 0, this.buf, index, n);
                this.count += n;
            }

            return this;
        } else {
            throw new IndexOutOfBoundsException("index=" + index + "; count=" + this.count);
        }
    }

    public ByteArrayBuilder insert(int index, ByteArrayBuilder bab) {
        if (index >= 0 && index <= this.count) {
            if (bab != null) {
                int n = bab.count;
                this.ensureCapacity(this.count + n);
                if (index < this.count) {
                    System.arraycopy(this.buf, index, this.buf, index + n, this.count - index);
                }

                System.arraycopy(bab.buf, 0, this.buf, index, n);
                this.count += n;
            }

            return this;
        } else {
            throw new IndexOutOfBoundsException("index=" + index + "; count=" + this.count);
        }
    }

    public ByteArrayBuilder create() {
        return new ByteArrayBuilder();
    }

    public ByteArrayBuilder create(int size) {
        return new ByteArrayBuilder(size);
    }
}
