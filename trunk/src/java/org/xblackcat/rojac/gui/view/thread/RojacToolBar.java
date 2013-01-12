package org.xblackcat.rojac.gui.view.thread;

import org.xblackcat.rojac.gui.view.model.IModelControl;
import org.xblackcat.rojac.gui.view.model.Post;

import javax.swing.*;
import java.util.EnumMap;
import java.util.Map;

/**
 * 05.05.12 12:20
 *
 * @author xBlackCat
 */
public class RojacToolBar extends JToolBar {
    private final Map<ThreadToolbarActions, JComponent> toolbarButtons = new EnumMap<>(ThreadToolbarActions.class);

    public void add(ThreadToolbarActions action, TreeTableThreadView treeTableThreadView) {
        JButton button = action.makeButton(treeTableThreadView);
        toolbarButtons.put(action, button);
        add(button);
    }

    public void updateButtons(Post post, IModelControl modelControl) {
        for (Map.Entry<ThreadToolbarActions, JComponent> button : toolbarButtons.entrySet()) {
            button.getValue().setVisible(modelControl.isToolBarButtonVisible(button.getKey(), post));
        }
    }
}
