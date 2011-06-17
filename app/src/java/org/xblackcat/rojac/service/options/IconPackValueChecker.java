package org.xblackcat.rojac.service.options;

import org.xblackcat.rojac.gui.theme.IconPack;

import javax.swing.*;
import java.util.Arrays;
import java.util.List;

/**
 * @author xBlackCat
 */

public class IconPackValueChecker implements IValueChecker<IconPack> {
    public static final IconPack DEFAULT_ICON_PACK = new IconPack("Default pack", "images", "png");

    private static final List<IconPack> ICON_PACKS = Arrays.asList(
            DEFAULT_ICON_PACK
    );

    @Override
    public List<IconPack> getPossibleValues() {
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

    @Override
    public Icon getValueIcon(IconPack v) throws IllegalArgumentException {
        return null;
    }
}
