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

    public IconPack(String name, String pathPrefix) {
        this.name = name;
        this.pathPrefix = pathPrefix;
        extension = "png";
    }

    public String getName() {
        return name;
    }

    public IButtonIcons getButtonIcons() {
        return NO_ICONS;
    }

    public IButtonIcons getImageButtonIcons(String buttonName) {
        return new IconsGetter(buttonName);
    }

    private class IconsGetter implements IButtonIcons {
        protected Icon defaultIcon;
        protected Icon selectedIcon;
        protected Icon rolloverIcon;
        protected Icon disabledIcon;

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
            return selectedIcon;
        }

        @Override
        public Icon getSelectedIcon() {
            return selectedIcon;
        }
    }

    private static class NoIcons implements IButtonIcons {
        @Override
        public Icon getDefaultIcon() {
            return null;
        }

        @Override
        public Icon getDisabledIcon() {
            return null;
        }

        @Override
        public Icon getRolloverIcon() {
            return null;
        }

        @Override
        public Icon getRolloverSelectedIcon() {
            return null;
        }

        @Override
        public Icon getSelectedIcon() {
            return null;
        }
    }
}
