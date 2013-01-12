package org.xblackcat.rojac.gui.tray;

import org.xblackcat.rojac.i18n.Message;
import org.xblackcat.rojac.util.RojacUtils;
import org.xblackcat.utils.ResourceUtils;

import java.awt.*;

/**
 * @author xBlackCat
 */

public enum RojacState {
    Initialization(Message.Tray_State_Loading, ResourceUtils.loadImage("/images/tray/loading.png")),
    Synchronizing(Message.Tray_State_Synchronization, ResourceUtils.loadImage("/images/tray/synchronization.png")),
    NoUnreadMessages(Message.Tray_State_Normal, ResourceUtils.loadImage("/images/tray/normal.png")),
    HaveUnreadMessages(Message.Tray_State_HaveUnreadMessages, ResourceUtils.loadImage("/images/tray/has-unread.png")),
    HaveUnreadReplies(Message.Tray_State_HaveUnreadReplies, ResourceUtils.loadImage("/images/tray/has-replies.png"));

    private final Image image;
    private final Message title;

    RojacState(Message title, Image image) {
        this.title = title;
        this.image = image;
    }

    public Image getImage() {
        return image;
    }

    public String getToolTip(Object... arguments) {
        return RojacUtils.VERSION_STRING + "\n" + title.get(arguments);
    }
}
