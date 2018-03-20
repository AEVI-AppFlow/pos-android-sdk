package com.aevi.sdk.flow.util;


import java.util.Collection;

public final class ComparisonUtil {

    private ComparisonUtil() {
    }

    public static boolean stringCollectionContainsIgnoreCase(Collection<String> collection, String value) {
        if (value == null) {
            return false;
        }
        for (String entry : collection) {
            if (entry.equalsIgnoreCase(value)) {
                return true;
            }
        }
        return false;
    }

    public static boolean stringArrayContainsIgnoreCase(String[] array, String value) {
        if (value == null) {
            return false;
        }
        for (String arrayEntry : array) {
            if (arrayEntry.equalsIgnoreCase(value)) {
                return true;
            }
        }
        return false;
    }
}
