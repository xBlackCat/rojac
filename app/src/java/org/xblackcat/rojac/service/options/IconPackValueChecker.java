package org.xblackcat.rojac.service.options;

import org.xblackcat.rojac.gui.theme.IconPack;

/**
 * @author xBlackCat
 */

public class IconPackValueChecker implements IValueChecker<IconPack> {
    @Override
    public IconPack[] getPossibleValues() {
        return new IconPack[0];
    }

    @Override
    public String getValueDescription(IconPack v) throws IllegalArgumentException {
        return v.getName();
    }

    @Override
    public boolean isValueCorrect(IconPack v) {
        return false;
    }
}
