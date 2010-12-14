package org.xblackcat.rojac.gui.view.model;

import org.xblackcat.rojac.gui.view.thread.PostTableCellRenderer;
import org.xblackcat.rojac.service.options.Property;

import java.text.DateFormat;
import java.util.Date;

/**
 * Delegate class to identify data set for a table renderer.
 *
 * @author xBlackCat
 */
final class PostDate extends APostProxy {
    public PostDate(Post post) {
        super(post);
    }

    @Override
    protected void setValue(PostTableCellRenderer renderer) {
        DateFormat format = DateFormat.getDateTimeInstance(
                DateFormat.MEDIUM,
                DateFormat.MEDIUM,
                Property.ROJAC_GUI_LOCALE.get()
        );

        renderer.setText(format.format(new Date(post.getMessageData().getMessageDate())));
    }
}
