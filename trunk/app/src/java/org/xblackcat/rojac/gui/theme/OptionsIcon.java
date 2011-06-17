package org.xblackcat.rojac.gui.theme;

/**
 * Definition of available icons to be used in thread view for posts.
 *
 * @author xBlackCat
 */

public enum OptionsIcon implements AnIcon {
    Confirm("button-confirm.png"),
    Cancel("button-cancel.png"),
    Enabled("option-enabled.png"),
    Disabled("option-disabled.png");

    private final String filename;

    private OptionsIcon(String filename) {
        this.filename = filename;
    }

    public String getFilename() {
        return filename;
    }
}
