package org.xblackcat.rojac.gui.view.thread;

import org.apache.commons.lang3.StringUtils;
import org.xblackcat.rojac.gui.component.GrayedIcon;
import org.xblackcat.rojac.gui.view.model.*;
import org.xblackcat.rojac.gui.view.model.Thread;
import org.xblackcat.rojac.i18n.Message;
import org.xblackcat.rojac.util.MessageUtils;
import org.xblackcat.rojac.util.UIUtils;

import javax.swing.*;

/**
 * 30.03.12 11:27
 *
 * @author xBlackCat
 */
class TreeCellPostProxy extends APostProxy {
    private final boolean mergeSubjectUser;

    public TreeCellPostProxy(boolean mergeSubjectUser, Post post) {
        super(post);
        this.mergeSubjectUser = mergeSubjectUser;
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

        String subject = post.getMessageData().getSubject();
        if (mergeSubjectUser) {
            Post parent = post.getParent();
            boolean merge = post instanceof Thread || parent == null || !MessageUtils.isSubjectSimilar(parent.getMessageData().getSubject(), subject);

            String userName = post.getMessageData().getUserName();
            if (merge) {
                if (StringUtils.isBlank(userName)) {
                    userName = Message.UserName_Anonymous.get();
                }
                renderer.setText("[" + userName + "] " + subject);
            } else {
                if (StringUtils.isBlank(userName)) {
                    renderer.setForeground(UIUtils.brighter(renderer.getForeground(), 0.33));
                    renderer.setText(Message.UserName_Anonymous.get());
                } else {
                    renderer.setText(userName);
                }
            }
        } else {
            renderer.setText(subject);
        }
    }
}
