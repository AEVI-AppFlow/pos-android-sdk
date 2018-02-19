package com.aevi.util.json.testmodels;

import com.aevi.util.json.JsonOption;

import java.util.HashMap;
import java.util.Map;

public class JsonOptionsTest {

    private final Map<String, JsonOption> options;

    public JsonOptionsTest() {
        options = new HashMap<>();
    }

    public <T> void addOption(String key, T... values) {
        if (values != null) {
            if (values.length == 1) {
                if (values[0] != null) {
                    options.put(key, new JsonOption(values[0]));
                }
            } else {
                options.put(key, new JsonOption(values));
            }
        }
    }

    public Object getOption(String key) {
        JsonOption option = options.get(key);
        if (option != null) {
            try {
                return Class.forName(option.getType()).cast(option.getValue());
            } catch (ClassNotFoundException e) {
                //... fall thru
            }
        }
        return null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        JsonOptionsTest options1 = (JsonOptionsTest) o;

        return options != null ? options.equals(options1.options) : options1.options == null;

    }

    @Override
    public int hashCode() {
        return options != null ? options.hashCode() : 0;
    }

    @Override
    public String toString() {
        return options.toString();
    }
}
