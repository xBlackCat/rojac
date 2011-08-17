package org.xblackcat.rojac.gui.view.recenttopics;

import org.xblackcat.rojac.gui.component.JLightPanel;
import org.xblackcat.rojac.gui.component.LineRenderer;
import org.xblackcat.rojac.util.LookupDelegate;
import org.xblackcat.rojac.util.UIUtils;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.io.Serializable;

/**
 * Renterer for a forum view list cell.
 * <p/>
 *
 * @author xBlackCat
 */
class TopicCellRenderer extends JLightPanel
        implements TableCellRenderer, Serializable {

    /**
     * An empty <code>Border</code>. This field might not be used. To change the <code>Border</code> used by this
     * renderer override the <code>getTableCellRendererComponent</code> method and set the border of the returned
     * component directly.
     */
    private static final Border SAFE_NO_FOCUS_BORDER = new EmptyBorder(1, 1, 1, 1);
    private static final Border DEFAULT_NO_FOCUS_BORDER = new EmptyBorder(1, 1, 1, 1);
    protected static Border noFocusBorder = DEFAULT_NO_FOCUS_BORDER;

    private LineRenderer topicLine = new LineRenderer(JLabel.LEFT);
    private LineRenderer infoLine = new LineRenderer(JLabel.RIGHT);
    private LineRenderer forumLine = new LineRenderer(JLabel.RIGHT);

    /**
     * Creates a default table cell renderer.
     */
    public TopicCellRenderer() {
        super(new GridLayout(0, 1));
        setOpaque(true);
        setBorder(getNoFocusBorder());
        setName("Table.cellRenderer");

        add(topicLine);
        add(infoLine);
        add(forumLine);
    }

    private Border getNoFocusBorder() {
        Border border = LookupDelegate.getBorder(this, ui, "Table.cellNoFocusBorder");
        if (System.getSecurityManager() != null) {
            return border != null ? border : SAFE_NO_FOCUS_BORDER;
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
        if (forumLine != null) {
            topicLine.setFont(font);
            infoLine.setFont(font);
            forumLine.setFont(font);
        }
    }

    @Override
    public void setForeground(Color fg) {
        super.setForeground(fg);
        if (forumLine != null) {
            topicLine.setForeground(fg);
            infoLine.setForeground(fg);
            forumLine.setForeground(fg == null ? null : UIUtils.brighter(fg, 0.33));
        }
    }

    @Override
    public void setBackground(Color bg) {
        super.setBackground(bg);
        if (forumLine != null) {
            topicLine.setBackground(bg);
            infoLine.setBackground(bg);
            forumLine.setBackground(bg);
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
     * @return the default table cell renderer
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
        topicLine.setBorder(null);
        infoLine.setBorder(null);
        forumLine.setBorder(null);

        // Setup component with value-based styles

        LastPostInfo f = (LastPostInfo) value;

        topicLine.setText(f.getTopicRoot().getSubject());
        forumLine.setText(f.getForum().getForumName());
        infoLine.setText(f.getLastPost().getUserName());

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
