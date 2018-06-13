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
