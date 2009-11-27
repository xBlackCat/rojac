package org.xblackcat.rojac.gui.view;

import org.apache.commons.lang.NotImplementedException;
import org.xblackcat.rojac.gui.IItemView;
import org.xblackcat.rojac.gui.IRootPane;
import org.xblackcat.rojac.gui.view.message.MessageView;
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
    public static IItemView makeForumThreadsView(IRootPane mainFrame) {
        return new TreeThreadView(mainFrame, new ForumThreadsControl());
    }

    /**
     * Creates a tree-based single thread view: only messages of the specified thread.
     *
     * @param mainFrame root component.
     *
     * @return a new instance of view
     */
    public static IItemView makeSingleThreadView(IRootPane mainFrame) {
        return new TreeThreadView(mainFrame, new SingleThreadControl());
    }

    /**
     * Creates a message view.
     *
     * @param mainFrame root component.
     *
     * @return a new instance of view
     */
    public static IItemView makeMessageView(IRootPane mainFrame) {
        return new MessageView(mainFrame);
    }

    /**
     * Creates a compound view. The view is split in to parts: left part contains a tree-based forum view and the right
     * part contains a message view.
     *
     * @param mainFrame root component.
     *
     * @return a new instance of view
     */
    public static IItemView makeTreeMessageView(IRootPane mainFrame) {
        IItemView threadView = ViewHelper.makeForumThreadsView(mainFrame);
        IItemView messageView = ViewHelper.makeMessageView(mainFrame);

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
    public static IItemView makeTreeTableMessageView(IRootPane mainFrame) {
        throw new NotImplementedException("Just in case.");
    }
}
