package org.xblackcat.rojac.util;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xblackcat.rojac.gui.component.ShortCut;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.*;
import java.util.Properties;

/**
 * @author xBlackCat
 */

public class ShortCutUtils {
    private static final Log log = LogFactory.getLog(ShortCutUtils.class);

    /**
     * Merge to InputMaps into a new InputMap with the rule: actions of <code>target</code> will be extended or replaced
     * with <code>base</code> actions. For example:
     * <p/>
     * Base: <code>['F1' -> 'help', 'F2' -> 'save']</code>; Target: <code>['F2' -> 'note', 'F3' -> 'open']</code> . As
     * result will be <code>['F1' -> 'help', 'F2' -> 'save', 'F3' -> 'open']</code>
     *
     * @param base   master input map.
     * @param target slave input map.
     * @return a new merged input map
     */
    public static InputMap mergeInputMaps(InputMap base, InputMap target) {
        InputMap copy = new InputMap();
        if (target != null) {
            KeyStroke[] keys = target.keys();
            if (keys != null) {
                for (KeyStroke k : keys) {
                    copy.put(k, target.get(k));
                }
            }
        }

        if (base != null) {
            KeyStroke[] keys = base.keys();
            if (keys != null) {
                for (KeyStroke k : keys) {
                    copy.put(k, base.get(k));
                }
            }
        }

        if (target != null) {
            copy.setParent(target.getParent());
        }

        return copy;
    }

    /**
     * Copy InputMap and add proxy to actions to target component.
     *
     * @param holder
     * @param target
     * @return
     */
    public static void mergeInputMaps(JComponent holder, final JComponent target) {
        InputMap copy = holder.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

        InputMap targetInputMap = target.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        if (targetInputMap != null) {
            KeyStroke[] keys = targetInputMap.keys();
            if (keys != null) {
                for (KeyStroke k : keys) {
                    Object actionKey = targetInputMap.get(k);

                    copy.put(k, actionKey);
                    holder.getActionMap().put(actionKey, target.getActionMap().get(actionKey));
                }
            }
        }
    }

    public static String keyStrokeHint(KeyStroke stroke) {
        StringBuilder str = new StringBuilder();
        if (stroke.getModifiers() > 0) {
            str.append(KeyEvent.getModifiersExText(stroke.getModifiers()));
            str.append("+");
        }
        if (stroke.getKeyCode() > 0) {
            str.append(KeyEvent.getKeyText(stroke.getKeyCode()));
        }

        return str.toString();
    }

    public static void updateShortCuts(Component component) {
        if (component instanceof JComponent) {
            // Update action maps
            updateInputMap(((JComponent) component).getInputMap(JComponent.WHEN_FOCUSED));
            updateInputMap(((JComponent) component).getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT));
            updateInputMap(((JComponent) component).getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW));
        }

        if (component instanceof Container) {
            for (Component c : ((Container) component).getComponents()) {
                updateShortCuts(c);
            }
        }
    }

    private static void updateInputMap(InputMap map) {
        if (map == null) {
            return;
        }

        KeyStroke[] keys = map.keys();
        if (ArrayUtils.isEmpty(keys)) {
            return;
        }

        for (KeyStroke key : keys) {
            Object action = map.get(key);

            if (action instanceof ShortCut) {
                ShortCut sc = (ShortCut) action;
                if (sc.getKeyStroke() != key) {
                    // Shortcut was changed.
                    map.remove(key);
                    map.put(sc.getKeyStroke(), sc);
                }
            }
        }

        updateInputMap(map.getParent());
    }

    public static void registerShortCuts(JComponent component) {
        final InputMap inputMap = component.getInputMap();
        for (ShortCut sc : ShortCut.values()) {
            inputMap.put(sc.getKeyStroke(), sc);
        }
    }

    public static void loadShortCuts() {
        File keyMapFile = RojacUtils.getKeyMapFile();
        if (!keyMapFile.exists()) {
            if (log.isDebugEnabled()) {
                log.debug("Default keymap is used.");
            }
            return;
        }

        Properties keyBinding = new Properties();
        try {
            try (BufferedInputStream is = new BufferedInputStream(new FileInputStream(keyMapFile))) {
                keyBinding.load(is);
            }

        } catch (IOException e) {
            log.error("Can not load stored keymap", e);
            return;
        }

        for (String action : keyBinding.stringPropertyNames()) {
            String keys = keyBinding.getProperty(action);

            if (log.isTraceEnabled()) {
                log.trace("Loading shortcut for " + action + ": [" + keys + "]");
            }

            try {
                KeyStroke keyStroke = KeyStroke.getKeyStroke(keys);

                if (keyStroke == null) {
                    throw new IllegalArgumentException("Invalid keystroke: " + keys);
                }

                ShortCut.valueOf(action).setKeyStroke(keyStroke);
            } catch (IllegalArgumentException e) {
                log.warn("Invalid action: " + action + "/" + keys, e);
            }
        }
    }

    public static void storeShortCuts() {
        Properties keyBinding = new Properties();

        for (ShortCut sc : ShortCut.values()) {
            if (sc.isCustom()) {
                keyBinding.setProperty(sc.name(), sc.getKeyStroke().toString());
            }
        }

        if (keyBinding.isEmpty()) {
            return;
        }

        File keyMapFile = RojacUtils.getKeyMapFile();
        
        try {
            try (BufferedOutputStream os = new BufferedOutputStream(new FileOutputStream(keyMapFile))) {
                keyBinding.store(os, "Rojac key binding config file.");
            }

        } catch (IOException e) {
            log.error("Can not store user keymap", e);
            return;
        }
    }
}
