package org.xblackcat.rojac.gui.frame.mtree;

/**
 * @author xBlackCat
 */

public enum Columns {
    Id {public Object getTitile() {
        return "Message Id";
    }},
    Topic {public Object getTitile() {
        return "Topic";
    }},
    Date {public Object getTitile() {
        return "Date";
    }},

    Ratings {public Object getTitile() {
        return "Ratings";
    }},
    Author {public Object getTitile() {
        return "Author";
    }},
    Favorite {public Object getTitile() {
        return "";
    }};

    public int getIndex() {
        return ordinal();
    }

    public abstract Object getTitile();
}
