package org.xblackcat.rojac.gui.tray;

import org.apache.commons.lang.ArrayUtils;
import org.xblackcat.rojac.i18n.Messages;
import org.xblackcat.rojac.util.RojacUtils;
import org.xblackcat.utils.ResourceUtils;

import java.awt.*;

/**
 * @author xBlackCat
 */

public enum RojacState {
    Initialized(Messages.TRAY_STATE_INITIALIZED, ResourceUtils.loadImage("/images/tray/initialized.png")),
    Normal(Messages.TRAY_STATE_NORMAL, ResourceUtils.loadImage("/images/tray/normal.png")),
    Synchronizing(Messages.TRAY_STATE_SYNCHRONIZATION, ResourceUtils.loadImage("/images/tray/synchronization.png")),
    HaveUnreadMessages(Messages.TRAY_STATE_HAVE_UNREAD_MESSAGES, ResourceUtils.loadImage("/images/tray/has_unread.png"));

    private final Image image;
    private final Messages title;

    RojacState(Messages title, Image image) {
        this.title = title;
        this.image = image;
    }

    public Image getImage() {
        return image;
    }

    public String getToolTip(Object... arguments) {
        return title.get(ArrayUtils.add(arguments, 0, RojacUtils.VERSION_STRING));
    }
}
