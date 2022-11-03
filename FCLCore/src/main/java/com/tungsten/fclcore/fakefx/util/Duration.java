package com.tungsten.fclcore.fakefx.util;

import com.tungsten.fclcore.fakefx.beans.NamedArg;

import java.io.Serializable;

/**
 * <p>
 *     A class that defines a duration of time. Duration instances are immutable,
 *     and are therefore replaced rather than modified, similar to {@link java.math.BigDecimal}.
 *     Durations can be created using the constructor, or one of the static construction
 *     methods such as {@link #seconds} or {@link #minutes}.
 * </p>
 * @since JavaFX 2.0
 */
public class Duration implements Comparable<Duration>, Serializable {
    /**
     * A Duration of 0 (no time).
     */
    public static final Duration ZERO = new Duration(0);

    /**
     * A Duration of 1 millisecond.
     */
    public static final Duration ONE = new Duration(1);

    /**
     * An Infinite Duration.
     */
    public static final Duration INDEFINITE = new Duration(Double.POSITIVE_INFINITY);

    /**
     * A Duration of some unknown amount of time.
     */
    public static final Duration UNKNOWN = new Duration(Double.NaN);

    /**
     * Factory method that returns a Duration instance for a specified
     * amount of time. The syntax is "[number][ms|s|m|h]".
     *
     * @param time A non-null string properly formatted. Leading or trailing
     * spaces will not parse correctly. Throws a NullPointerException if
     * time is null.
     * @return a Duration which is represented by the <code>time</code>
     */
    public static Duration valueOf(String time) {
        int index = -1;
        for (int i=0; i<time.length(); i++) {
            char c = time.charAt(i);
            if (!Character.isDigit(c) && c != '.' && c != '-') {
                index = i;
                break;
            }
        }

        if (index == -1) {
            // Never found the suffix!
            throw new IllegalArgumentException("The time parameter must have a suffix of [ms|s|m|h]");
        }

        double value = Double.parseDouble(time.substring(0, index));
        String suffix = time.substring(index);
        if ("ms".equals(suffix)) {
            return millis(value);
        } else if ("s".equals(suffix)) {
            return seconds(value);
        } else if ("m".equals(suffix)) {
            return minutes(value);
        } else if ("h".equals(suffix)) {
            return hours(value);
        } else {
            // Malformed suffix
            throw new IllegalArgumentException("The time parameter must have a suffix of [ms|s|m|h]");
        }
    }

    /**
     * Factory method that returns a Duration instance for a specified
     * number of milliseconds.
     *
     * @param ms the number of milliseconds
     * @return a Duration instance of the specified number of milliseconds
     */
    public static Duration millis(double ms) {
        if (ms == 0) {
            return ZERO;
        } else if (ms == 1) {
            return ONE;
        } else if (ms == Double.POSITIVE_INFINITY) {
            return INDEFINITE;
        } else if (Double.isNaN(ms)) {
            return UNKNOWN;
        } else {
            return new Duration(ms);
        }
    }

    /**
     * Factory method that returns a Duration instance representing the specified
     * number of seconds.
     *
     * @param s the number of seconds
     * @return a Duration instance of the specified number of seconds
     */
    public static Duration seconds(double s) {
        if (s == 0) {
            return ZERO;
        } else if (s == Double.POSITIVE_INFINITY) {
            return INDEFINITE;
        } else if (Double.isNaN(s)) {
            return UNKNOWN;
        } else {
            return new Duration(s * 1000.0);
        }
    }

    /**
     * Factory method that returns a Duration instance representing the specified
     * number of minutes.
     *
     * @param m the number of minutes
     * @return a Duration instance of the specified number of minutes
     */
    public static Duration minutes(double m) {
        if (m == 0) {
            return ZERO;
        } else if (m == Double.POSITIVE_INFINITY) {
            return INDEFINITE;
        } else if (Double.isNaN(m)) {
            return UNKNOWN;
        } else {
            return new Duration(m * (1000.0 * 60.0));
        }
    }

    /**
     * Factory method that returns a Duration instance representing the specified
     * number of hours.
     *
     * @param h the number of hours
     * @return a Duration instance representing the specified number of hours
     */
    public static Duration hours(double h) {
        if (h == 0) {
            return ZERO;
        } else if (h == Double.POSITIVE_INFINITY) {
            return INDEFINITE;
        } else if (Double.isNaN(h)) {
            return UNKNOWN;
        } else {
            return new Duration(h * (1000.0 * 60.0 * 60.0));
        }
    }

