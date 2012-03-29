package org.xblackcat.rojac.gui.view.message;

import org.xblackcat.rojac.i18n.IDescribable;
import org.xblackcat.rojac.i18n.Message;

/**
 * 28.03.12 16:27
 *
 * @author xBlackCat
 */
public enum YoutubePreviewSize implements IDescribable {
    Normal(Message.PreviewLink_Youtube_SizeNormal),
    HighQuality(Message.PreviewLink_Youtube_SizeHq);
    private final Message msg;

    YoutubePreviewSize(Message msg) {
        this.msg = msg;
    }

    @Override
    public Message getLabel() {
        return msg;
    }
}
