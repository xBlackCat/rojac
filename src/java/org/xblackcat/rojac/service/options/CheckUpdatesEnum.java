package org.xblackcat.rojac.service.options;

import org.xblackcat.rojac.i18n.IDescribable;
import org.xblackcat.rojac.i18n.Message;

import java.util.Calendar;

/**
 * @author xBlackCat
 */

public enum CheckUpdatesEnum implements IDescribable {
    None(Message.Description_UpdatePeriod_None, 0, 0),
    EveryRun(Message.Description_UpdatePeriod_EveryRun, 0, 0),
    EveryDay(Message.Description_UpdatePeriod_EveryDay, Calendar.DAY_OF_MONTH, 1),
    EveryWeek(Message.Description_UpdatePeriod_EveryWeek, Calendar.DAY_OF_MONTH, 7),
    EveryMonth(Message.Description_UpdatePeriod_EveryMonth, Calendar.MONTH, 1);

    private final Message description;
    private final int field;
    private final int amount;

    CheckUpdatesEnum(Message description, int field, int amount) {
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
    public Message getLabel() {
        return description;
    }
}
