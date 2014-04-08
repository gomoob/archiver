package com.gomoob.archiver.configuration;

public class ParseException extends Exception {

    /**
     * The default serial version UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Constructs a new exception with the specified detail message and cause.
     * <p>
     * Note that the detail message associated with <code>cause</code> is <i>not</i> automatically incorporated in this
     * exception's detail message.
     * 
     * @param message the detail message (which is saved for later retrieval by the {@link #getMessage()} method).
     * @param cause the cause (which is saved for later retrieval by the {@link #getCause()} method). (A <tt>null</tt>
     *            value is permitted, and indicates that the cause is nonexistent or unknown.)
     * @since 1.4
     */
    public ParseException(String message, Throwable cause) {

        super(message, cause);

    }

}
