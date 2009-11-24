package org.xblackcat.rojac.gui.view.thread;

import ch.lambdaj.Lambda;
import org.apache.commons.lang.StringUtils;
import org.xblackcat.rojac.data.Message;
import org.xblackcat.rojac.gui.component.JLightPanel;
import org.xblackcat.rojac.gui.component.LineRenderer;
import org.xblackcat.rojac.i18n.Messages;
import sun.swing.DefaultLookup;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.FontUIResource;
import javax.swing.plaf.basic.BasicGraphicsUtils;
import javax.swing.tree.TreeCellRenderer;
import java.awt.*;
import java.text.DateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Displays an entry in a tree. <code>DefaultTreeCellRenderer</code> is not opaque and unless you subclass paint you
 * should not change this. See <a href="http://java.sun.com/docs/books/tutorial/uiswing/components/tree.html">How to Use
 * Trees</a> in <em>The Java Tutorial</em> for examples of customizing node display using this class.
 * <p/>
 * <p/>
 * <strong><a name="override">Implementation Note:</a></strong> This class overrides <code>invalidate</code>,
 * <code>validate</code>, <code>revalidate</code>, <code>repaint</code>, and <code>firePropertyChange</code> solely to
 * improve performance. If not overridden, these frequently called methods would execute code paths that are unnecessary
 * for the default tree cell renderer. If you write your own renderer, take care to weigh the benefits and drawbacks of
 * overriding these methods.
 * <p/>
 * <p/>
 * <strong>Warning:</strong> Serialized objects of this class will not be compatible with future Swing releases. The
 * current serialization support is appropriate for short term storage or RMI between applications running the same
 * version of Swing.  As of 1.4, support for long term storage of all JavaBeans<sup><font size="-2">TM</font></sup> has
 * been added to the <code>java.beans</code> package. Please see {@link java.beans.XMLEncoder}.
 *
 * @author Rob Davis
 * @author Ray Ryan
 * @author Scott Violet
 * @version 1.62 02/04/08
 */
class MultiLineThreadItemRenderer extends JLightPanel implements TreeCellRenderer {
    private LineRenderer titleLine = new LineRenderer(JLabel.LEFT);
    private LineRenderer dateLine = new LineRenderer(JLabel.RIGHT);
    private LineRenderer userLine = new LineRenderer(JLabel.LEFT);
    private LineRenderer iconLine = new LineRenderer(JLabel.LEFT);
    private JPanel titlePane = new JLightPanel(new BorderLayout(0, 2));

    /**
     * Last tree the renderer was painted in.
     */
    private JTree tree;

    /**
     * Is the value currently selected.
     */
    protected boolean selected;
    /**
     * True if has focus.
     */
    protected boolean hasFocus;
    /**
     * True if draws focus border around icon as well.
     */
    private boolean drawsFocusBorderAroundIcon;
    /**
     * If true, a dashed line is drawn as the focus indicator.
     */
    private boolean drawDashedFocusIndicator;

    // If drawDashedFocusIndicator is true, the following are used.
    /**
     * Background color of the tree.
     */
    private Color treeBGColor;
    /**
     * Color to draw the focus indicator in, determined from the background. color.
     */
    private Color focusBGColor;

    // Icons
    /**
     * Icon used to show non-leaf nodes that aren't expanded.
     */
    transient protected Icon closedIcon;

    /**
     * Icon used to show leaf nodes.
     */
    transient protected Icon leafIcon;

    /**
     * Icon used to show non-leaf nodes that are expanded.
     */
    transient protected Icon openIcon;

    // Colors
    /**
     * Color to use for the foreground for selected nodes.
     */
    protected Color textSelectionColor;

    /**
     * Color to use for the foreground for non-selected nodes.
     */
    protected Color textNonSelectionColor;

    /**
     * Color to use for the background when a node is selected.
     */
    protected Color backgroundSelectionColor;

    /**
     * Color to use for the background when the node isn't selected.
     */
    protected Color backgroundNonSelectionColor;

    /**
     * Color to use for the focus indicator when the node has focus.
     */
    protected Color borderSelectionColor;

