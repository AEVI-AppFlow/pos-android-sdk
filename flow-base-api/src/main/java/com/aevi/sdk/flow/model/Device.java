package com.aevi.sdk.flow.model;


import com.aevi.util.json.JsonConverter;

/**
 * Represents a device connected to the merchant device.
 */
public class Device extends BaseModel {

    private final String name;

    public Device(String id, String name) {
        super(id);
        this.name = name;
    }

    /**
     * Get the name of the device.
     *
     * @return The name of the device.
     */
    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "Device{" +
                "name='" + name + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        Device device = (Device) o;

        return name != null ? name.equals(device.name) : device.name == null;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }

    @Override
    public String toJson() {
        return JsonConverter.serialize(this);
    }

    public static Device fromJson(String json) {
        return JsonConverter.deserialize(json, Device.class);
    }
}
