package org.xblackcat.rojac.data;

import org.xblackcat.rojac.gui.view.model.FavoriteType;
import org.xblackcat.rojac.util.RojacUtils;

/**
 * @author xBlackCat
 */

public class Favorite {
    protected final int itemId;
    protected final int id;
    protected final FavoriteType type;
    protected String name;

    public Favorite(int id, int itemId, FavoriteType type) {
        this(id, itemId, type, null);
    }

    private Favorite(int id, int itemId, FavoriteType type, String name) {
        this.id = id;
        this.itemId = itemId;
        this.type = type;
        this.name = name;
    }

    public int getItemId() {
        return itemId;
    }

    public int getId() {
        return id;
    }

    public FavoriteType getType() {
        return type;
    }

    public String getName() {
        assert RojacUtils.checkThread(true);

        if (name == null) {
            return getType().getTypeName("#" + id);
        } else {
            return name;
        }
    }

    public boolean isNameSet() {
        return name != null;
    }

    public Favorite setName(String name) {
        return new Favorite(id, itemId, type, name);
    }
}
