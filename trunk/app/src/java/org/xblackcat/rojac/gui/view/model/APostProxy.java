package org.xblackcat.rojac.gui.view.model;

import org.xblackcat.rojac.gui.view.thread.PostTableCellRenderer;
import org.xblackcat.rojac.util.UIUtils;

import java.awt.*;

/**
 * @author xBlackCat
 */

public abstract class APostProxy {
    protected final Post post;

    public APostProxy(Post post) {
        this.post = post;
    }

    public void prepareRenderer(PostTableCellRenderer renderer) {
        int style;
        switch (post.isRead()) {
            default:
            case Read:
            case ReadPartially:
                style = Font.PLAIN;
                break;
            case Unread:
                style = Font.BOLD;
                break;
        }

        Font font = renderer.getFont().deriveFont(style);
        renderer.setFont(font);
        renderer.setText(null);
        renderer.setIcon(null);

        boolean ignored;

        if (post.isIgnored()) {
            ignored = true;
        } else if (PostUtils.isPostIgnoredByUser(post)) {
            ignored = true;
        } else {
            ignored = false;
        }

        if (ignored) {
            renderer.setForeground(UIUtils.brighter(renderer.getForeground(), .3));
        }
        setValue(renderer);
    }

    protected abstract void setValue(PostTableCellRenderer renderer);
}
