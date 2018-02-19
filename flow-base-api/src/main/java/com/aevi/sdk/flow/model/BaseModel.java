package com.aevi.sdk.flow.model;


import com.aevi.util.json.Sendable;

public abstract class BaseModel implements Sendable {

    private final String id;

    protected BaseModel(String id) {
        this.id = id;
    }

    /**
     * Get the id of this model.
     *
     * @return The id.
     */
    @Override
    public String getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BaseModel baseModel = (BaseModel) o;

        return id != null ? id.equals(baseModel.id) : baseModel.id == null;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
