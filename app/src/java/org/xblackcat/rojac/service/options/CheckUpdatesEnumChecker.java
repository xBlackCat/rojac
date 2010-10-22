package org.xblackcat.rojac.service.options;

/**
* @author xBlackCat
*/
class CheckUpdatesEnumChecker implements IValueChecker<CheckUpdatesEnum> {
    @Override
    public CheckUpdatesEnum[] getPossibleValues() {
        return CheckUpdatesEnum.values();
    }

    @Override
    public String getValueDescription(CheckUpdatesEnum v) throws IllegalArgumentException {
        return v == null ? "(null)" : v.description();
    }

    @Override
    public boolean isValueCorrect(CheckUpdatesEnum v) {
        return true;
    }
}
