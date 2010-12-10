package org.xblackcat.rojac.data.favorite;

/**
 * @author xBlackCat
 */

public class FavoriteOption {
    private final String name;
    private final FavoriteOptionType type;

    private FavoriteOption(String name, FavoriteOptionType type) {
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public FavoriteOptionType getType() {
        return type;
    }
}
