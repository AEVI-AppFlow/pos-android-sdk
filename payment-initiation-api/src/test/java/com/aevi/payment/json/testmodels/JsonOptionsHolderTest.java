package com.aevi.util.json.testmodels;

import com.aevi.util.json.JsonConverter;
import com.aevi.util.json.Jsonable;

public class JsonOptionsHolderTest implements Jsonable {

    private JsonOptionsTest options = new JsonOptionsTest();

    public JsonOptionsTest getOptions() {
        return options;
    }

    @Override
    public String toJson() {
        return JsonConverter.serialize(this);
    }
}
