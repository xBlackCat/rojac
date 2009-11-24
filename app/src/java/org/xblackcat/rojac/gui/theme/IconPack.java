package org.xblackcat.rojac.gui.theme;

import org.xblackcat.utils.ResourceUtils;

import javax.swing.*;
import java.util.MissingResourceException;

/**
 * @author xBlackCat
 */

public final class IconPack {
    private static final NoIcons NO_ICONS = new NoIcons();
    
    private final String extension;
    private final String name;
    private final String pathPrefix;

    public IconPack(String name, String pathPrefix, String extension) {
        this.name = name;
        this.pathPrefix = pathPrefix;
        this.extension = extension;
    }

    public String getName() {
        return name;
    }

    public String getExtension() {
        return extension;
    }

    public String getPathPrefix() {
        return pathPrefix;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof IconPack)) return false;

        IconPack iconPack = (IconPack) o;

        return extension.equals(iconPack.extension) &&
                name.equals(iconPack.name) &&
                pathPrefix.equals(iconPack.pathPrefix);
    }

    @Override
    public int hashCode() {
        int result = extension.hashCode();
        result = 31 * result + name.hashCode();
        result = 31 * result + pathPrefix.hashCode();
        return result;
    }

    public IButtonIcons getButtonIcons() {
        return NO_ICONS;
    }

    public IButtonIcons getImageButtonIcons(String buttonName) {
        return new IconsGetter(buttonName);
    }

    private final class IconsGetter implements IButtonIcons {
        private Icon defaultIcon;
        private Icon selectedIcon;
        private Icon rolloverIcon;
        private Icon disabledIcon;
        private Icon rolloverSelectedIcon;

        private IconsGetter(String buttonName) {
            String imageSet = pathPrefix + "/button/" + buttonName + '/';
            // Load icons for the button.
            // If default icon is not present a MissingResourceException will be thrown.
            defaultIcon = ResourceUtils.loadIcon(imageSet + "enabled." + extension);
            try {
                selectedIcon = ResourceUtils.loadIcon(imageSet + "pressed." + extension);
            } catch (MissingResourceException e) {
                // Use default icon
                selectedIcon = defaultIcon;
            }
            try {
                rolloverIcon = ResourceUtils.loadIcon(imageSet + "over." + extension);
            } catch (MissingResourceException e) {
                // Use default icon
                rolloverIcon = defaultIcon;
            }
            try {
                rolloverSelectedIcon = ResourceUtils.loadIcon(imageSet + "pressed-over." + extension);
            } catch (MissingResourceException e) {
                // Use default icon
                rolloverSelectedIcon = rolloverIcon;
            }
            try {
                disabledIcon = ResourceUtils.loadIcon(imageSet + "disabled." + extension);
            } catch (MissingResourceException e) {
                // Use default icon
                disabledIcon = defaultIcon;
            }
        }

        @Override
        public Icon getDefaultIcon() {
            return defaultIcon;
        }

        @Override
        public Icon getDisabledIcon() {
            return disabledIcon;
        }

        @Override
        public Icon getRolloverIcon() {
            return rolloverIcon;
        }

        @Override
        public Icon getRolloverSelectedIcon() {
            return rolloverSelectedIcon;
        }

        @Override
        public Icon getSelectedIcon() {
            return selectedIcon;
        }
    }

}
