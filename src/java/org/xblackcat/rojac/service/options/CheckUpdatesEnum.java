package org.xblackcat.rojac.service.options;

import org.xblackcat.rojac.i18n.IDescribable;
import org.xblackcat.rojac.i18n.Messages;

import java.util.Calendar;

/**
 * @author xBlackCat
 */

public enum CheckUpdatesEnum implements IDescribable {
    None(Messages.Description_UpdatePeriod_None, 0, 0),
    EveryRun(Messages.Description_UpdatePeriod_EveryRun, 0, 0),
    EveryDay(Messages.Description_UpdatePeriod_EveryDay, Calendar.DAY_OF_MONTH, 1),
    EveryWeek(Messages.Description_UpdatePeriod_EveryWeek, Calendar.DAY_OF_MONTH, 7),
    EveryMonth(Messages.Description_UpdatePeriod_EveryMonth, Calendar.MONTH, 1);

    private final Messages description;
    private final int field;
    private final int amount;

    CheckUpdatesEnum(Messages description, int field, int amount) {
        this.description = description;
        this.field = field;
        this.amount = amount;
    }

    public boolean shouldCheck(Long lastCheckTime) {
        if (this == None) {
            return false;
        } else if (this == EveryRun) {
            return true;
        }

        if (lastCheckTime == null) {
            return true;
        }

        Calendar c = Calendar.getInstance();
        c.add(field, -amount);

        return c.getTimeInMillis() > lastCheckTime;
    }

    @Override
    public Messages getLabel() {
        return description;
    }
}
