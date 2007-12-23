package org.xblackcat.sunaj.util;

import javax.swing.*;
import java.awt.*;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Date: 14/9/2006
 *
 * @author xBlackCat
 */

public class ImageUtils {
    private static final ImageUtils INSTANCE = new ImageUtils();

    // Icons cache. Map: "type" -> "icon". String -> Icon
    private Map iconsCache = Collections.synchronizedMap(new HashMap());

    private ImageUtils() {
    }

    public static Icon getIcon(String type) {
        ImageIcon ic = (ImageIcon) INSTANCE.iconsCache.get(type);
        if (ic == null) {
            ic = loadImageIcon(type);
            // Put to cache.
            if (ic != null) {
                INSTANCE.iconsCache.put(type, ic);
            } else {
                return null;
            }
        }
        return ic;
    }

    public static Image getImage(String type) {
        ImageIcon ic = (ImageIcon) INSTANCE.iconsCache.get(type);
        if (ic == null) {
            ic = loadImageIcon(type);
            // Put to cache.
            if (ic != null) {
                INSTANCE.iconsCache.put(type, ic);
            } else {
                return null;
            }
        }
        return ic.getImage();
    }

    public static ImageIcon loadImageIcon(String name) {
        ImageIcon ic = null;

        try {
            ic = new ImageIcon(ResourceUtils.getResource(name));
        } catch (Exception e) {
            System.out.println("Can not load resource: " + name);
            e.printStackTrace();
        }

        return ic;
    }

}
