package org.xblackcat.rojac.gui.component.factory;

import org.xblackcat.rojac.gui.theme.TextStyle;
import org.xblackcat.rojac.util.WindowsUtils;
import org.xblackcat.utils.ResourceUtils;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author xBlackCat
 */

public class TextStyleEditor extends AComplexEditor<TextStyle> {
    private final JCheckBox overrideFont = new JCheckBox("Override font");
    private final JCheckBox overrideForeground = new JCheckBox("Override foreground");
    private final JCheckBox overrideBackground = new JCheckBox("Override background");
    private final JButton font = new JButton();
    private final JButton foreground = new JButton();
    private final JButton background = new JButton();

    private Font selectedFont = null;
    private Color selectedForeground = null;
    private Color selectedBackground = null;

    private final JTextArea example = new JTextArea("Quick brown fox jumps over lazy dog\n0123456789");

    protected TextStyleEditor() {
        super(new BorderLayout(5, 5));

        JPanel defaultMarks = new JPanel(new GridLayout(0, 1));
        defaultMarks.add(overrideFont);
        defaultMarks.add(overrideForeground);
        defaultMarks.add(overrideBackground);

        trackEnabling(overrideFont, font);
        trackEnabling(overrideForeground, foreground);
        trackEnabling(overrideBackground, background);

        JPanel selectors = new JPanel(new GridLayout(0, 1));
        selectors.add(font);
        selectors.add(foreground);
        selectors.add(background);

        // TODO: add select font dialog

        foreground.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Color newColor = JColorChooser.showDialog(TextStyleEditor.this, "Select foreground", selectedForeground);

                if (newColor != null) {
                    foreground.setBackground(newColor);
                    selectedForeground = newColor;
                    updateExample();
                }
            }
        });

        background.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Color newColor = JColorChooser.showDialog(TextStyleEditor.this, "Select background", selectedBackground);

                if (newColor != null) {
                    background.setBackground(newColor);
                    selectedBackground = newColor;
                    updateExample();
                }
            }
        });

        JPanel buttonsPane = new JPanel(new GridLayout(0, 1));
        JButton confirm = new JButton(ResourceUtils.loadIcon("/images/icons/button-confirm.png"));
        JButton cancel = new JButton(ResourceUtils.loadIcon("/images/icons/button-cancel.png"));

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

        buttonsPane.add(confirm);
        buttonsPane.add(cancel);

        add(defaultMarks, BorderLayout.WEST);
        add(selectors, BorderLayout.CENTER);
        add(WindowsUtils.coverComponent(buttonsPane, FlowLayout.CENTER), BorderLayout.EAST);

        add(example, BorderLayout.SOUTH);
        Dimension exampleSize = new Dimension(200, 70);

        example.setPreferredSize(exampleSize);
        example.setMinimumSize(exampleSize);

        example.setLineWrap(true);
        example.setEditable(false);
        example.setAlignmentX(.5f);
        example.setAlignmentY(.5f);
    }

    @Override
    public TextStyle getValue() {
        Font f = null;
        Color fg = null;
        Color bg = null;

        if (overrideFont.isSelected()) {
            f = selectedFont;
        }

        if (overrideForeground.isSelected()) {
            fg = selectedForeground;
        }

        if (overrideBackground.isSelected()) {
            bg = selectedBackground;
        }

        return new TextStyle(f, fg, bg);
    }

    @Override
    public void setValue(TextStyle v) {
        if (v.getFont() == null) {
            overrideFont.setSelected(false);
            font.setEnabled(false);
        } else {
            overrideFont.setSelected(true);
            font.setEnabled(true);
            selectedFont = v.getFont();
            font.setText(v.getFont().toString());
        }

        if (v.getForeground() == null) {
            overrideForeground.setSelected(false);
            foreground.setEnabled(false);
        } else {
            overrideForeground.setSelected(true);
            foreground.setEnabled(true);

            selectedForeground = v.getForeground();
            foreground.setBackground(v.getForeground());
        }

        if (v.getBackground() == null) {
            overrideBackground.setSelected(false);
            background.setEnabled(false);
        } else {
            overrideBackground.setSelected(true);
            background.setEnabled(true);

            selectedBackground = v.getBackground();
            background.setBackground(v.getBackground());
        }

        updateExample();
    }

    private void updateExample() {
        TextStyle ts = getValue();

        example.setFont(ts.getFont());
        example.setForeground(ts.getForeground());
        example.setBackground(ts.getBackground());
    }

    private void trackEnabling(final JCheckBox master, final JButton slave) {
        class Tracker implements ChangeListener {
            @Override
            public void stateChanged(ChangeEvent e) {
                slave.setEnabled(master.isSelected());
                updateExample();
            }
        }

        master.addChangeListener(new Tracker());
    }
}
