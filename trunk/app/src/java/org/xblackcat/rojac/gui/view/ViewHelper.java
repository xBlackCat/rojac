package org.xblackcat.rojac.gui.view;

import org.apache.commons.lang.NotImplementedException;
import org.xblackcat.rojac.gui.IMessageView;
import org.xblackcat.rojac.gui.IRootPane;
import org.xblackcat.rojac.gui.view.message.MessageView;
import org.xblackcat.rojac.gui.view.message.PreviewMessageView;
import org.xblackcat.rojac.gui.view.thread.ForumThreadsControl;
import org.xblackcat.rojac.gui.view.thread.SingleThreadControl;
import org.xblackcat.rojac.gui.view.thread.ThreadDoubleView;
import org.xblackcat.rojac.gui.view.thread.TreeThreadView;

/**
 * Helper class to replace sequence of 'new' operators.
 *
 * @author xBlackCat
 */

public final class ViewHelper {
    /**
     * Creates a tree-based forum view: list of all the available threads of the forum.
     *
     * @param mainFrame root component.
     *
     * @return a new instance of view
     */
    public static IMessageView makeForumThreadsView(IRootPane mainFrame) {
        return new TreeThreadView(mainFrame, new ForumThreadsControl());
    }

    /**
     * Creates a tree-based single thread view: only messages of the specified thread.
     *
     * @param mainFrame root component.
     *
     * @return a new instance of view
     */
    public static IMessageView makeSingleThreadView(IRootPane mainFrame) {
        return new TreeThreadView(mainFrame, new SingleThreadControl());
    }

    /**
     * Creates a message view.
     *
     * @param mainFrame root component.
     *
     * @return a new instance of view
     */
    public static IMessageView makeMessageView(IRootPane mainFrame) {
        return new MessageView(mainFrame);
    }

    /**
     * Creates a preview message view. Used to see a message before sending.
     *
     * @param mainFrame root component.
     *
     * @return a new instance of view
     */
    public static IMessageView makePreviewMessageView(IRootPane mainFrame) {
        return new PreviewMessageView(mainFrame);
    }

    /**
     * Creates a compound view. The view is split in to parts: left part contains a tree-based forum view and the right
     * part contains a message view.
     *
     * @param mainFrame root component.
     *
     * @return a new instance of view
     */
    public static IMessageView makeTreeMessageView(IRootPane mainFrame) {
        IMessageView threadView = ViewHelper.makeForumThreadsView(mainFrame);
        IMessageView messageView = ViewHelper.makeMessageView(mainFrame);

        return new ThreadDoubleView(threadView, messageView, false, mainFrame);
    }

    /**
     * Creates a compound view. The view is split in to parts: top part contains a tree-table-based forum view and the
     * bottom part contains a message view.
     *
     * @param mainFrame root component.
     *
     * @return a new instance of view
     */
    public static IMessageView makeTreeTableMessageView(IRootPane mainFrame) {
        throw new NotImplementedException("Just in case.");
    }
}