    private boolean isDropCell;
    private boolean fillBackground = true;
    private JComponent components;

    /**
     * Returns a new instance of DefaultTreeCellRenderer.  Alignment is set to left aligned. Icons and text color are
     * determined from the UIManager.
     */
    public MultiLineThreadItemRenderer() {
        super(new BorderLayout(2, 0));
        setOpaque(true);
        setLeafIcon(DefaultLookup.getIcon(this, ui, "Tree.leafIcon"));
        setClosedIcon(DefaultLookup.getIcon(this, ui, "Tree.closedIcon"));
        setOpenIcon(DefaultLookup.getIcon(this, ui, "Tree.openIcon"));

        setTextSelectionColor(DefaultLookup.getColor(this, ui, "Tree.selectionForeground"));
        setTextNonSelectionColor(DefaultLookup.getColor(this, ui, "Tree.textForeground"));
        setBackgroundSelectionColor(DefaultLookup.getColor(this, ui, "Tree.selectionBackground"));
        setBackgroundNonSelectionColor(DefaultLookup.getColor(this, ui, "Tree.textBackground"));
        setBorderSelectionColor(DefaultLookup.getColor(this, ui, "Tree.selectionBorderColor"));
        drawsFocusBorderAroundIcon = DefaultLookup.getBoolean(this, ui, "Tree.drawsFocusBorderAroundIcon", false);
        drawDashedFocusIndicator = DefaultLookup.getBoolean(this, ui, "Tree.drawDashedFocusIndicator", false);

        fillBackground = DefaultLookup.getBoolean(this, ui, "Tree.rendererFillBackground", true);
        Insets margins = DefaultLookup.getInsets(this, ui, "Tree.rendererMargins");
        if (margins == null) {
            margins = new Insets(0, 0, 0, 0);
        }
        setBorder(new MutableBorder(margins));

        add(iconLine, BorderLayout.WEST);
        add(titlePane, BorderLayout.CENTER);

        titlePane.add(titleLine, BorderLayout.NORTH);
        titlePane.add(userLine, BorderLayout.CENTER);
        titlePane.add(dateLine, BorderLayout.EAST);

        titlePane.setOpaque(true);

        List<? extends JComponent> list = Arrays.asList(titleLine, userLine, dateLine, iconLine, titlePane);
        components = Lambda.forEach(list);
        setName("Tree.cellRenderer");
    }


    /**
     * Returns the default icon, for the current laf, that is used to represent non-leaf nodes that are expanded.
     */
    public Icon getDefaultOpenIcon() {
        return DefaultLookup.getIcon(this, ui, "Tree.openIcon");
    }

    /**
     * Returns the default icon, for the current laf, that is used to represent non-leaf nodes that are not expanded.
     */
    public Icon getDefaultClosedIcon() {
        return DefaultLookup.getIcon(this, ui, "Tree.closedIcon");
    }

    /**
     * Returns the default icon, for the current laf, that is used to represent leaf nodes.
     */
    public Icon getDefaultLeafIcon() {
        return DefaultLookup.getIcon(this, ui, "Tree.leafIcon");
    }

    /**
     * Sets the icon used to represent non-leaf nodes that are expanded.
     */
    public void setOpenIcon(Icon newIcon) {
        openIcon = newIcon;
    }

    /**
     * Returns the icon used to represent non-leaf nodes that are expanded.
     */
    public Icon getOpenIcon() {
        return openIcon;
    }

    /**
     * Sets the icon used to represent non-leaf nodes that are not expanded.
     */
    public void setClosedIcon(Icon newIcon) {
        closedIcon = newIcon;
    }

    /**
     * Returns the icon used to represent non-leaf nodes that are not expanded.
     */
    public Icon getClosedIcon() {
        return closedIcon;
    }

    /**
     * Sets the icon used to represent leaf nodes.
     */
    public void setLeafIcon(Icon newIcon) {
        leafIcon = newIcon;
    }

    /**
     * Returns the icon used to represent leaf nodes.
     */
    public Icon getLeafIcon() {
        return leafIcon;
    }

