package org.xblackcat.rojac.service.options;

import org.xblackcat.rojac.i18n.Messages;

import java.util.Calendar;

/**
 * @author xBlackCat
 */

public enum CheckUpdatesEnum {
    None(Messages.DESCRIPTION_UPDATE_PERIOD_NONE, 0, 0),
    EveryRun(Messages.DESCRIPTION_UPDATE_PERIOD_EVERYRUN, 0, 0),
    EveryDay(Messages.DESCRIPTION_UPDATE_PERIOD_EVERYDAY, Calendar.DAY_OF_MONTH, 1),
    EveryWeek(Messages.DESCRIPTION_UPDATE_PERIOD_EVERYWEEK, Calendar.DAY_OF_MONTH, 7),
    EveryMonth(Messages.DESCRIPTION_UPDATE_PERIOD_EVERYMONTH, Calendar.MONTH, 1);

    private final Messages description;
    private final int field;
    private final int amount;

    CheckUpdatesEnum(Messages description, int field, int amount) {
        this.description = description;
        this.field = field;
        this.amount = amount;
    }

    public String description() {
        return description.get();
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
}
