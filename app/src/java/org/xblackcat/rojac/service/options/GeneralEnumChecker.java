package org.xblackcat.rojac.service.options;

import org.xblackcat.rojac.i18n.IDescribable;

/**
 * @author xBlackCat
 */
class GeneralEnumChecker<T extends Enum<T>> implements IValueChecker<T> {
    private final Class<T> enumClass;

    public GeneralEnumChecker(Class<T> enumClass) {
        this.enumClass = enumClass;
    }

    @Override
    public T[] getPossibleValues() {
        return enumClass.getEnumConstants();
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
}
