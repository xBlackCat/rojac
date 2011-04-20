package org.xblackcat.rojac.gui.view;

import net.infonode.docking.View;
import org.xblackcat.rojac.gui.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Helper class to replace sequence of 'new' operators.
 *
 * @author xBlackCat
 */

public final class ViewHelper {
    /**
     * Stores a view state for future restoring docking layout.
     *
     * @param out  output stream to store a view state
     * @param view view to be stored.
     *
     * @throws IOException exception will be thrown if write can not be performed.
     */
    public static void storeView(ObjectOutputStream out, View view) throws IOException {
        IView v = (IView) view.getComponent();

        ViewId id = v.getId();
        // Store view id for future identification.
        out.writeObject(id);

        IViewLayout layout = v.storeLayout();

        IViewState stateObject = v.getState();

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

        if (o instanceof IViewState) {
            view.setState((IViewState) o);
        }

        view.loadItem(id.getId());

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
        return viewId.getType().getFactory().makeView(viewId, appControl);
    }

}
