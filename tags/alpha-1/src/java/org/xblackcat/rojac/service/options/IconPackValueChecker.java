package org.xblackcat.rojac.service.options;

import org.xblackcat.rojac.gui.theme.IconPack;

/**
 * @author xBlackCat
 */

public class IconPackValueChecker implements IValueChecker<IconPack> {
    public static final IconPack DEFAULT_ICON_PACK = new IconPack("Default pack", "images", "png");

    private static final IconPack[] ICON_PACKS = new IconPack[]{
            DEFAULT_ICON_PACK
    };

    @Override
    public IconPack[] getPossibleValues() {
        return ICON_PACKS;
    }

    @Override
    public String getValueDescription(IconPack v) throws IllegalArgumentException {
        return v.getName();
    }

    @Override
    public boolean isValueCorrect(IconPack v) {
        return DEFAULT_ICON_PACK.equals(v);
    }
}
