package org.xblackcat.rojac.gui.dialog.options;

import org.jdesktop.swingx.combobox.ListComboBoxModel;
import org.xblackcat.rojac.gui.component.AComplexEditor;
import org.xblackcat.rojac.gui.theme.TextStyle;
import org.xblackcat.rojac.util.WindowsUtils;
import org.xblackcat.utils.ResourceUtils;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.UIResource;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.NumberFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

/**
 * @author xBlackCat
 */

class TextStyleEditor extends AComplexEditor<TextStyle> {
    private final JCheckBox overrideFont = new JCheckBox("Override font");
    private final JCheckBox overrideForeground = new JCheckBox("Override foreground");
    private final JCheckBox overrideBackground = new JCheckBox("Override background");
    private final JButton selectForegroundButton = new JButton("...");
    private final JButton selectBackgroundButton = new JButton("...");

    private final JComboBox<String> fontSelector;
    private final JComboBox<Integer> fontSizeSelector;
    private final JToggleButton fontBold = new JToggleButton("B");
    private final JToggleButton fontItalic = new JToggleButton("I");

    private final JPanel previewForeground = new JPanel();
    private final JPanel previewBackground = new JPanel();

    private Font selectedFont = null;
    private Color selectedForeground = null;
    private Color selectedBackground = null;

    private final JTextArea example = new JTextArea("The quick brown fox jumps the over lazy dog\n0123456789");
    private final ListComboBoxModel<String> fontModel;
    private final ListComboBoxModel<Integer> fontSizeModel;

