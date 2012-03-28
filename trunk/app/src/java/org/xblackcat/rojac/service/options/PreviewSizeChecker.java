package org.xblackcat.rojac.service.options;

import org.xblackcat.rojac.gui.view.message.PreviewSize;

/**
 * 28.03.12 18:07
 *
 * @author xBlackCat
 */
class PreviewSizeChecker extends GeneralEnumChecker<PreviewSize> {
    PreviewSizeChecker() {
        super(PreviewSize.class);
    }

    @Override
    public String getValueDescription(PreviewSize v) throws IllegalArgumentException {
        return v.asString();
    }
}
