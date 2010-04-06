package org.xblackcat.rojac.gui.popup;

import org.xblackcat.rojac.gui.IRootPane;
import org.xblackcat.rojac.gui.view.forumlist.ForumData;
import org.xblackcat.rojac.gui.view.forumlist.ForumTableModel;
import org.xblackcat.rojac.gui.view.thread.AThreadModel;
import org.xblackcat.rojac.gui.view.thread.ITreeItem;
import org.xblackcat.rojac.util.LinkUtils;

import javax.swing.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Class for prepare pop-up menus for the application.
 *
 * @author xBlackCat
 */
public class PopupMenuBuilder {
    private static final Map<PopupTypeEnum, IPopupBuilder> BUILDERS;

    public PopupMenuBuilder() {
    }

    /**
     * Constructs a pop-up menu for link in message pane.
     *
     * @param url         URL object of the link.
     * @param description Text representing of the link.
     * @param text        Text associated with the link.
     * @param mainFrame   back link to main window control.
     *
     * @return new popup menu for the specified url.
     */
    public static JPopupMenu getLinkMenu(URL url, String description, String text, IRootPane mainFrame) {
        if (url == null) {
            // Invalid url. Try to parse it from text.

            try {
                url = new URL(text);
                // Url in the 'text' field, so assume that text in the 'description' field 
                text = description;
                description = url.toExternalForm();
            } catch (MalformedURLException e) {
                // url can not be obtained neither from text or description.
            }
        }

        PopupTypeEnum type = PopupTypeEnum.LinkPopup;
        Integer messageId = LinkUtils.getMessageId(description);
        if (messageId == null) {
            messageId = LinkUtils.getMessageId(text);
        }

        return getBuilder(type, messageId, description, text, mainFrame);
    }

    public static JPopupMenu getForumViewMenu(ForumData forum, ForumTableModel forumsModel, IRootPane rootPane) {
        return getBuilder(PopupTypeEnum.ForumListCommonPopup, forum, forumsModel, rootPane);
    }

    public static JPopupMenu getTreeViewPopup(ITreeItem messageItem, AThreadModel model, IRootPane mainFrame) {
        return getBuilder(PopupTypeEnum.TreeThreadViewPopup, messageItem, model, mainFrame);
    }

    private static JPopupMenu getBuilder(PopupTypeEnum type, Object... parameters) {
        IPopupBuilder builder = BUILDERS.get(type);
        if (builder == null) {
            throw new IllegalStateException("Undefined popup menu builder: " + type);
        }
        return builder.buildMenu(parameters);
    }

    static {
        HashMap<PopupTypeEnum, IPopupBuilder> builderMap = new HashMap<PopupTypeEnum, IPopupBuilder>();
        // Initialize builders map.
        builderMap.put(PopupTypeEnum.LinkPopup, new LinkMessagePopupBuilder());
        builderMap.put(PopupTypeEnum.ForumListCommonPopup, new ForumListPopupBuilder());
        builderMap.put(PopupTypeEnum.TreeThreadViewPopup, new TreeThreadViewPopupBuilder());

        BUILDERS = Collections.unmodifiableMap(builderMap);
    }
}
