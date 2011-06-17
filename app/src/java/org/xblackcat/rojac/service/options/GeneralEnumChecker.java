package org.xblackcat.rojac.service.options;

import org.xblackcat.rojac.i18n.IDescribable;

import javax.swing.*;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

/**
 * @author xBlackCat
 */
class GeneralEnumChecker<T extends Enum<T>> implements IValueChecker<T> {
    private final Set<T> allowedValues;

    public GeneralEnumChecker(Class<T> enumClass) {
        allowedValues = EnumSet.allOf(enumClass);
    }

    public GeneralEnumChecker(T value, T... rest) {
        allowedValues = EnumSet.of(value, rest);
    }

    @Override
    public List<T> getPossibleValues() {
        return new ArrayList<T>(allowedValues);
    }

    @Override
    public String getValueDescription(T v) throws IllegalArgumentException {
        if (v == null) {
            return "(null)";
        } else if (v instanceof IDescribable) {
            return ((IDescribable) v).getLabel().get();
        }

        return v.name();
    }

    @Override
    public boolean isValueCorrect(T v) {
        return true;
    }

    @Override
    public Icon getValueIcon(T v) throws IllegalArgumentException {
        return null;
    }
}
