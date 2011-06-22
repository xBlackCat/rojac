package org.xblackcat.rojac.i18n;

import java.util.ResourceBundle;

/**
 * @author xBlackCat Date: 22.06.11
 */
public enum NodeText {
    Name("i18n/node_names"),
    Tip("i18n/node_hints");

    final String bundleName;

    private ResourceBundle bundle;

    NodeText(String bundle_name) {
        bundleName = bundle_name;
//        bundle = ResourceBundle.getBundle(bundleName);
    }

    public String get(ANode key) {
        return key.getKey();
    }
}
