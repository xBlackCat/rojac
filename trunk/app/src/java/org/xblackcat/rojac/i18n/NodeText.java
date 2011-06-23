package org.xblackcat.rojac.i18n;

/**
 * @author xBlackCat Date: 22.06.11
 */
public enum NodeText {
    Name("node_names"),
    Tip("node_hints");

    private final String bundleName;

    private NodeText(String bundle_name) {
        bundleName = bundle_name;
    }

    public String get(ANode key) {
        return LocaleControl.getInstance().getString(bundleName, key.getKey());
    }
}
