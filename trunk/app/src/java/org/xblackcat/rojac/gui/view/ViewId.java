package org.xblackcat.rojac.gui.view;

import java.io.Serializable;

/**
 * @author xBlackCat
 */

public class ViewId implements Serializable {
    private static final long serialVersionUID = 1L;

    private final int id;
    private final ViewType type;

    ViewId(int id, ViewType type) {
        this.id = id;
        this.type = type;
    }

    public ViewType getType() {
        return type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ViewId)) return false;

        ViewId ViewId = (ViewId) o;

        return id == ViewId.id && type == ViewId.type;

    }

    @Override
    public int hashCode() {
        return 31 * id + type.hashCode();
    }

    public int getId() {
        return id;
    }

    public String getAnchor() {
        return type.name() + "_" + id;
    }
}
