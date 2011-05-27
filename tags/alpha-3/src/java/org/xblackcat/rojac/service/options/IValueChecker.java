package org.xblackcat.rojac.service.options;

import java.util.List;

/**
 * Interface for all possible values accessor of property.
 *
 * @author xBlackCat
 */

public interface IValueChecker<T> {
    /**
     * Returns all the possible values for a property.
     *
     * @return an array of possible values for a property.
     */
    List<T> getPossibleValues();

    /**
     * Returns a string representation of the value.
     *
     * @param v value to convert.
     *
     * @return string representation of a value.
     *
     * @throws IllegalArgumentException if value is invalid.
     */
    String getValueDescription(T v) throws IllegalArgumentException;

    /**
     * Checks if the value is valid one.
     *
     * @param v value to check.
     *
     * @return <code>true</code> if the value is valid and <code>false</code> elsewise.
     */
    boolean isValueCorrect(T v);
}
