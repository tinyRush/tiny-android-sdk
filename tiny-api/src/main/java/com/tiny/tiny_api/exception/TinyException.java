package com.tiny.tiny_api.exception;

/**
 * File Name : TinyException.java
 * Author : ManhNV
 * Date : 2017-09-06
 */

public class TinyException extends RuntimeException {
    static final long serialVersionUID = 1;

    /**
     * Constructs a new ApiException.
     */
    public TinyException() {
        super();
    }

    /**
     * Constructs a new ApiException.
     *
     * @param message the detail message of this exception
     */
    public TinyException(String message) {
        super(message);
    }

    /**
     * Constructs a new ApiException.
     *
     * @param format the format string (see {@link java.util.Formatter#format})
     * @param args   the list of arguments passed to the formatter.
     */
    public TinyException(String format, Object... args) {
        this(String.format(format, args));
    }

    /**
     * Constructs a new ApiException.
     *
     * @param message   the detail message of this exception
     * @param throwable the cause of this exception
     */
    public TinyException(String message, Throwable throwable) {
        super(message, throwable);
    }

    /**
     * Constructs a new ApiException.
     *
     * @param throwable the cause of this exception
     */
    public TinyException(Throwable throwable) {
        super(throwable);
    }

    @Override
    public String toString() {
        // Throwable.toString() returns "ApiException:{message}". Returning just "{message}"
        // should be fine here.
        return getMessage();
    }
}
