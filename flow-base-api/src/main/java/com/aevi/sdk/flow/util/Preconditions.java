package com.aevi.sdk.flow.util;

public final class Preconditions {
    private Preconditions() {
    }

    public static void checkState(boolean check, String message, Object... messageParams) {
        if (messageParams.length > 0) {
            message = String.format(message, messageParams);
        }

        if (!check) {
            throw new IllegalStateException(message);
        }
    }

    public static void checkArgument(boolean check, String message, Object... messageParams) {
        if (messageParams.length > 0) {
            message = String.format(message, messageParams);
        }

        if (!check) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void checkNotNull(Object object, String message) {
        if (object == null) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void checkArgument(boolean b, String message) {
        if (!b) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void checkNotEmpty(Object[] array, String message) {
        if (array == null || array.length == 0) {
            throw new IllegalArgumentException(message);
        }
    }
}