    /**
     * The value of this duration, in fractional milliseconds
     */
    private final double millis;

    /**
     * Creates a new Duration with potentially fractional millisecond resolution.
     * @param millis The number of milliseconds
     */
    public Duration(@NamedArg("millis") double millis) {
        this.millis = millis;
    }

    /**
     * Returns the number of milliseconds in this period or Double.POSITIVE_INFINITY
     * if the period is INDEFINITE or NaN if the period is UNKNOWN.
     * @return the Duration in fractional milliseconds
     */
    public double toMillis() {
        return millis;
    }

    /**
     * Returns the number of seconds in this period or Double.POSITIVE_INFINITY
     * if the period is INDEFINITE or NaN if the period is UNKNOWN.
     * @return the Duration in fractional seconds
     */
    public double toSeconds() {
        return millis / 1000.0;
    }

    /**
     * Returns the number of minutes in this period or Double.POSITIVE_INFINITY
     * if the period is INDEFINITE or NaN if the period is UNKNOWN.
     * @return the Duration in fractional minutes
     */
    public double toMinutes() {
        return millis / (60 * 1000.0);
    }

    /**
     * Returns the number of hours in this period or Double.POSITIVE_INFINITY
     * if the period is INDEFINITE or NaN if the period is UNKNOWN.
     * @return the Duration in fractional hours
     */
    public double toHours() {
        return millis / (60 * 60 * 1000.0);
    }

    /**
     * Add this instance and another Duration instance to return a new Duration instance.
     * If either instance is INDEFINITE, return INDEFINITE.
     * If either instance is UNKNOWN, return UNKNOWN.
     * This method does not change the value of the called Duration instance.
     *
     * @param other must not be null
     * @return the result of adding this duration to the other duration. This is
     *         the same as millis + other.millis using double arithmetic
     */
    public Duration add(Duration other) {
        // Note that several of these functions assume that the value of millis in INDEFINITE
        // is Double.POSITIVE_INFINITY.
        return millis(millis + other.millis);
    }

    /**
     * Subtract other Duration instance from this instance to return a new Duration instance.
     * If either instance is UNKNOWN, return UNKNOWN.
     * Otherwise, if either instance is INDEFINITE, return INDEFINITE.
     * This method does not change the value of the called Duration instance.
     *
     * @param other must not be null
     * @return the result of subtracting the other duration from this duration. This is
     *         the same as millis - other.millis using double arithmetic
     */
    public Duration subtract(Duration other) {
        return millis(millis - other.millis);
    }

    /**
     * Multiply this instance with a number to return a new Duration instance.
     * If either instance is INDEFINITE, return INDEFINITE.
     * If either Duration instance is UNKNOWN, return UNKNOWN.
     * This method does not change the value of the called Duration instance.
     *
     * @deprecated This method produces surprising results by not taking units into
     *             account. Use {@link #multiply(double)} instead.
     * @param other must not be null
     * @return the result of multiplying this duration with the other duration. This is
     *         the same as millis * other.millis using double arithmetic
     */
    @Deprecated
    public Duration multiply(Duration other) {
        return millis(millis * other.millis);
    }

    /**
     * Multiply this instance with a number representing millis and return a new Duration.
     * If the called Duration instance is INDEFINITE, return INDEFINITE.
     * If the called Duration instance is UNKNOWN, return UNKNOWN.
     * This method does not change the value of the called Duration instance.
     *
     * @param n the amount to multiply by in fractional milliseconds
     * @return the result of multiplying this duration with n. This is
     *         the same as millis * n using double arithmetic
     */
    public Duration multiply(double n) {
        return millis(millis * n);
    }

    /**
     * Divide this instance by a number to return a new Duration instance.
     * If the called Duration instance is INDEFINITE, return INDEFINITE.
     * If the called Duration instance is UNKNOWN, return UNKNOWN.
     * This method does not change the value of the called Duration instance.
     *
     * @param n the amount to divide by in fractional milliseconds
     * @return the result of dividing this duration with n. This is
     *         the same as millis / n using double arithmetic
     */
    public Duration divide(double n) {
        return millis(millis / n);
    }

