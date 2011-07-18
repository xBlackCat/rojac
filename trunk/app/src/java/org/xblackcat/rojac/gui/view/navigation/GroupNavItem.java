package org.xblackcat.rojac.gui.view.navigation;

import org.xblackcat.rojac.i18n.Message;

import java.util.Collections;

/**
 * @author xBlackCat
 */
public class GroupNavItem extends ANavItem {
    private final Message title;

    protected GroupNavItem(ANavItem parent, Message title) {
        super(true, parent);
        this.title = title;
        children = Collections.emptyList();
    }

    @Override
    String getBriefInfo() {
        return String.valueOf(children.size());
    }

    @Override
    String getExtraInfo() {
        return null;
    }

    @Override
    String getTitleLine() {
        return title.get();
    }

    @Override
    String getExtraTitleLine() {
        return null;
    }
}
