package org.xblackcat.rojac.gui.view.model;

import org.xblackcat.rojac.i18n.IDescribable;
import org.xblackcat.rojac.i18n.Message;

/**
 * 07.05.12 21:17
 *
 * @author xBlackCat
 */
public enum ViewMode implements IDescribable {
    Normal(Message.View_Thread_Mode_Normal,
            1,
            Header.ID,
            Header.SUBJECT,
            Header.USER,
            Header.REPLIES,
            Header.RATING,
            Header.DATE),
    Compact(Message.View_Thread_Mode_Compact,
            1,
            Header.ID,
            Header.SUBJECT_USER,
            Header.REPLIES,
            Header.RATING,
            Header.DATE),
    OwnPosts(null,
            1,
            Header.ID,
            Header.SUBJECT,
            Header.REPLIES,
            Header.RATING,
            Header.DATE
    );

    private final Header[] headers;
    private final int hierarchicalColumn;
    private final Message description;

    private ViewMode(Message description, int hierarchicalColumn, Header... headers) {
        this.hierarchicalColumn = hierarchicalColumn;
        this.headers = headers;
        this.description = description;
    }

    public Header[] getHeaders() {
        return headers;
    }

    public int getHierarchicalColumn() {
        return hierarchicalColumn;
    }

    @Override
    public Message getLabel() {
        return description;
    }
}