    /**
     * Divide this instance by another Duration to return the ratio.
     * If both instances are INDEFINITE, return NaN.
     * If this instance is INDEFINITE, return POSITIVE_INFINITY
     * If the other instance is INDEFINITE, return 0.0.
     * This function does not change the value of the called Duration instance.
     *
     * @deprecated This method produces surprising results by not taking units into
     *             account. Use {@link #divide(double)} instead.
     * @param other must not be null
     * @return the result of dividing this duration by the other duration. This is
     *         the same as millis / other.millis using double arithmetic
     */
    @Deprecated
    public Duration divide(Duration other) {
        return millis(millis / other.millis);
    }

    /**
     * Return a new Duration instance which has a negative number of milliseconds
     * from this instance.  For example, <code>Duration.millis(50).negate()</code> returns
     * a Duration of -50 milliseconds.
     * If the called Duration instance is INDEFINITE, return INDEFINITE.
     * This function does not change the value of the called Duration instance.
     *
     * @return the result of negating this duration. This is
     *         the same as -millis using double arithmetic
     */
    public Duration negate() {
        return millis(-millis);
    }

    /**
     * Gets whether this Duration instance is Indefinite. A Duration is Indefinite
     * if it equals Duration.INDEFINITE.
     * @return true if this Duration is equivalent to Duration.INDEFINITE or Double.POSITIVE_INFINITY.
     */
    public boolean isIndefinite() {
        return millis == Double.POSITIVE_INFINITY;
    }

    /**
     * Gets whether this Duration instance is Unknown. A Duration is Unknown
     * if it equals Duration.UNKNOWN.
     * @return true if this Duration is equivalent to Duration.UNKNOWN or Double.isNaN(millis)
     */
    public boolean isUnknown() {
        return Double.isNaN(millis);
    }

    /**
     * Returns true if the specified duration is less than (&lt;) this instance.
     * INDEFINITE is treated as if it were positive infinity.
     *
     * @param other cannot be null
     * @return true if millis &lt; other.millis using double arithmetic
     */
    public boolean lessThan(Duration other) {
        return millis < other.millis;
    }

    /**
     * Returns true if the specified duration is less than or equal to (&lt;=) this instance.
     * INDEFINITE is treated as if it were positive infinity.
     *
     * @param other cannot be null
     * @return true if millis &lt;= other.millis using double arithmetic
     */
    public boolean lessThanOrEqualTo(Duration other) {
        return millis <= other.millis;
    }

    /**
     * Returns true if the specified duration is greater than (&gt;) this instance.
     * INDEFINITE is treated as if it were positive infinity.
     *
     * @param other cannot be null
     * @return true if millis &gt; other.millis using double arithmetic
     */
    public boolean greaterThan(Duration other) {
        return millis > other.millis;
    }

    /**
     * Returns true if the specified duration is greater than or equal to (&gt;=) this instance.
     * INDEFINITE is treated as if it were positive infinity.
     *
     * @param other cannot be null
     * @return true if millis &gt;= other.millis using double arithmetic
     */
    public boolean greaterThanOrEqualTo(Duration other) {
        return millis >= other.millis;
    }

    /**
     * Returns a string representation of this {@code Duration} object.
     * @return a string representation of this {@code Duration} object.
     */
    @Override public String toString() {
        return isIndefinite() ? "INDEFINITE" : (isUnknown() ? "UNKNOWN" : millis + " ms");
    }

    /**
     * Compares durations represented by this object and the specified object.
     * Returns a negative integer, zero, or a positive integer as this duration
     * is less than, equal to, or greater than the specified duration.
     * @param d the duration to be compared.
     * @return a negative integer, zero, or a positive integer as this duration
     * is less than, equal to, or greater than the specified duration.
     */
    @Override public int compareTo(Duration d) {
        // Reuse the Double.compare implementation
        return Double.compare(millis, d.millis);
    }

    /**
     * Indicates whether some other object is "equal to" this one.
     * @param obj the reference object with which to compare.
     * @return {@code true} if this object is equal to the {@code obj} argument; {@code false} otherwise.
     */
    @Override public boolean equals(Object obj) {
        // Rely on Java's handling of double == double
        return obj == this || obj instanceof Duration && millis == ((Duration) obj).millis;
    }

    /**
     * Returns a hash code for this {@code Duration} object.
     * @return a hash code for this {@code Duration} object.
     */
    @Override public int hashCode() {
        // Uses the same implementation as Double.hashCode
        long bits = Double.doubleToLongBits(millis);
        return (int)(bits ^ (bits >>> 32));
    }
}
