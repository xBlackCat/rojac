package org.xblackcat.rojac.gui.view.thread;

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
                style = Font.PLAIN;
                break;
            case ReadPartially:
                style = Font.ITALIC;
                break;
            case Unread:
                style = Font.BOLD | Font.ITALIC;
                break;
        }

        Font font = renderer.getFont().deriveFont(style);
        renderer.setFont(font);
        renderer.setText(null);
        renderer.setIcon(null);

        setValue(renderer);
    }

    protected abstract void setValue(PostTableCellRenderer renderer);
}
