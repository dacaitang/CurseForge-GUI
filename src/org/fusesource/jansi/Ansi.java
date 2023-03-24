package org.fusesource.jansi;

public class Ansi {
    private StringBuffer mBuffer = new StringBuffer();

    public static Ansi ansi() {
        return new Ansi();
    }

    public Ansi a(Object o) {
        mBuffer.append(o);
        return this;
    }

    public Ansi fg(Color color) {
        return this;
    }

    public Ansi reset() {
        return this;
    }

    public String toString() {
        return mBuffer.toString();
    }

    public static enum Color {
        CYAN,
        GREEN,
        MAGENTA,
        RED,
        YELLOW
    }
}