    /**
     * Sets the color the text is drawn with when the node is selected.
     */
    public void setTextSelectionColor(Color newColor) {
        textSelectionColor = newColor;
    }

    /**
     * Returns the color the text is drawn with when the node is selected.
     */
    public Color getTextSelectionColor() {
        return textSelectionColor;
    }

    /**
     * Sets the color the text is drawn with when the node isn't selected.
     */
    public void setTextNonSelectionColor(Color newColor) {
        textNonSelectionColor = newColor;
    }

    /**
     * Returns the color the text is drawn with when the node isn't selected.
     */
    public Color getTextNonSelectionColor() {
        return textNonSelectionColor;
    }

    /**
     * Sets the color to use for the background if node is selected.
     */
    public void setBackgroundSelectionColor(Color newColor) {
        backgroundSelectionColor = newColor;
    }


    /**
     * Returns the color to use for the background if node is selected.
     */
    public Color getBackgroundSelectionColor() {
        return backgroundSelectionColor;
    }

    /**
     * Sets the background color to be used for non selected nodes.
     */
    public void setBackgroundNonSelectionColor(Color newColor) {
        backgroundNonSelectionColor = newColor;
    }

    /**
     * Returns the background color to be used for non selected nodes.
     */
    public Color getBackgroundNonSelectionColor() {
        return backgroundNonSelectionColor;
    }

    /**
     * Sets the color to use for the border.
     */
    public void setBorderSelectionColor(Color newColor) {
        borderSelectionColor = newColor;
    }

    /**
     * Returns the color the border is drawn.
     */
    public Color getBorderSelectionColor() {
        return borderSelectionColor;
    }

    /**
     * Subclassed to map <code>FontUIResource</code>s to null. If <code>font</code> is null, or a
     * <code>FontUIResource</code>, this has the effect of letting the font of the JTree show through. On the other
     * hand, if <code>font</code> is non-null, and not a <code>FontUIResource</code>, the font becomes
     * <code>font</code>.
     */
    public void setFont(Font font) {
        if (font instanceof FontUIResource) {
            font = null;
        }
        super.setFont(font);
    }

    /**
     * Gets the font of this component.
     *
     * @return this component's font; if a font has not been set for this component, the font of its parent is returned
     */
    public Font getFont() {
        Font font = super.getFont();

        if (font == null && tree != null) {
            // Strive to return a non-null value, otherwise the html support
            // will typically pick up the wrong font in certain situations.
            font = tree.getFont();
        }
        return font;
    }

    /**
     * Subclassed to map <code>ColorUIResource</code>s to null. If <code>color</code> is null, or a
     * <code>ColorUIResource</code>, this has the effect of letting the background color of the JTree show through. On
     * the other hand, if <code>color</code> is non-null, and not a <code>ColorUIResource</code>, the background becomes
     * <code>color</code>.
     */
    public void setBackground(Color color) {
        if (color instanceof ColorUIResource) {
            color = null;
        }
        super.setBackground(color);
    }

