package org.xblackcat.rojac.gui.view.thread;

import org.xblackcat.rojac.gui.view.model.Header;

import java.io.Serializable;

/**
 * @author xBlackCat
 */
class TableThreadViewLayout extends ThreadViewLayout {
    private static final long serialVersionUID = 1L;

    private Column[] columns;

    public TableThreadViewLayout(ThreadViewLayout parent, Column[] columns) {
        super(parent.getToolbarPosition(), parent.getToolbarOrientation());
        this.columns = columns;
    }

    public Column[] getColumns() {
        return columns;
    }

    static class Column implements Serializable {
        private static final long serialVersionUID = 1L;

        private Header anchor;
        private int index;
        private int width;

        Column(Header anchor, int index, int width) {
            this.anchor = anchor;
            this.index = index;
            this.width = width;
        }

        public Header getAnchor() {
            return anchor;
        }

        public int getIndex() {
            return index;
        }

        public int getWidth() {
            return width;
        }
    }
}
