package org.xblackcat.rojac.util;

import org.apache.commons.lang.ArrayUtils;
import org.xblackcat.rojac.gui.component.ShortCut;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Map;

/**
 * @author xBlackCat
 */

public class ShortCutUtils {
    public static final VKCollection vkCollect = new VKCollection();

    /**
     * Merge to InputMaps into a new InputMap with the rule: actions of <code>target</code> will be extended or replaced
     * with <code>base</code> actions. For example:
     * <p/>
     * Base: <code>['F1' -> 'help', 'F2' -> 'save']</code>; Target: <code>['F2' -> 'note', 'F3' -> 'open']</code> . As
     * result will be <code>['F1' -> 'help', 'F2' -> 'save', 'F3' -> 'open']</code>
     *
     * @param base   master input map.
     * @param target slave input map.
     *
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

        copy.setParent(target.getParent());

        return copy;
    }

    /**
     * Copy InputMap and add proxy to actions to target component.
     *
     * @param base
     * @param target
     *
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
        if (stroke.getKeyCode()  > 0) {
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

    private static class VKCollection {
        private Map<Integer, String> code2name;
        private Map<String, Integer> name2code;

        VKCollection() {
            code2name = new HashMap<Integer, String>();
            name2code = new HashMap<String, Integer>();
        }

        synchronized void put(String name, int code) {
            assert (name != null);
            assert (findName(code) == null);
            assert (findCode(name) == null);
            code2name.put(code, name);
            name2code.put(name, code);
        }

        synchronized Integer findCode(String name) {
            assert (name != null);
            return name2code.get(name);
        }

        synchronized String findName(int code) {
            return code2name.get(code);
        }
    }
}
