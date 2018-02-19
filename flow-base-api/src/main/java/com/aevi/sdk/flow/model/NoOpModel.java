package com.aevi.sdk.flow.model;


import com.aevi.util.json.Jsonable;

public class NoOpModel implements Jsonable {

    @Override
    public String toJson() {
        return null;
    }
}
