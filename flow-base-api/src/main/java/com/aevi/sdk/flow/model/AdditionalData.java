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

package com.aevi.sdk.flow.model;


import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import com.aevi.util.json.JsonConverter;
import com.aevi.util.json.JsonOption;
import com.aevi.util.json.Jsonable;

import java.lang.reflect.Array;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * A container of generic/bespoke data that can be of any type, identified by a string key.
 *
 * Note that keys are case sensitive, meaning it is essential the right case is used for insertions and lookups.
 */
@SuppressWarnings({"unchecked", "ConstantConditions", "WeakerAccess"})
public class AdditionalData implements Jsonable {

    private final Map<String, JsonOption> data;

    /**
     * Create a new instance with an empty collection of data.
     */
    public AdditionalData() {
        data = new ConcurrentHashMap<>();
    }

    /**
     * Create a new instance based on the collection of data passed as parameter.
     *
     * @param data The data to use as a base
     */
    public AdditionalData(Map<String, JsonOption> data) {
        this.data = new ConcurrentHashMap<>(data);
    }

    /**
     * Create a new instance based on the data provided.
     *
     * @param copyFrom The data to copy from
     */
    public AdditionalData(AdditionalData copyFrom) {
        this.data = new ConcurrentHashMap<>(copyFrom.data);
    }

    /**
     * Check if this data collection is empty or not.
     *
     * @return True if there are no data, false otherwise.
     */
    public boolean isEmpty() {
        return data.isEmpty();
    }

    /**
     * Add any arbitrary non-primitive object as value with an associated string based key.
     *
     * This will overwrite any previous values with the same key. Clients need to call {@link #hasData(String)} to check whether a key already
     * exists to avoid potential overwriting of existing values.
     *
     * @param key    The string key for this value
     * @param values The value/values to store
     * @param <T>    The type of the value
     */
    public <T> void addData(String key, T... values) {
        if (values != null) {
            if (values.length == 1) {
                if (values[0] != null) {
                    data.put(key, new JsonOption(values[0]));
                }
            } else {
                data.put(key, new JsonOption(values));
            }
        }
    }

    /**
     * Copy over values from the provided AdditionalData model.
     *
     * Depending on the allowOverwrite parameter, existing values may or may not get overwritten.
     *
     * @param additionalData The data to copy from.
     * @param allowOverwrite Whether or not to allow overwriting existing values
     */
    public void addData(AdditionalData additionalData, boolean allowOverwrite) {
        for (String key : additionalData.getKeys()) {
            if (allowOverwrite || !hasData(key)) {
                addData(key, additionalData.getValue(key));
            }
        }
    }

    /**
     * Remove data with associated key from the collection.
     *
     * @param key The data key
     */
    public void removeData(String key) {
        data.remove(key);
    }

    /**
     * Will retrieve and afterwards remove the data.
     *
     * See {@link #getValue(String, Object...)}
     *
     * @param key The data key
     * @return The data that was removed
     */
    @Nullable
    public Object getAndRemoveData(String key) {
        Object object = getValue(key);
        removeData(key);
        return object;
    }

    /**
     * Will retrieve and afterwards remove the data.
     *
     * See {@link #getValue(String, Class, Object[])}
     *
     * @param key         The data key
     * @param desiredType The desired data type
     * @param <T>         Type
     * @return The data that was removed
     */
    @Nullable
    public <T> T getAndRemoveData(String key, Class<T> desiredType) {
        T t = getValue(key, desiredType);
        removeData(key);
        return t;
    }

    /**
     * Clear all data from the collection.
     */
    public void clearData() {
        data.clear();
    }

    /**
     * Check whether there is any data with the associated key in the collection.
     *
     * @param key The data key
     * @return True if exists, false otherwise.
     */
    public boolean hasData(String key) {
        return data.containsKey(key);
    }

    /**
     * Get a set of all the keys for this data collection.
     *
     * @return The set of data keys
     */
    @NonNull
    public Set<String> getKeys() {
        return data.keySet();
    }

    /**
     * Retrieve the value for the provided key as a raw object for casting.
     *
     * @param key          The data key
     * @param defaultValue Optional var-args where a default value can be passed in which will be returned if key does not exist
     * @return The value as an Object (for casting) or default value if set, or null
     */
    @Nullable
    public Object getValue(String key, Object... defaultValue) {
        JsonOption option = data.get(key);
        if (option != null) {
            return option.getValue();
        }
        if (defaultValue.length > 0) {
            return defaultValue[0];
        }
        return null;
    }

    /**
     * Get all data of a particular type returned as a Map.
     *
     * @param desiredType The type type to filter by
     * @param <T>         Type
     * @return A map of key/value pairs of the type specified
     */
    @NonNull
    public <T> Map<String, T> getDataOfType(Class<T> desiredType) {
        Map<String, T> map = new HashMap<>();
        for (String key : data.keySet()) {
            T value = getValueByType(desiredType, data.get(key).getValue());
            if (value != null) {
                map.put(key, value);
            }
        }
        return map;
    }

