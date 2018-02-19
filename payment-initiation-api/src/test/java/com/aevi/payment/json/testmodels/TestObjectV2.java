package com.aevi.util.json.testmodels;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestObjectV2 {

    private String stringy;
    private int numbery;
    private long nummmmmbery;
    private float floaty;
    private boolean truthy;
    private List<TestOtherObject> otherObjectList;
    private Map<String, TestOtherObject> otherObjectMap;
    private TestOtherObject testOtherObject;

    private String superAmazoNewValue;

    public TestObjectV2(String stringy, int numbery, long nummmmmbery, float floaty, boolean truthy, List<TestOtherObject> otherObjectList,
                        Map<String, TestOtherObject> otherObjectMap, TestOtherObject testOtherObject, String superAmazoNewValue) {
        this.stringy = stringy;
        this.numbery = numbery;
        this.nummmmmbery = nummmmmbery;
        this.floaty = floaty;
        this.truthy = truthy;
        this.otherObjectList = otherObjectList;
        this.otherObjectMap = otherObjectMap;
        this.testOtherObject = testOtherObject;
        this.superAmazoNewValue = superAmazoNewValue;
    }

    public String getStringy() {
        return stringy;
    }

    public int getNumbery() {
        return numbery;
    }

    public long getNummmmmbery() {
        return nummmmmbery;
    }

    public float getFloaty() {
        return floaty;
    }

    public boolean isTruthy() {
        return truthy;
    }

    public List<TestOtherObject> getOtherObjectList() {
        return otherObjectList;
    }

    public Map<String, TestOtherObject> getOtherObjectMap() {
        return otherObjectMap;
    }

    public TestOtherObject getTestOtherObject() {
        return testOtherObject;
    }

    public String getSuperAmazoNewValue() {
        return superAmazoNewValue;
    }

    public static TestObjectV2 getDefaultObject() {
        List<TestOtherObject> list = new ArrayList<>();
        Map<String, TestOtherObject> map = new HashMap<>();
        TestOtherObject otherObject = new TestOtherObject("helloooo");

        return new TestObjectV2("string", 1, 3, 24.5f, true, list, map, otherObject, "newValue");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        TestObjectV2 that = (TestObjectV2) o;

        if (numbery != that.numbery) {
            return false;
        }
        if (nummmmmbery != that.nummmmmbery) {
            return false;
        }
        if (Float.compare(that.floaty, floaty) != 0) {
            return false;
        }
        if (truthy != that.truthy) {
            return false;
        }
        if (stringy != null ? !stringy.equals(that.stringy) : that.stringy != null) {
            return false;
        }
        if (otherObjectList != null ? !otherObjectList.equals(that.otherObjectList) : that.otherObjectList != null) {
            return false;
        }
        if (otherObjectMap != null ? !otherObjectMap.equals(that.otherObjectMap) : that.otherObjectMap != null) {
            return false;
        }
        if (testOtherObject != null ? !testOtherObject.equals(that.testOtherObject) : that.testOtherObject != null) {
            return false;
        }
        return superAmazoNewValue != null ? superAmazoNewValue.equals(that.superAmazoNewValue) : that.superAmazoNewValue == null;
    }

    @Override
    public int hashCode() {
        int result = stringy != null ? stringy.hashCode() : 0;
        result = 31 * result + numbery;
        result = 31 * result + (int) (nummmmmbery ^ (nummmmmbery >>> 32));
        result = 31 * result + (floaty != +0.0f ? Float.floatToIntBits(floaty) : 0);
        result = 31 * result + (truthy ? 1 : 0);
        result = 31 * result + (otherObjectList != null ? otherObjectList.hashCode() : 0);
        result = 31 * result + (otherObjectMap != null ? otherObjectMap.hashCode() : 0);
        result = 31 * result + (testOtherObject != null ? testOtherObject.hashCode() : 0);
        result = 31 * result + (superAmazoNewValue != null ? superAmazoNewValue.hashCode() : 0);
        return result;
    }
}
