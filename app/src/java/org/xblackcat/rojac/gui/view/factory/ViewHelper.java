package org.xblackcat.rojac.gui.view.factory;

import org.xblackcat.rojac.RojacDebugException;
import org.xblackcat.rojac.gui.IAppControl;
import org.xblackcat.rojac.gui.IItemView;
import org.xblackcat.rojac.gui.IState;
import org.xblackcat.rojac.gui.IViewLayout;
import org.xblackcat.rojac.gui.view.ViewId;
import org.xblackcat.rojac.gui.view.ViewType;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;

/**
 * Helper class to replace sequence of 'new' operators.
 *
 * @author xBlackCat
 */

public final class ViewHelper {
    private static final Map<ViewType, IViewFactory> VIEW_FACTORIES;

    static {
        EnumMap<ViewType, IViewFactory> map = new EnumMap<ViewType, IViewFactory>(ViewType.class);
        map.put(ViewType.Forum, new ForumThreadViewFactory());
        map.put(ViewType.SingleThread, new SingleThreadViewFactory());
        map.put(ViewType.SingleMessage, new MessageViewFactory());
        map.put(ViewType.Favorite, new FavoriteViewFactory());
        map.put(ViewType.PostList, new UserPostListFactory());
        map.put(ViewType.ReplyList, new UserReplyListFactory());

        for (ViewType t : ViewType.values()) {
            if (map.get(t) == null) {
                throw new RojacDebugException("A view factory is not defined for " + t + " view type.");
            }
        }

        VIEW_FACTORIES = Collections.unmodifiableMap(map);
    }

    /**
     * Stores a view state for future restoring docking layout.
     *
     * @param out  output stream to store a view state
     * @param itemView
     * @throws IOException exception will be thrown if write can not be performed.
     */
    public static void storeView(ObjectOutputStream out, IItemView itemView) throws IOException {

        ViewId id = itemView.getId();
        // Store view id for future identification.
        out.writeObject(id);

        IViewLayout layout = itemView.storeLayout();

        IState stateObject = itemView.getObjectState();

        out.writeObject(layout);
        out.writeObject(stateObject);
        out.flush();
    }

    /**
     * Restores a view in previously saved state.
     *
     * @param in         input stream with configuration.
     * @param appControl application global control interface to be used in restored views.
     *
     * @return restored and initialized view.
     *
     * @throws IOException            will be thrown if read from stream can not be performed.
     * @throws ClassNotFoundException will be thrown if object from stream can not be identified.
     */
    public static IItemView initializeView(ObjectInputStream in, IAppControl appControl) throws IOException, ClassNotFoundException {
        ViewId id = (ViewId) in.readObject();
        IItemView view = makeView(id, appControl);

        Object o = in.readObject();

        if (o instanceof IViewLayout) {
            view.setupLayout((IViewLayout) o);
            o = in.readObject();
        }

        if (o instanceof IState) {
            view.setObjectState((IState) o);
        }

        return view;
    }

    /**
     * Creates a view by unique viewId.
     *
     * @param viewId     target view identifier.
     * @param appControl application control to be used in a view.
     *
     * @return created view.
     */
    public static IItemView makeView(ViewId viewId, IAppControl appControl) {
        IViewFactory factory = VIEW_FACTORIES.get(viewId.getType());

        assert factory != null;

        return factory.makeView(viewId, appControl);
    }

}
