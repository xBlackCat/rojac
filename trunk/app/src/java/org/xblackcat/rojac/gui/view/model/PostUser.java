package org.xblackcat.rojac.gui.view.model;

import org.apache.commons.lang.StringUtils;
import org.xblackcat.rojac.gui.view.thread.PostTableCellRenderer;
import org.xblackcat.rojac.i18n.Message;
import org.xblackcat.rojac.util.UIUtils;

/**
 * Delegate class to identify data set for a table renderer.
 *
 * @author xBlackCat
 */
final class PostUser extends APostProxy {
    public PostUser(Post post) {
        super(post);
    }

    @Override
    protected void setValue(PostTableCellRenderer renderer) {
        final String userName = post.getMessageData().getUserName();
        if (StringUtils.isNotEmpty(userName)) {
            renderer.setText(userName);
        } else {
            renderer.setText(Message.UserName_Anonymous.get());
            renderer.setForeground(UIUtils.brighter(renderer.getForeground(), 0.33));
        }
    }
}