    /**
     * Configures the renderer based on the passed in components. The value is set from messaging the tree with
     * <code>convertValueToText</code>, which ultimately invokes <code>toString</code> on <code>value</code>. The
     * foreground color is set based on the selection and the icon is set based on the <code>leaf</code> and
     * <code>expanded</code> parameters.
     */
    public Component getTreeCellRendererComponent(JTree tree, Object value,
                                                  boolean sel,
                                                  boolean expanded,
                                                  boolean leaf, int row,
                                                  boolean hasFocus) {
        this.tree = tree;
        this.hasFocus = hasFocus;
        this.selected = sel;

        Color fg = null;
        isDropCell = false;

        JTree.DropLocation dropLocation = tree.getDropLocation();
        if (dropLocation != null
                && dropLocation.getChildIndex() == -1
                && tree.getRowForPath(dropLocation.getPath()) == row) {

            Color col = DefaultLookup.getColor(this, ui, "Tree.dropCellForeground");
            if (col != null) {
                fg = col;
            } else {
                fg = getTextSelectionColor();
            }

            isDropCell = true;
        } else if (sel) {
            fg = getTextSelectionColor();
        } else {
            fg = getTextNonSelectionColor();
        }

        setForeground(fg);
        components.setForeground(fg);

        Icon icon = null;
        if (leaf) {
            icon = getLeafIcon();
        } else if (expanded) {
            icon = getOpenIcon();
        } else {
            icon = getClosedIcon();
        }

        if (!tree.isEnabled()) {
            setEnabled(false);
            components.setEnabled(false);
            LookAndFeel laf = UIManager.getLookAndFeel();
            Icon disabledIcon = laf.getDisabledIcon(tree, icon);
            if (disabledIcon != null) icon = disabledIcon;
            iconLine.setDisabledIcon(icon);
        } else {
            setEnabled(true);
            components.setEnabled(true);
            iconLine.setIcon(icon);
        }
        setComponentOrientation(tree.getComponentOrientation());

        Color bColor;
        if (isDropCell) {
            bColor = DefaultLookup.getColor(this, ui, "Tree.dropCellBackground");
            if (bColor == null) {
                bColor = getBackgroundSelectionColor();
            }
        } else if (sel) {
            bColor = getBackgroundSelectionColor();
        } else {
            bColor = getBackgroundNonSelectionColor();
            if (bColor == null) {
                bColor = getBackground();
            }
        }


        setBackground(bColor);
        components.setBackground(bColor);
        // Setup component with value-based styles

        if (value != null) {
            Message mi = ((MessageItem) value).getMessage((ThreadsModel) tree.getModel());

            dateLine.setText(null);
            userLine.setText(null);

            if (mi != null) {
                titleLine.setText(mi.getSubject());

                long forumDate = mi.getMessageDate();
                if (forumDate > 0) {
                    DateFormat dateFormat = DateFormat.getDateTimeInstance(
                            DateFormat.MEDIUM,
                            DateFormat.SHORT,
                            Messages.getLocale());
                    dateLine.setText(dateFormat.format(new Date(forumDate)));
                }

                if (StringUtils.isEmpty(mi.getUserNick())) {
                    userLine.setText(String.valueOf(mi.getUserId()));
                } else {
                    userLine.setText(mi.getUserNick());
                }

                int style = Font.PLAIN;
                if (!mi.isRead()) style |= Font.BOLD;

                Font font = tree.getFont().deriveFont(style);
                setFont(font);
                components.setFont(font);

                if (sel) {
                    tree.setToolTipText(String.valueOf(mi.getMessageId()));
                }
            } else {
                titleLine.setText("Loading...");
                Font font = tree.getFont().deriveFont(Font.ITALIC);
                setFont(font);
                components.setFont(font);
            }
            titlePane.validate();
            titlePane.invalidate();
        }

        return this;
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
    }

    /**
     * Overrides <code>JComponent.getPreferredSize</code> to return slightly wider preferred size value.
     */
    public Dimension getPreferredSize() {
        Dimension retDimension = super.getPreferredSize();

        if (retDimension != null) {
            retDimension = new Dimension(retDimension.width + 3,
                    retDimension.height);
        }
        return retDimension;
    }

    private class MutableBorder extends EmptyBorder {
        public MutableBorder(Insets margins) {
            super(margins.top + 1, margins.left + 1, margins.bottom + 1, margins.right + 1);
        }

        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            if (!selected) {
                return;
            }
            Color bsColor = getBorderSelectionColor();
            Color notColor = getBackgroundSelectionColor();

            if (bsColor != null && (selected || !drawDashedFocusIndicator)) {
                g.setColor(bsColor);
                g.drawRect(x, y, width - 1, height - 1);
            }
            if (drawDashedFocusIndicator && notColor != null) {
                if (treeBGColor != notColor) {
                    treeBGColor = notColor;
                    focusBGColor = new Color(~notColor.getRGB());
                }
                g.setColor(focusBGColor);
                BasicGraphicsUtils.drawDashedRect(g, x, y, width, height);
            }
        }
    }

}