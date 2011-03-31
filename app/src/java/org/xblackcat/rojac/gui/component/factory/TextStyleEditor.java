package org.xblackcat.rojac.gui.component.factory;

import org.xblackcat.rojac.gui.theme.TextStyle;
import org.xblackcat.rojac.util.WindowsUtils;
import org.xblackcat.utils.ResourceUtils;

import javax.swing.*;
import javax.swing.border.LineBorder;
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
    private final JButton selectFontButton = new JButton("...");
    private final JButton selectForegroundButton = new JButton("...");
    private final JButton selectBackgroundButton = new JButton("...");

    private final JComboBox fontSelector;
    private final JComboBox fontSizeSelector;
    private final JToggleButton fontBold = new JToggleButton("B");
    private final JToggleButton fontItalic = new JToggleButton("I");

    private final JPanel previewForeground = new JPanel();
    private final JPanel previewBackground = new JPanel();

    private Font selectedFont = null;
    private Color selectedForeground = null;
    private Color selectedBackground = null;

    private final JTextArea example = new JTextArea("The quick brown fox jumps the over lazy dog\n0123456789");

    protected TextStyleEditor() {
        super(new BorderLayout(5, 5));

        fontSelector = new JComboBox();
        fontSizeSelector = new JComboBox();

        fontBold.setFont(fontBold.getFont().deriveFont(Font.BOLD));
        fontItalic.setFont(fontItalic.getFont().deriveFont(Font.ITALIC));

        // Initialize layout
        JPanel defaultMarks = WindowsUtils.createColumn(overrideFont, overrideForeground, overrideBackground);

        trackEnabling(overrideFont, selectFontButton);
        trackEnabling(overrideForeground, selectForegroundButton);
        trackEnabling(overrideBackground, selectBackgroundButton);


        JPanel selectors = WindowsUtils.createColumn(
                createFontSelector(),
                createColorPreview(previewForeground, selectForegroundButton),
                createColorPreview(previewBackground, selectBackgroundButton)
        );

        selectForegroundButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Color newColor = JColorChooser.showDialog(TextStyleEditor.this, "Select foreground", selectedForeground);

                if (newColor != null) {
                    selectForegroundButton.setBackground(newColor);
                    selectedForeground = newColor;
                    updateExample();
                }
            }
        });

        selectBackgroundButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Color newColor = JColorChooser.showDialog(TextStyleEditor.this, "Select background", selectedBackground);

                if (newColor != null) {
                    selectBackgroundButton.setBackground(newColor);
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
            selectFontButton.setEnabled(false);
        } else {
            overrideFont.setSelected(true);
            selectFontButton.setEnabled(true);
            selectedFont = v.getFont();
            selectFontButton.setText(v.getFont().toString());
        }

        if (v.getForeground() == null) {
            overrideForeground.setSelected(false);
            selectForegroundButton.setEnabled(false);
        } else {
            overrideForeground.setSelected(true);
            selectForegroundButton.setEnabled(true);

            selectedForeground = v.getForeground();
            selectForegroundButton.setBackground(v.getForeground());
        }

        if (v.getBackground() == null) {
            overrideBackground.setSelected(false);
            selectBackgroundButton.setEnabled(false);
        } else {
            overrideBackground.setSelected(true);
            selectBackgroundButton.setEnabled(true);

            selectedBackground = v.getBackground();
            selectBackgroundButton.setBackground(v.getBackground());
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

    private static JPanel createColorPreview(JPanel previewPane, JButton selectorButton) {
        JPanel colorSelector = new JPanel(new BorderLayout(5, 5));

        colorSelector.add(previewPane, BorderLayout.CENTER);
        colorSelector.add(selectorButton, BorderLayout.EAST);

        previewPane.setBorder(new LineBorder(Color.black, 1));

        return colorSelector;
    }

    private JPanel createFontSelector() {
        JPanel fontSelector = new JPanel(new BorderLayout(5, 5));

        fontSelector.add(new JLabel("Here will be font"));

        return fontSelector;
    }
}
