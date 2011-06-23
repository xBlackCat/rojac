package org.xblackcat.rojac.gui.view.forumlist;

import org.xblackcat.rojac.data.Forum;
import org.xblackcat.rojac.data.ForumStatistic;
import org.xblackcat.rojac.gui.component.JLightPanel;
import org.xblackcat.rojac.gui.component.LineRenderer;
import org.xblackcat.rojac.i18n.LocaleControl;
import org.xblackcat.rojac.util.LookupDelegate;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.io.Serializable;
import java.text.DateFormat;
import java.util.Date;


class MultiLineForumRenderer extends JLightPanel
        implements TableCellRenderer, Serializable {

    /**
     * An empty <code>Border</code>. This field might not be used. To change the <code>Border</code> used by this
     * renderer override the <code>getTableCellRendererComponent</code> method and set the border of the returned
     * component directly.
     */
    private static final Border SAFE_NO_FOCUS_BORDER = new EmptyBorder(1, 1, 1, 1);
    private static final Border DEFAULT_NO_FOCUS_BORDER = new EmptyBorder(1, 1, 1, 1);
    protected static Border noFocusBorder = DEFAULT_NO_FOCUS_BORDER;

    private LineRenderer titleLine = new LineRenderer(JLabel.LEFT);
    private LineRenderer dateLine = new LineRenderer(JLabel.RIGHT);
    private LineRenderer statLine = new LineRenderer(JLabel.RIGHT);

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
    }

    private Border getNoFocusBorder() {
        Border border = LookupDelegate.getBorder(this, ui, "Table.cellNoFocusBorder");
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

    @Override
    public void setFont(Font font) {
        super.setFont(font);
        if (statLine != null) {
            titleLine.setFont(font);
            dateLine.setFont(font);
            statLine.setFont(font);
        }
    }

    @Override
    public void setForeground(Color fg) {
        super.setForeground(fg);
        if (statLine != null) {
            titleLine.setForeground(fg);
            dateLine.setForeground(fg);
            statLine.setForeground(fg);
        }
    }

    @Override
    public void setBackground(Color bg) {
        super.setBackground(bg);
        if (statLine != null) {
            titleLine.setBackground(bg);
            dateLine.setBackground(bg);
            statLine.setBackground(bg);
        }
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

        // Make general steps
        Color fg = null;
        Color bg = null;

        JTable.DropLocation dropLocation = table.getDropLocation();
        if (dropLocation != null
                && !dropLocation.isInsertRow()
                && !dropLocation.isInsertColumn()
                && dropLocation.getRow() == row
                && dropLocation.getColumn() == column) {

            fg = LookupDelegate.getColor(this, ui, "Table.dropCellForeground");
            bg = LookupDelegate.getColor(this, ui, "Table.dropCellBackground");

            isSelected = true;
        }

        Color foreground;
        Color background;
        if (isSelected) {
            foreground = fg == null ? table.getSelectionForeground() : fg;
            background = bg == null ? table.getSelectionBackground() : bg;
        } else {
            background = table.getBackground();
            if (background == null || background instanceof javax.swing.plaf.UIResource) {
                if (row % 2 == 0) {
                    Color alternateColor = LookupDelegate.getColor(this, ui, "Table.alternateRowColor");
                    if (alternateColor != null) {
                        background = alternateColor;
                    }
                }
            }
            foreground = table.getForeground();
        }

        setForeground(foreground);
        setBackground(background);

        if (hasFocus) {
            Border border = null;
            if (isSelected) {
                border = LookupDelegate.getBorder(this, ui, "Table.focusSelectedCellHighlightBorder");
            }
            if (border == null) {
                border = LookupDelegate.getBorder(this, ui, "Table.focusCellHighlightBorder");
            }
            setBorder(border);

            if (!isSelected && table.isCellEditable(row, column)) {
                Color col;
                col = LookupDelegate.getColor(this, ui, "Table.focusCellForeground");
                if (col != null) {
                    setForeground(col);
                }
                col = LookupDelegate.getColor(this, ui, "Table.focusCellBackground");
                if (col != null) {
                    setBackground(col);
                }
            }
        } else {
            setBorder(getNoFocusBorder());
        }
        titleLine.setBorder(null);
        dateLine.setBorder(null);
        statLine.setBorder(null);

        // Setup component with value-based styles

        ForumData fd = (ForumData) value;

        Forum f = fd.getForum();
        ForumStatistic fs = fd.getStat();

        boolean hasUnread = fs != null && fs.getUnreadMessages() > 0;
        boolean isSubscribed = false;
        String titleText = "";
        String statText = "";
        String dateText = "";

        if (f != null) {
            isSubscribed = fd.isSubscribed();

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
                            LocaleControl.getInstance().getLocale());
                    dateText = dateFormat.format(new Date(forumDate));
                }
            }

        } else {
            titleText = "Loading info for forum: " + fd.getForumId();
        }
        table.setToolTipText(titleText + " " + statText);

        int style = Font.PLAIN;
        if (hasUnread) style |= Font.BOLD;
        if (!isSubscribed) style |= Font.ITALIC;

        Font font = table.getFont().deriveFont(style);
        setFont(font);

        titleLine.setText(titleText);
        statLine.setText(statText);
        dateLine.setText(dateText);

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

}
