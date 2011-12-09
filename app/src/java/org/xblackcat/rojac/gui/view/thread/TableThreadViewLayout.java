package org.xblackcat.rojac.gui.view.thread;

import org.xblackcat.rojac.gui.IViewLayout;
import org.xblackcat.rojac.gui.view.model.Header;

import java.io.Serializable;

/**
 * @author xBlackCat
 */
class TableThreadViewLayout implements IViewLayout {
    private static final long serialVersionUID = 2L;

    private Column[] columns;
    private Object toolbarPosition;
    private int toolbarOrientation;
    private int dividerLocation;

    public TableThreadViewLayout(int toolbarOrientation, int dividerLocation, Object toolbarPosition, Column... columns) {
        this.toolbarOrientation = toolbarOrientation;
        this.dividerLocation = dividerLocation;
        this.toolbarPosition = toolbarPosition;
        this.columns = columns;
    }

    public Column[] getColumns() {
        return columns;
    }

    public Object getToolbarPosition() {
        return toolbarPosition;
    }

    public int getToolbarOrientation() {
        return toolbarOrientation;
    }

    public int getDividerLocation() {
        return dividerLocation;
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
