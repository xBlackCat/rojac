package org.xblackcat.rojac.gui.view.model;

/**
 * 07.05.12 21:17
 *
 * @author xBlackCat
 */
public enum ViewMode {
    Normal(1,
            Header.ID,
            Header.SUBJECT,
            Header.USER,
            Header.REPLIES,
            Header.RATING,
            Header.DATE),
    Compact(1,
            Header.ID,
            Header.SUBJECT_USER,
            Header.REPLIES,
            Header.RATING,
            Header.DATE),
    OwnPosts(1,
            Header.ID,
            Header.SUBJECT,
            Header.REPLIES,
            Header.RATING,
            Header.DATE
            );

    private final Header[] headers;
    private final int hierarchicalColumn;

    private ViewMode(int hierarchicalColumn, Header... headers) {
        this.hierarchicalColumn = hierarchicalColumn;
        this.headers = headers;
    }

    public Header[] getHeaders() {
        return headers;
    }

    public int getHierarchicalColumn() {
        return hierarchicalColumn;
    }
}
