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
    Initialized(Messages.Tray_State_Initialized, ResourceUtils.loadImage("/images/tray/initialized.png")),
    Normal(Messages.Tray_State_Normal, ResourceUtils.loadImage("/images/tray/normal.png")),
    Synchronizing(Messages.Tray_State_Synchronization, ResourceUtils.loadImage("/images/tray/synchronization.png")),
    HaveUnreadMessages(Messages.Tray_State_HaveUnreadMessages, ResourceUtils.loadImage("/images/tray/has_unread.png"));

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