    protected TextStyleEditor() {
        super(new BorderLayout(5, 5));

        GraphicsEnvironment environment = GraphicsEnvironment.getLocalGraphicsEnvironment();
        String[] fontNames = environment.getAvailableFontFamilyNames(Locale.ROOT);

        fontModel = new ListComboBoxModel<>(Arrays.asList(fontNames));

        List<Integer> sizes = Arrays.asList(6, 7, 8, 9, 10, 12, 14, 16, 18, 22, 26, 32, 48, 72);
        fontSizeModel = new ListComboBoxModel<>(sizes);

        fontSelector = new JComboBox<>(fontModel);
        fontSizeSelector = new JComboBox<>(fontSizeModel);

        fontBold.setFont(fontBold.getFont().deriveFont(Font.BOLD));
        fontItalic.setFont(fontItalic.getFont().deriveFont(Font.ITALIC));

        // Initialize layout
        JPanel defaultMarks = WindowsUtils.createColumn(overrideFont, overrideForeground, overrideBackground);

        overrideFont.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                boolean enable = overrideFont.isSelected();
                fontSelector.setEnabled(enable);
                fontSizeSelector.setEnabled(enable);
                fontBold.setEnabled(enable);
                fontItalic.setEnabled(enable);
                updateExample();
            }
        });
        overrideForeground.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                selectForegroundButton.setEnabled(overrideForeground.isSelected());
                updateExample();
            }
        });
        overrideBackground.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                selectBackgroundButton.setEnabled(overrideBackground.isSelected());
                updateExample();
            }
        });


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
                    previewForeground.setBackground(newColor);
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
                    previewBackground.setBackground(newColor);
                    selectedBackground = newColor;
                    updateExample();
                }
            }
        });

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

        JPanel buttonsPane = WindowsUtils.createColumn(
                confirm,
                cancel
        );

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
    public void setValue(TextStyle stroke) {
        if (stroke.getFont() == null) {
            overrideFont.setSelected(false);
            fontSelector.setEnabled(false);
            fontSizeSelector.setEnabled(false);
            fontBold.setEnabled(false);
            fontItalic.setEnabled(false);
        } else {
            overrideFont.setSelected(true);
            fontSelector.setEnabled(true);
            fontSizeSelector.setEnabled(true);
            fontBold.setEnabled(true);
            fontItalic.setEnabled(true);
            selectedFont = stroke.getFont();
            fontSelector.setSelectedItem(selectedFont.getFamily(Locale.ROOT));
            fontItalic.setSelected(selectedFont.isItalic());
            fontBold.setSelected(selectedFont.isBold());
            fontSizeSelector.setSelectedItem(selectedFont.getSize());
        }

        if (stroke.getForeground() == null) {
            overrideForeground.setSelected(false);
            selectForegroundButton.setEnabled(false);
        } else {
            overrideForeground.setSelected(true);
            selectForegroundButton.setEnabled(true);

            selectedForeground = stroke.getForeground();
            selectForegroundButton.setBackground(stroke.getForeground());
        }

        if (stroke.getBackground() == null) {
            overrideBackground.setSelected(false);
            selectBackgroundButton.setEnabled(false);
        } else {
            overrideBackground.setSelected(true);
            selectBackgroundButton.setEnabled(true);

            selectedBackground = stroke.getBackground();
            selectBackgroundButton.setBackground(stroke.getBackground());
        }

        updateExample();
    }

    private void updateExample() {
        TextStyle ts = getValue();

        example.setFont(ts.getFont());
        example.setForeground(ts.getForeground());
        example.setBackground(ts.getBackground());
    }

    private static JPanel createColorPreview(JPanel previewPane, JButton selectorButton) {
        JPanel colorSelector = new JPanel(new BorderLayout(5, 5));

        colorSelector.add(previewPane, BorderLayout.CENTER);
        colorSelector.add(selectorButton, BorderLayout.EAST);

        previewPane.setBorder(new LineBorder(Color.black, 1));

        return colorSelector;
    }

    private JPanel createFontSelector() {
        JPanel fontSelectorPane = new JPanel(new BorderLayout());
        fontSelectorPane.add(fontSelector, BorderLayout.WEST);
        fontSelectorPane.add(fontSizeSelector, BorderLayout.CENTER);
        fontSelectorPane.add(WindowsUtils.createRow(fontBold, fontItalic), BorderLayout.EAST);

        fontSizeSelector.setEditable(true);
        fontSizeSelector.setEditor(new FontSizeEditor());

        ActionListener fontChanged = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fontChanged();
            }
        };
        fontSelector.addActionListener(fontChanged);
        fontSizeSelector.addActionListener(fontChanged);
        fontBold.addActionListener(fontChanged);
        fontItalic.addActionListener(fontChanged);

        return fontSelectorPane;
    }

    private void fontChanged() {
        int style = Font.PLAIN;
        if (fontBold.isSelected()) {
            style |= Font.BOLD;
        }
        if (fontItalic.isSelected()) {
            style |= Font.ITALIC;
        }

        Integer size = fontSizeModel.getSelectedItem();

        int plainSize = size == null ? 10 : size;

        String fontName = fontModel.getSelectedItem();

        if (fontName != null) {
            selectedFont = new Font(fontName, style, plainSize);
            updateExample();
        }
    }

    private class FontSizeEditor implements ComboBoxEditor {
        private final JTextField editor;

        private FontSizeEditor() {
            NumberFormat instance = NumberFormat.getIntegerInstance(Locale.ROOT);
            editor = new JFormattedTextField(instance) {
                public void setBorder(Border b) {
                    if (!(b instanceof UIResource)) {
                        super.setBorder(b);
                    }
                }
            };
            editor.setBorder(new EmptyBorder(0, 3, 0, 3));
        }

        @Override
        public Component getEditorComponent() {
            return editor;
        }

        @Override
        public void setItem(Object anObject) {
            editor.setText(String.valueOf(anObject));
        }

        @Override
        public Object getItem() {
            return Integer.valueOf(editor.getText());
        }

        @Override
        public void selectAll() {
            editor.selectAll();
            editor.requestFocus();
        }

        public void addActionListener(ActionListener l) {
            editor.addActionListener(l);
        }

        public void removeActionListener(ActionListener l) {
            editor.removeActionListener(l);
        }
    }
}
