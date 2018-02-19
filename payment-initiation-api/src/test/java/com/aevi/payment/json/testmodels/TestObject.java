package com.aevi.util.json.testmodels;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestObject {

    private String stringy;
    private int numbery;
    private long nummmmmbery;
    private float floaty;
    private boolean truthy;
    private List<TestOtherObject> otherObjectList;
    private Map<String, TestOtherObject> otherObjectMap;
    private TestOtherObject testOtherObject;

    public TestObject(String stringy, int numbery, long nummmmmbery, float floaty, boolean truthy, List<TestOtherObject> otherObjectList, Map<String, TestOtherObject> otherObjectMap, TestOtherObject testOtherObject) {
        this.stringy = stringy;
        this.numbery = numbery;
        this.nummmmmbery = nummmmmbery;
        this.floaty = floaty;
        this.truthy = truthy;
        this.otherObjectList = otherObjectList;
        this.otherObjectMap = otherObjectMap;
        this.testOtherObject = testOtherObject;
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

    public void setStringy(String stringy) {
        this.stringy = stringy;
    }

    public void setNumbery(int numbery) {
        this.numbery = numbery;
    }

    public void setNummmmmbery(long nummmmmbery) {
        this.nummmmmbery = nummmmmbery;
    }

    public void setFloaty(float floaty) {
        this.floaty = floaty;
    }

    public void setTruthy(boolean truthy) {
        this.truthy = truthy;
    }

    public void setOtherObjectList(List<TestOtherObject> otherObjectList) {
        this.otherObjectList = otherObjectList;
    }

    public void setOtherObjectMap(Map<String, TestOtherObject> otherObjectMap) {
        this.otherObjectMap = otherObjectMap;
    }

    public void setTestOtherObject(TestOtherObject testOtherObject) {
        this.testOtherObject = testOtherObject;
    }

    public static TestObject getDefaultObject() {
        List<TestOtherObject> list = new ArrayList<>();
        Map<String, TestOtherObject> map = new HashMap<>();
        TestOtherObject otherObject = new TestOtherObject("helloooo");

        return new TestObject("string", 1, 3, 24.5f, true, list, map, otherObject);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        TestObject object = (TestObject) o;

        if (numbery != object.numbery) {
            return false;
        }
        if (nummmmmbery != object.nummmmmbery) {
            return false;
        }
        if (Float.compare(object.floaty, floaty) != 0) {
            return false;
        }
        if (truthy != object.truthy) {
            return false;
        }
        if (stringy != null ? !stringy.equals(object.stringy) : object.stringy != null) {
            return false;
        }
        if (otherObjectList != null ? !otherObjectList.equals(object.otherObjectList) : object.otherObjectList != null) {
            return false;
        }
        if (otherObjectMap != null ? !otherObjectMap.equals(object.otherObjectMap) : object.otherObjectMap != null) {
            return false;
        }
        return testOtherObject != null ? testOtherObject.equals(object.testOtherObject) : object.testOtherObject == null;
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
        return result;
    }
}