    /**
     * Retrieve the value for the provided key, casted to the type provided where possible.
     * <p>
     * The method will attempt the following, in order; <br><br>
     * 1. If the stored value isn't an array and the desiredType is, it will be wrapped in a desiredType array<br>
     * If the value types don't match the array will be empty but a fixed size of 1
     * Example - stored value is java.lang.Integer and desiredType is Integer[].class, returns single item array.
     * <pre> {@code Integer[] value = getValue("key", Integer[].class) } </pre>
     * 2. If the stored value type is an array and matches the desiredType parameter, it will be cast to that and returned.<br>
     * Example - stored value is Integer[] and desiredType is Integer[].
     * <pre> {@code Integer[] value = getValue("key", Integer[].class) } </pre>
     * 3. If the desiredType parameter is a String[], the stored value(s) (array or not) will be returned as their string representation (toString()) as elements in a string array.<br>
     * Example - stored value is 10 (Integer) and desiredType is String[].class, new String[]{"10"}.
     * <pre> {@code String[] value = getValue("key", String[].class) } </pre>
     * 4. If none of the above conditions are met then the desiredType is likely primitive, it will attempt to be cast to that and returned<br>
     * Example - stored value is java.lang.String and desiredType is String.class.
     * <pre> {@code String value = getValue("key", String.class) } </pre>
     *
     * @param key          The data key
     * @param desiredType  The expected or desired type for the return value
     * @param defaultValue Optional var-args for setting a default value to pass back if key does not exist
     * @param <T>          The type of the value
     * @return The value in desired type if possible, or default value if provided or null
     */
    @Nullable
    public <T> T getValue(String key, Class<T> desiredType, T... defaultValue) {
        JsonOption option = data.get(key);
        T returnValue = null;

        if (option != null) {
            // If desired type is an array but value isn't, cast, box and return
            if (desiredType.isArray() && !option.getValue().getClass().isArray()) {
                returnValue = getValueAsArray(desiredType, option);
            }
            // Else if exact array match, cast and return
            else if (desiredType.isArray() && option.getValue().getClass() == desiredType) {
                returnValue = getValueByType(desiredType, option.getValue());
            }
            // Else, if desired type is String[], we convert whatever we have to an array of strings relying on toString()
            else if (desiredType == String[].class) {
                returnValue = getValueAsStringArray(option);
            }
            // Else attempt to cast and return
            else {
                returnValue = getValueByType(desiredType, option.getValue());
            }
            // Fallback - log as a warning
            if (returnValue == null) {
                Log.w(AdditionalData.class.getSimpleName(), "Failed to convert " + option.getValue() + " to " + desiredType.getName());
            }
        }

        if (returnValue != null) {
            return returnValue;
        }

        if (defaultValue.length > 0) {
            return defaultValue[0];
        }

        return null;
    }

    /**
     * Convenience method to retrieve a string based value.
     *
     * @param key          The data key
     * @param defaultValue Optional var-args for setting a default value to pass back if key does not exist
     * @return The value associated with the key if it exists. If not, uses default value if provided, or null.
     */
    public String getStringValue(String key, String... defaultValue) {
        return getValue(key, String.class, defaultValue);
    }

    /**
     * Convenience method to retrieve an integer based value.
     *
     * @param key          The data key
     * @param defaultValue Optional var-args for setting a default value to pass back if key does not exist
     * @return The value associated with the key if it exists. If not, uses default value if provided, or null.
     */
    public int getIntegerValue(String key, Integer... defaultValue) {
        return getValue(key, Integer.class, defaultValue);
    }

    /**
     * Convenience method to retrieve a boolean based value.
     *
     * @param key          The data key
     * @param defaultValue Optional var-args for setting a default value to pass back if key does not exist
     * @return The value associated with the key if it exists. If not, uses default value if provided, or null.
     */
    public boolean getBooleanValue(String key, Boolean... defaultValue) {
        return getValue(key, Boolean.class, defaultValue);
    }

    private <T> T getValueByType(Class<T> desiredType, Object value) {
        try {
            return desiredType.cast(value);
        } catch (ClassCastException e) {
            //... fall thru
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    private <T> T getValueAsArray(Class<T> desiredType, JsonOption option) {
        T t = (T) Array.newInstance(desiredType.getComponentType(), 1);
        try {
            T value = (T) option.getValue();
            Array.set(t, 0, value);
        } catch (IllegalArgumentException e) {
            if (desiredType == String[].class) {
                Array.set(t, 0, option.getValue().toString());
            }
        }
        return t;
    }

    @SuppressWarnings("unchecked")
    private <T> T getValueAsStringArray(JsonOption option) {
        if (option.getValue().getClass().isArray()) {
            int arrayLength = Array.getLength(option.getValue());
            String[] values = new String[arrayLength];
            for (int i = 0; i < arrayLength; i++) {
                values[i] = Array.get(option.getValue(), i).toString();
            }
            return (T) values;
        } else {
            return (T) new String[]{option.getValue().toString()};
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        AdditionalData additionalData1 = (AdditionalData) o;

        return data != null ? data.equals(additionalData1.data) : additionalData1.data == null;

    }

    @Override
    public int hashCode() {
        return data != null ? data.hashCode() : 0;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("{");
        for (String key : data.keySet()) {
            if (stringBuilder.length() > 1) {
                stringBuilder.append(", ");
            }
            stringBuilder.append(key);
            stringBuilder.append("=\"");
            stringBuilder.append(data.get(key).getValue().toString());
            stringBuilder.append("\"");
        }
        stringBuilder.append("}");
        return stringBuilder.toString();
    }

    @Override
    public String toJson() {
        return JsonConverter.serialize(this);
    }

    public static AdditionalData fromJson(String json) {
        return JsonConverter.deserialize(json, AdditionalData.class);
    }
}
