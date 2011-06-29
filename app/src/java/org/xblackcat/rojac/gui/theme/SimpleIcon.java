package org.xblackcat.rojac.gui.theme;

/**
 * @author xBlackCat Date: 29.06.11
 */
class SimpleIcon implements AnIcon {
    private final String filename;

    SimpleIcon(String filename) {
        this.filename = filename;
    }

    @Override
    public String getFilename() {
        return filename;
    }
}
