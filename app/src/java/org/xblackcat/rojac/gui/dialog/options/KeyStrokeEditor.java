package org.xblackcat.rojac.gui.dialog.options;

import org.xblackcat.rojac.gui.component.AComplexEditor;
import org.xblackcat.rojac.gui.theme.OptionsIcon;
import org.xblackcat.rojac.util.ShortCutUtils;
import org.xblackcat.rojac.util.WindowsUtils;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;

/**
 * Created by IntelliJ IDEA. User: Alexey Date: 12.05.11 Time: 14:04 To change this template use File | Settings | File
 * Templates.
 */
class KeyStrokeEditor extends AComplexEditor<KeyStroke> {
    private KeyStroke stroke;

    private final JTextField description = new JTextField();

    KeyStrokeEditor() {
        super(new BorderLayout());

        JButton confirm = new JButton(OptionsIcon.Confirm);
        JButton cancel = new JButton(OptionsIcon.Cancel);

        confirm.setBorder(null);
        cancel.setBorder(null);
        setBorder(new EmptyBorder(0, 5, 0, 5));

        confirm.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fireEditDone();
            }
        });

        cancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fireEditCancel();
            }
        });

        JPanel buttonsPane = WindowsUtils.createRow(
                confirm,
                cancel
        );

        description.setEditable(false);
        description.setBorder(null);
        description.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                int keyCode = e.getKeyCode();
                if (keyCode == KeyEvent.VK_ALT ||
                        keyCode == KeyEvent.VK_ALT_GRAPH ||
                        keyCode == KeyEvent.VK_CONTROL ||
                        keyCode == KeyEvent.VK_SHIFT) {
                    keyCode = 0;
                }

                KeyStroke pressedKeyStroke = KeyStroke.getKeyStroke(keyCode, e.getModifiers());

                if (keyCode > 0) {
                    stroke = pressedKeyStroke;
                }

                updateStroke(pressedKeyStroke);
            }

            @Override
            public void keyReleased(KeyEvent e) {
                updateStroke(stroke);
            }
        });
        add(description, BorderLayout.CENTER);
        add(WindowsUtils.coverComponent(buttonsPane, FlowLayout.CENTER), BorderLayout.EAST);

        addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                description.requestFocus();
            }

            @Override
            public void focusLost(FocusEvent e) {
            }
        });
    }

    @Override
    public KeyStroke getValue() {
        return stroke;
    }

    @Override
    public void setValue(KeyStroke stroke) {
        this.stroke = stroke;

        updateStroke(stroke);
    }

    private void updateStroke(KeyStroke stroke) {
        if (stroke == null) {
            description.setText("");
        }

        description.setText(ShortCutUtils.keyStrokeHint(stroke));
    }
}
