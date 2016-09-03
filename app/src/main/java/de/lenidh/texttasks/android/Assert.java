package de.lenidh.texttasks.android;

public final class Assert {

    // Don't allow instantiation.
    private Assert() {}

    /**
     * Fail with the given message if the specified condition is not true;
     *
     * @param condition the condition
     * @param message the message
     */
    public static void isTrue(boolean condition, String message) {
        if(condition)
            throw new AssertionError(message);
    }

    /**
     * Fail with the given message.
     *
     * @param message the message
     */
    public static void fail(String message) {
        isTrue(false, message);
    }
}
