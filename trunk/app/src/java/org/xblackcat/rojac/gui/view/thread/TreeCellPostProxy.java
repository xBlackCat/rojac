package org.xblackcat.rojac.gui.view.thread;

import org.xblackcat.rojac.gui.component.GrayedIcon;
import org.xblackcat.rojac.gui.view.model.APostProxy;
import org.xblackcat.rojac.gui.view.model.ForumRoot;
import org.xblackcat.rojac.gui.view.model.Post;
import org.xblackcat.rojac.gui.view.model.PostUtils;
import org.xblackcat.rojac.util.UIUtils;

import javax.swing.*;

/**
 * 30.03.12 11:27
 *
 * @author xBlackCat
 */
class TreeCellPostProxy extends APostProxy {
    public TreeCellPostProxy(Post post) {
        super(post);
    }

    @Override
    protected void setValue(PostTableCellRenderer renderer, boolean ignored) {
        Icon icon = PostUtils.getPostIcon(post);

        if (!(post instanceof ForumRoot)) {
            if (ignored) {
                icon = new GrayedIcon(icon);
                renderer.setForeground(UIUtils.brighter(renderer.getForeground(), .3));
            }
        }

        renderer.setIcon(icon);

        renderer.setText(post.getMessageData().getSubject());
    }
}
