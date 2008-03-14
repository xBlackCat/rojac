package org.xblackcat.sunaj.service.data;

import org.xblackcat.sunaj.i18n.Messages;
import org.xblackcat.sunaj.util.ResourceUtils;

import javax.swing.*;
import java.net.URL;

/**
 * Date: 28 ñ³÷ 2008
 *
 * @author xBlackCat
 */

public enum Mark {
    PlusOne(-3, Messages.DESCRIPTION_MARK_PLUSONE, "plus_one.gif"),
    Agree(-4, Messages.DESCRIPTION_MARK_AGREE, "plus.gif"),
    Disagree(0, Messages.DESCRIPTION_MARK_DISAGREE, "minus.gif"),
    x1(1, Messages.DESCRIPTION_MARK_X1, "x1.gif"),
    x2(2, Messages.DESCRIPTION_MARK_X2, "x2.gif"),
    x3(3, Messages.DESCRIPTION_MARK_X3, "x3.gif"),
    Smile(-2, Messages.DESCRIPTION_MARK_SMILE, "smile.gif"),
    Remove(-1, Messages.DESCRIPTION_MARK_REMOVE, "smile.gif");

    private static final String IMAGES_MARKS_PATH = "/images/marks/";

    public static Mark getMark(int code) {
        for (Mark m : values()) {
            if (m.getValue() == code) {
                return m;
            }
        }
        throw new IllegalArgumentException("Unknown mark type: " + code);
    }

    private final int value;
    private final String path;
    private final Messages description;

    private Mark(int value, Messages description, String imgName) {
        this.value = value;
        this.description = description;
        path = IMAGES_MARKS_PATH + imgName;
    }

    public int getValue() {
        return value;
    }

    public Icon getIcon() {
        return ResourceUtils.loadImageIcon(path);
    }

    public URL getUrl() {
        return ResourceUtils.getResource(path);
    }

    public String toString() {
        return description.getMessage();
    }
}
