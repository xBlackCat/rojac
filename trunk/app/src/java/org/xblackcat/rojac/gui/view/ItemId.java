package org.xblackcat.rojac.gui.view;

import org.xblackcat.rojac.gui.ViewId;

/**
* @author xBlackCat
*/
class ItemId implements ViewId {
    private static final long serialVersionUID = 1L;

    private final int id;
    private final ViewType type;

    ItemId(int id, ViewType type) {
        this.id = id;
        this.type = type;
    }

    @Override
    public ViewType getType() {
        return type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ItemId)) return false;

        ItemId itemId = (ItemId) o;

        return id == itemId.id && type == itemId.type;

    }

    @Override
    public int hashCode() {
        return 31 * id + type.hashCode();
    }

    public int getId() {
        return id;
    }
}
