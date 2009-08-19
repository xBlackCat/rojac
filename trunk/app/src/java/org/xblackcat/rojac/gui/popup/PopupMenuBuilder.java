package org.xblackcat.rojac.gui.popup;

import org.xblackcat.rojac.data.Forum;
import org.xblackcat.rojac.gui.IRootPane;
import org.xblackcat.rojac.gui.model.ForumTableModel;
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
     *
     * @return new popup menu for the specified url.
     */
    public static JPopupMenu getLinkMenu(URL url, String description, String text) {
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

        PopupTypeEnum type = PopupTypeEnum.LinkCommonPopup;
        Integer messageId = LinkUtils.getMessageId(description);
        if (messageId == null) {
            messageId = LinkUtils.getMessageId(text);
        }
        if (messageId != null) {
            type = PopupTypeEnum.LinkMessagePopup;
        }

        return getBuilder(type, description, messageId, text);
    }

    public static JPopupMenu getForumViewMenu(Forum forum, ForumTableModel forumsModel, IRootPane rootPane) {
        return getBuilder(PopupTypeEnum.ForumListCommonPopup, forum, forumsModel, rootPane);
    }

    public static JPopupMenu getTreeViewPopup(int messageId) {
        return getBuilder(PopupTypeEnum.TreeThreadViewPopup, Integer.valueOf(messageId));
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
        builderMap.put(PopupTypeEnum.LinkMessagePopup, new LinkMessagePopupBuilder());
        builderMap.put(PopupTypeEnum.LinkDownloadablePopup, new LinkDownloadablePopupBuilder());
        builderMap.put(PopupTypeEnum.LinkCommonPopup, new LinkPopupBuilder());
        builderMap.put(PopupTypeEnum.ForumListCommonPopup, new ForumListPopupBuilder());
        builderMap.put(PopupTypeEnum.TreeThreadViewPopup, new TreeThreadViewPopupBuilder());

        BUILDERS = Collections.unmodifiableMap(builderMap);
    }
}