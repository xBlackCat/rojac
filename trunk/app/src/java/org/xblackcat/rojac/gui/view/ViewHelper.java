package org.xblackcat.rojac.gui.view;

import net.infonode.docking.View;
import org.apache.commons.lang.NotImplementedException;
import org.xblackcat.rojac.RojacDebugException;
import org.xblackcat.rojac.gui.IItemView;
import org.xblackcat.rojac.gui.IRootPane;
import org.xblackcat.rojac.gui.IView;
import org.xblackcat.rojac.gui.MainFrame;
import org.xblackcat.rojac.gui.ViewId;
import org.xblackcat.rojac.gui.view.message.MessageView;
import org.xblackcat.rojac.gui.view.thread.SortedForumThreadsControl;
import org.xblackcat.rojac.gui.view.thread.ThreadDoubleView;
import org.xblackcat.rojac.gui.view.thread.TreeTableThreadView;
import org.xblackcat.rojac.gui.view.thread.TreeThreadView;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * Helper class to replace sequence of 'new' operators.
 *
 * @author xBlackCat
 */

public final class ViewHelper {
    /**
     * Creates a tree-based forum view: list of all the available threads of the forum.
     *
     * @param id        associated id
     * @param mainFrame root component.
     *
     * @return a new instance of view
     */
    public static IItemView makeForumThreadsView(ViewId id, IRootPane mainFrame) {
        return new TreeThreadView(id, mainFrame, new SortedForumThreadsControl());
    }

    public static IItemView makeForumTTThreadsView(ViewId id, IRootPane mainFrame) {
        return new TreeTableThreadView(id, mainFrame, new SortedForumThreadsControl());
    }

    /**
     * Creates a message view.
     *
     * @param id        associated id
     * @param mainFrame root component.
     *
     * @return a new instance of view
     */
    public static IItemView makeMessageView(ViewId id, IRootPane mainFrame) {
        return new MessageView(id, mainFrame);
    }

    /**
     * Creates a compound view. The view is split in to parts: left part contains a tree-based forum view and the right
     * part contains a message view.
     *
     * @param id        associated id
     * @param mainFrame root component.
     *
     * @return a new instance of view
     */
    public static IItemView makeTreeMessageView(ViewId id, IRootPane mainFrame) {
        IItemView threadView = ViewHelper.makeForumThreadsView(id, mainFrame);
        IItemView messageView = ViewHelper.makeMessageView(null, mainFrame);

        return new ThreadDoubleView(threadView, messageView, false, mainFrame);
    }

    /**
     * Creates a compound view. The view is split in to parts: top part contains a tree-table-based forum view and the
     * bottom part contains a message view.
     *
     * @param id        associated id
     * @param mainFrame root component.
     *
     * @return a new instance of view
     */
    public static IItemView makeTreeTableMessageView(ViewId id, IRootPane mainFrame) {
        IItemView threadView = ViewHelper.makeForumTTThreadsView(id, mainFrame);
        IItemView messageView = ViewHelper.makeMessageView(null, mainFrame);

        return new ThreadDoubleView(threadView, messageView, true, mainFrame);
    }

    public static void storeView(ObjectOutputStream out, View view) throws IOException {
        IView v = (IView) view.getComponent();

        ViewId id = v.getId();
        // Store view id for future identification.
        out.writeObject(id);
        out.writeObject(view.getTitle());

        ViewType type = id.getType();

        Object stateObject;
        switch (type) {
            case Forum:
                stateObject = new ForumState(((IItemView) v).getVisibleId());
                break;
            case SingleThread:
                stateObject = new ThreadState();
                break;
            case SingleMessage:
                stateObject = null; // No state
                break;
            default:
                // Handle feature expanding views.
                throw new RojacDebugException("Can not serialize view type " + type);
        }

        out.writeObject(stateObject);
        out.flush();
    }

    public static View initializeView(ObjectInputStream in, MainFrame mainFrame) throws IOException, ClassNotFoundException {
        ViewId id = (ViewId) in.readObject();
        String title = (String) in.readObject();
        IItemView view;

        switch (id.getType()) {
            case Forum: {
                view = makeTreeTableMessageView(id, mainFrame);
                view.loadItem(((ItemId) id).getId());
                ForumState state = (ForumState) in.readObject();
                if (state.openedMessageId() > 0) {
                    view.makeVisible(state.openedMessageId());
                }
                break;
            }
            case SingleThread: {
                throw new NotImplementedException();
//                ThreadState state = (ThreadState) in.readObject();
            }
//            break;
            case SingleMessage: {
                view = makeMessageView(id, mainFrame);
                view.loadItem(((ItemId) id).getId());
                break;
            }
            default:
                // Handle feature expanding views.
                throw new RojacDebugException("Can not un-serialize view type " + id.getType());
        }

        return new View(title, null, view.getComponent());
    }

    private static final class ForumState implements Serializable {
        private static final long serialVersionUID = 1L;

        private int openedMessageId;

        public ForumState(int openedMessageId) {
            this.openedMessageId = openedMessageId;
        }

        public int openedMessageId() {
            return openedMessageId;
        }
    }

    private static final class ThreadState implements Serializable {
        private static final long serialVersionUID = 1L;

    }
}
