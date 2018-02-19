package com.aevi.util.json.testmodels;

public class TestOtherObject {
    private String otherObjectString;

    public TestOtherObject() {

    }

    public TestOtherObject(String string) {
        this.otherObjectString = string;
    }

    public String getOtherObjectString() {
        return otherObjectString;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        TestOtherObject that = (TestOtherObject) o;

        return otherObjectString != null ? otherObjectString.equals(that.otherObjectString) : that.otherObjectString == null;
    }

    @Override
    public int hashCode() {
        return otherObjectString != null ? otherObjectString.hashCode() : 0;
    }
}
