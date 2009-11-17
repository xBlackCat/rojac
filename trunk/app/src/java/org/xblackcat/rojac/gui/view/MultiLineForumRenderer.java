package org.xblackcat.rojac.gui.view;

import ch.lambdaj.Lambda;
import org.xblackcat.rojac.data.Forum;
import org.xblackcat.rojac.data.ForumStatistic;
import org.xblackcat.rojac.service.options.Property;
import sun.swing.DefaultLookup;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.io.Serializable;
import java.text.DateFormat;
import java.util.Arrays;
import java.util.Date;

class MultiLineForumRenderer extends JPanel
        implements TableCellRenderer, Serializable {

    /**
     * An empty <code>Border</code>. This field might not be used. To change the <code>Border</code> used by this
     * renderer override the <code>getTableCellRendererComponent</code> method and set the border of the returned
     * component directly.
     */
    private static final Border SAFE_NO_FOCUS_BORDER = new EmptyBorder(1, 1, 1, 1);
    private static final Border DEFAULT_NO_FOCUS_BORDER = new EmptyBorder(1, 1, 1, 1);
    protected static Border noFocusBorder = DEFAULT_NO_FOCUS_BORDER;

    // We need a place to store the color the JLabel should be returned
    // to after its foreground and background colors have been set
    // to the selection background color.
    // These ivars will be made protected when their names are finalized.
    private Color unselectedForeground;
    private Color unselectedBackground;

    private LineRenderer titleLine = new LineRenderer(JLabel.LEFT);
    private LineRenderer dateLine = new LineRenderer(JLabel.RIGHT);
    private LineRenderer statLine = new LineRenderer(JLabel.RIGHT);

    protected final JComponent components;

    /**
     * Creates a default table cell renderer.
     */
    public MultiLineForumRenderer() {
        super(new BorderLayout(0, 0));
        setOpaque(true);
        setBorder(getNoFocusBorder());
        setName("Table.cellRenderer");

        add(titleLine, BorderLayout.NORTH);
        add(statLine, BorderLayout.EAST);
        add(dateLine, BorderLayout.WEST);

        components = Lambda.forEach(Arrays.asList(titleLine, statLine, dateLine));
    }

    private Border getNoFocusBorder() {
        Border border = DefaultLookup.getBorder(this, ui, "Table.cellNoFocusBorder");
        if (System.getSecurityManager() != null) {
            if (border != null) return border;
            return SAFE_NO_FOCUS_BORDER;
        } else if (border != null) {
            if (noFocusBorder == null || noFocusBorder == DEFAULT_NO_FOCUS_BORDER) {
                return border;
            }
        }
        return noFocusBorder;
    }

    /**
     * Overrides <code>JComponent.setForeground</code> to assign the unselected-foreground color to the specified
     * color.
     *
     * @param c set the foreground color to this value
     */
    public void setForeground(Color c) {
        super.setForeground(c);
        unselectedForeground = c;
    }

    /**
     * Overrides <code>JComponent.setBackground</code> to assign the unselected-background color to the specified
     * color.
     *
     * @param c set the background color to this value
     */
    public void setBackground(Color c) {
        super.setBackground(c);
        unselectedBackground = c;
    }

    /**
     * Notification from the <code>UIManager</code> that the look and feel [L&F] has changed. Replaces the current UI
     * object with the latest version from the <code>UIManager</code>.
     *
     * @see javax.swing.JComponent#updateUI
     */
    public void updateUI() {
        super.updateUI();
        setForeground(null);
        setBackground(null);
    }

    // implements javax.swing.table.TableCellRenderer
    /**
     * Returns the default table cell renderer.
     * <p/>
     * During a printing operation, this method will be called with <code>isSelected</code> and <code>hasFocus</code>
     * values of <code>false</code> to prevent selection and focus from appearing in the printed output. To do other
     * customization based on whether or not the table is being printed, check the return value from {@link
     * javax.swing.JComponent#isPaintingForPrint()}.
     *
     * @param table      the <code>JTable</code>
     * @param value      the value to assign to the cell at <code>[row, column]</code>
     * @param isSelected true if cell is selected
     * @param hasFocus   true if cell has focus
     * @param row        the row of the cell to render
     * @param column     the column of the cell to render
     *
     * @return the default table cell renderer
     *
     * @see javax.swing.JComponent#isPaintingForPrint()
     */
    public Component getTableCellRendererComponent(JTable table, Object value,
                                                   boolean isSelected, boolean hasFocus, int row, int column) {

        Color fg = null;
        Color bg = null;

        JTable.DropLocation dropLocation = table.getDropLocation();
        if (dropLocation != null
                && !dropLocation.isInsertRow()
                && !dropLocation.isInsertColumn()
                && dropLocation.getRow() == row
                && dropLocation.getColumn() == column) {

            fg = DefaultLookup.getColor(this, ui, "Table.dropCellForeground");
            bg = DefaultLookup.getColor(this, ui, "Table.dropCellBackground");

            isSelected = true;
        }

        if (isSelected) {
            super.setForeground(fg == null ? table.getSelectionForeground() : fg);
            super.setBackground(bg == null ? table.getSelectionBackground() : bg);

            components.setForeground(fg == null ? table.getSelectionForeground() : fg);
            components.setBackground(bg == null ? table.getSelectionBackground() : bg);
        } else {
            Color background = unselectedBackground != null ? unselectedBackground : table.getBackground();
            if (background == null || background instanceof javax.swing.plaf.UIResource) {
                Color alternateColor = DefaultLookup.getColor(this, ui, "Table.alternateRowColor");
                if (alternateColor != null && row % 2 == 0) {
                    background = alternateColor;
                }
            }
            super.setForeground(unselectedForeground != null ? unselectedForeground : table.getForeground());
            super.setBackground(background);

            components.setForeground(unselectedForeground != null ? unselectedForeground : table.getForeground());
            components.setBackground(background);
        }

        if (hasFocus) {
            Border border = null;
            if (isSelected) {
                border = DefaultLookup.getBorder(this, ui, "Table.focusSelectedCellHighlightBorder");
            }
            if (border == null) {
                border = DefaultLookup.getBorder(this, ui, "Table.focusCellHighlightBorder");
            }
            setBorder(border);

            if (!isSelected && table.isCellEditable(row, column)) {
                Color col;
                col = DefaultLookup.getColor(this, ui, "Table.focusCellForeground");
                if (col != null) {
                    super.setForeground(col);

                    components.setForeground(col);
                }
                col = DefaultLookup.getColor(this, ui, "Table.focusCellBackground");
                if (col != null) {
                    super.setBackground(col);

                    components.setBackground(col);
                }
            }
        } else {
            setBorder(getNoFocusBorder());
        }
        components.setBorder(null);

        ForumData fd = (ForumData) value;

        Forum f = fd.getForum();
        ForumStatistic fs = fd.getStat();

        boolean hasUnread = fs != null && fs.getUnreadMessages() > 0;
        boolean isSubcribed = false;
        String titleText = "";
        String statText = "";
        String dateText = "";

        if (f != null) {
            isSubcribed = f.isSubscribed();

            titleText = f.getForumName();

            if (fs != null) {
                StringBuilder text = new StringBuilder(" (");
                text.append(fs.getUnreadMessages());
                text.append("/");
                text.append(fs.getTotalMessages());
                text.append(")");
                statText = text.toString();

                Long forumDate = fs.getLastMessageDate();
                if (forumDate != null && forumDate > 0) {
                    DateFormat dateFormat = DateFormat.getDateTimeInstance(
                            DateFormat.MEDIUM,
                            DateFormat.SHORT,
                            Property.ROJAC_GUI_LOCALE.get());
                    dateText = dateFormat.format(new Date(forumDate));
                }
            }

        } else {
            titleText = "Loading info for forum: " + fd.getForumId();
        }
        table.setToolTipText(titleText + " " + statText);

        int style = Font.PLAIN;
        if (hasUnread) style |= Font.BOLD;
        if (!isSubcribed) style |= Font.ITALIC;

        setFont(table.getFont().deriveFont(style));
        components.setFont(table.getFont().deriveFont(style));

        titleLine.setText(titleText);
        statLine.setText(statText);
        dateLine.setText(dateText);

//        super.invalidate();
        int newHeight = getPreferredSize().height;
        int oldHeight = table.getRowHeight(row);
        if (oldHeight != newHeight) {
            table.setRowHeight(row, newHeight);
        }

        return this;
    }

    /*
    * The following methods are overridden as a performance measure to
    * to prune code-paths are often called in the case of renders
    * but which we know are unnecessary.  Great care should be taken
    * when writing your own renderer to weigh the benefits and
    * drawbacks of overriding methods like these.
    */

    /**
     * Overridden for performance reasons. See the <a href="#override">Implementation Note</a> for more information.
     */
    public boolean isOpaque() {
        Color back = getBackground();
        Component p = getParent();
        if (p != null) {
            p = p.getParent();
        }

        // p should now be the JTable.
        boolean colorMatch = (back != null) && (p != null) &&
                back.equals(p.getBackground()) &&
                p.isOpaque();
        return !colorMatch && super.isOpaque();
    }

    /**
     * Overridden for performance reasons. See the <a href="#override">Implementation Note</a> for more information.
     *
     * @since 1.5
     */
    public void invalidate() {
        super.invalidate();
    }

    /**
     * Overridden for performance reasons. See the <a href="#override">Implementation Note</a> for more information.
     */
    public void validate() {
        super.validate();
    }

    /**
     * Overridden for performance reasons. See the <a href="#override">Implementation Note</a> for more information.
     */
    public void revalidate() {
    }

    /**
     * Overridden for performance reasons. See the <a href="#override">Implementation Note</a> for more information.
     */
    public void repaint(long tm, int x, int y, int width, int height) {

    }

    /**
     * Overridden for performance reasons. See the <a href="#override">Implementation Note</a> for more information.
     */
    public void repaint(Rectangle r) {
    }

    /**
     * Overridden for performance reasons. See the <a href="#override">Implementation Note</a> for more information.
     *
     * @since 1.5
     */
    public void repaint() {
    }

    /**
     * Overridden for performance reasons. See the <a href="#override">Implementation Note</a> for more information.
     */
    protected void firePropertyChange(String propertyName, Object oldValue, Object newValue) {
        // Strings get interned...
        if (propertyName == "text"
                || propertyName == "labelFor"
                || propertyName == "displayedMnemonic"
                || ((propertyName == "font" || propertyName == "foreground")
                && oldValue != newValue
                && getClientProperty(javax.swing.plaf.basic.BasicHTML.propertyKey) != null)) {

            super.firePropertyChange(propertyName, oldValue, newValue);
        }
    }

    /**
     * Overridden for performance reasons. See the <a href="#override">Implementation Note</a> for more information.
     */
    public void firePropertyChange(String propertyName, boolean oldValue, boolean newValue) {
    }

    private class LineRenderer extends JLabel {
        private LineRenderer(int alignment) {
            super();
            setHorizontalAlignment(alignment);
        }

        /**
         * Overridden for performance reasons. See the <a href="#override">Implementation Note</a> for more
         * information.
         *
         * @since 1.5
         */
        public void invalidate() {
        }

        /**
         * Overridden for performance reasons. See the <a href="#override">Implementation Note</a> for more
         * information.
         */
        public void validate() {
        }

        /**
         * Overridden for performance reasons. See the <a href="#override">Implementation Note</a> for more
         * information.
         */
        public void revalidate() {
        }

        /**
         * Overridden for performance reasons. See the <a href="#override">Implementation Note</a> for more
         * information.
         */
        public void repaint(long tm, int x, int y, int width, int height) {
        }

        /**
         * Overridden for performance reasons. See the <a href="#override">Implementation Note</a> for more
         * information.
         */
        public void repaint(Rectangle r) {
        }

        /**
         * Overridden for performance reasons. See the <a href="#override">Implementation Note</a> for more
         * information.
         *
         * @since 1.5
         */
        public void repaint() {
        }

        /**
         * Overridden for performance reasons. See the <a href="#override">Implementation Note</a> for more
         * information.
         */
        protected void firePropertyChange(String propertyName, Object oldValue, Object newValue) {
            // Strings get interned...
            if (propertyName == "text"
                    || propertyName == "labelFor"
                    || propertyName == "displayedMnemonic"
                    || ((propertyName == "font" || propertyName == "foreground")
                    && oldValue != newValue
                    && getClientProperty(javax.swing.plaf.basic.BasicHTML.propertyKey) != null)) {

                super.firePropertyChange(propertyName, oldValue, newValue);
            }
        }

        /**
         * Overridden for performance reasons. See the <a href="#override">Implementation Note</a> for more
         * information.
         */
        public void firePropertyChange(String propertyName, boolean oldValue, boolean newValue) {
        }
    }
}