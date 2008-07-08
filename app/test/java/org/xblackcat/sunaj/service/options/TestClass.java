package org.xblackcat.sunaj.service.options;

/**
 * Date: 8 лип 2008
 *
 * @author xBlackCat
 */

public class TestClass {
    private final int a = 1;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TestClass testClass = (TestClass) o;

        return a == testClass.a;

    }

    @Override
    public int hashCode() {
        return a;
    }
}
