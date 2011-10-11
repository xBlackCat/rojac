package org.xblackcat.rojac.service.storage.importing;

/**
 * 05.10.11 10:29
 *
 * @author xBlackCat
 */
public class Cell {
    private final String name;
    private final Object data;

    public Cell(String name, Object data) {
        this.name = name;
        this.data = data;
    }

    public String getName() {
        return name;
    }

    public Object getData() {
        return data;
    }
}
