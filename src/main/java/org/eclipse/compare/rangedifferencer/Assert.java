package org.eclipse.compare.rangedifferencer;

public class Assert {

    public static boolean isTrue(boolean expression, String message) {
        if (!expression) {
            throw new AssertionFailedException("assertion failed: " + message);
        } else {
            return expression;
        }
    }

    public static boolean isTrue(boolean expression) {
        return isTrue(expression, "");
    }

}
