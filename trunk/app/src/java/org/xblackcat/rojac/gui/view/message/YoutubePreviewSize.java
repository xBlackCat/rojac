package org.xblackcat.rojac.gui.view.message;

import org.xblackcat.rojac.i18n.IDescribable;
import org.xblackcat.rojac.i18n.Message;

/**
 * 28.03.12 16:27
 *
 * @author xBlackCat
 */
public enum YoutubePreviewSize implements IDescribable {
    Normal(null),
    HighQuality(null);
    private final Message msg;

    YoutubePreviewSize(Message msg) {
        this.msg = msg;
    }

    @Override
    public Message getLabel() {
        return msg;
    }
}
