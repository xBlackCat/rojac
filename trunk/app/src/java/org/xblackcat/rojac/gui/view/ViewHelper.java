package org.xblackcat.rojac.gui.view;

import net.infonode.docking.View;
import org.xblackcat.rojac.RojacDebugException;
import org.xblackcat.rojac.gui.IItemView;
import org.xblackcat.rojac.gui.IRootPane;
import org.xblackcat.rojac.gui.IView;
import org.xblackcat.rojac.gui.MainFrame;
import org.xblackcat.rojac.gui.ViewId;
import org.xblackcat.rojac.gui.view.message.MessageView;
import org.xblackcat.rojac.gui.view.thread.*;

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

    public static IItemView constructForumThreadsView(ViewId id, IRootPane mainFrame, boolean singleThread) {
        IThreadControl<Post> threadControl = singleThread ? new SingleThreadControl() : new SortedForumThreadsControl();
        boolean useTreeTable = true;
        if (useTreeTable) {
            return new TreeTableThreadView(id, mainFrame, threadControl);
        } else {
            return new TreeThreadView(id, mainFrame, threadControl);
        }
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
     * Creates a compound view. The view is split in to parts: top part contains a tree-table-based forum view and the
     * bottom part contains a message view.
     *
     * @param id        associated id
     * @param mainFrame root component.
     *
     * @return a new instance of view
     */
    public static IItemView constructThreadView(ViewId id, IRootPane mainFrame) {
        IItemView threadView = ViewHelper.constructForumThreadsView(id, mainFrame, true);
        IItemView messageView = ViewHelper.makeMessageView(null, mainFrame);

        return new ThreadDoubleView(threadView, messageView, true, mainFrame);
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
    public static IItemView constructForumView(ViewId id, IRootPane mainFrame) {
        IItemView threadView = ViewHelper.constructForumThreadsView(id, mainFrame, false);
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
                stateObject = new ThreadState(((IItemView) v).getVisibleId());
                break;
            case SingleThread:
                stateObject = new ThreadState(((IItemView) v).getVisibleId());
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
                view = constructForumView(id, mainFrame);
                view.loadItem(((ItemId) id).getId());
                Object o = in.readObject();
                if (o instanceof ThreadState) {
                    ThreadState state = (ThreadState) o;
                    if (state.openedMessageId() > 0) {
                        view.makeVisible(state.openedMessageId());
                    }
                }
                break;
            }
            case SingleThread: {
                view = constructThreadView(id, mainFrame);
                view.loadItem(((ItemId) id).getId());
                Object o = in.readObject();
                if (o instanceof ThreadState) {
                    ThreadState state = (ThreadState) o;
                    if (state.openedMessageId() > 0) {
                        view.makeVisible(state.openedMessageId());
                    }
                }
                break;
            }
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

    private static final class ThreadState implements Serializable {
        private static final long serialVersionUID = 1L;

        private int openedMessageId;

        public ThreadState(int openedMessageId) {
            this.openedMessageId = openedMessageId;
        }

        public int openedMessageId() {
            return openedMessageId;
        }
    }
}
