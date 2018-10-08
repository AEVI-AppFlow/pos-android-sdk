/*
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.aevi.sdk.flow.util;

import java.util.Collection;

/**
 * Internal preconditions.
 */
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

    public static void checkNotEmpty(String str, String message) {
        if (str == null || str.isEmpty()) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void checkNotEmpty(Object[] array, String message) {
        if (array == null || array.length == 0) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void checkNotEmpty(Collection<?> collection, String message) {
        if (collection == null || collection.isEmpty()) {
            throw new IllegalArgumentException(message);
        }
    }
}
