package org.xblackcat.rojac.data;

import org.xblackcat.rojac.i18n.Message;
import org.xblackcat.utils.ResourceUtils;

import javax.swing.*;

/**
 * @author xBlackCat
 */

public enum Mark implements IDictionary {
    PlusOne(-3, Message.Description_Mark_PlusOne, "plus_one.gif"),
    Agree(-4, Message.Description_Mark_Agree, "plus.gif"),
    Disagree(0, Message.Description_Mark_Disagree, "minus.gif"),
    x1(1, Message.Description_Mark_X1, "x1.gif"),
    x2(2, Message.Description_Mark_X2, "x2.gif"),
    x3(3, Message.Description_Mark_X3, "x3.gif"),
    Smile(-2, Message.Description_Mark_Smile, "smile.gif"),
    Remove(-1, Message.Description_Mark_Remove, "smile.gif");

    private static final String IMAGES_MARKS_PATH = "marks/";

    public static Mark getMark(int code) throws IllegalArgumentException {
        for (Mark m : values()) {
            if (m.getId() == code) {
                return m;
            }
        }

        if (code < -4) {
            return Disagree;
        }
        throw new IllegalArgumentException("Unknown mark type: " + code);
    }

    private final int value;
    private final Message description;
    private final ImageIcon markIcon;

    private Mark(int value, Message description, String imgName) {
        this.value = value;
        this.description = description;
        markIcon = ResourceUtils.loadImageIcon("/images/" + IMAGES_MARKS_PATH + imgName);
    }

    public int getId() {
        return value;
    }

    public ImageIcon getIcon() {
        return markIcon;
    }

    public String toString() {
        return description.get();
    }
}
