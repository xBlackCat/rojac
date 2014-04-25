/*
 * @(#)DefaultTableCellRenderer.java	1.48 08/09/18
 *
 * Copyright 2006 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package org.xblackcat.rojac.gui.view.thread;

import org.xblackcat.rojac.gui.view.model.APostProxy;
import org.xblackcat.rojac.util.LookupDelegate;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.io.Serializable;


/**
 * The standard class for rendering (displaying) individual cells in a {@code JTable}.
 * <p/>
 * <p/>
 * <strong><a name="override">Implementation Note:</a></strong> This class inherits from {@code JLabel}, a standard
 * component class. However {@code JTable} employs a unique mechanism for rendering its cells and therefore
 * requires some slightly modified behavior from its cell renderer. The table class defines a single cell renderer and
 * uses it as a as a rubber-stamp for rendering all cells in the table; it renders the first cell, changes the contents
 * of that cell renderer, shifts the origin to the new location, re-draws it, and so on. The standard
 * {@code JLabel} component was not designed to be used this way and we want to avoid triggering a
 * {@code revalidate} each time the cell is drawn. This would greatly decrease performance because the
 * {@code revalidate} message would be passed up the hierarchy of the container to determine whether any other
 * components would be affected. As the renderer is only parented for the lifetime of a painting operation we similarly
 * want to avoid the overhead associated with walking the hierarchy for painting operations. So this class overrides the
 * {@code validate}, {@code invalidate}, {@code revalidate}, {@code repaint}, and
 * {@code firePropertyChange} methods to be no-ops and override the {@code isOpaque} method solely to improve
 * performance.  If you write your own renderer, please keep this performance consideration in mind.
 * <p/>
 * <p/>
 * <strong>Warning:</strong> Serialized objects of this class will not be compatible with future Swing releases. The
 * current serialization support is appropriate for short term storage or RMI between applications running the same
 * version of Swing.  As of 1.4, support for long term storage of all JavaBeans<sup><font size="-2">TM</font></sup> has
 * been added to the {@code java.beans} package. Please see {@link java.beans.XMLEncoder}.
 *
 * @author Philip Milne
 * @version 1.48 09/18/08
 * @see javax.swing.JTable
 */
public class PostTableCellRenderer extends JLabel
        implements TableCellRenderer, Serializable {

    /**
     * An empty {@code Border}. This field might not be used. To change the {@code Border} used by this
     * renderer override the {@code getTableCellRendererComponent} method and set the border of the returned
     * component directly.
     */
    private static final Border SAFE_NO_FOCUS_BORDER = new EmptyBorder(1, 1, 1, 1);
    private static final Border DEFAULT_NO_FOCUS_BORDER = new EmptyBorder(1, 1, 1, 1);
    protected static Border noFocusBorder = DEFAULT_NO_FOCUS_BORDER;

    /**
     * Creates a default table cell renderer.
     */
    public PostTableCellRenderer() {
        super();
        setOpaque(true);
        setBorder(getNoFocusBorder());
        setName("Table.cellRenderer");
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
     * Notification from the {@code UIManager} that the look and feel [L&F] has changed. Replaces the current UI
     * object with the latest version from the {@code UIManager}.
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
     * During a printing operation, this method will be called with {@code isSelected} and {@code hasFocus}
     * values of {@code false} to prevent selection and focus from appearing in the printed output. To do other
     * customization based on whether or not the table is being printed, check the return value from {@link
     * javax.swing.JComponent#isPaintingForPrint()}.
     *
     * @param table      the {@code JTable}
     * @param value      the value to assign to the cell at {@code [row, column]}
     * @param isSelected true if cell is selected
     * @param hasFocus   true if cell has focus
     * @param row        the row of the cell to render
     * @param column     the column of the cell to render
     * @return the default table cell renderer
     * @see javax.swing.JComponent#isPaintingForPrint()
     */
    public Component getTableCellRendererComponent(JTable table, Object value,
                                                   boolean isSelected, boolean hasFocus, int row, int column) {

        if (!(value instanceof APostProxy)) {
            throw new IllegalArgumentException("Got non-PostData class value object: " + value.getClass().getName());
        }
        APostProxy postProxy = (APostProxy) value;

        // Make common actions to prepare renderer.
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

        if (isSelected) {
            super.setForeground(fg == null ? table.getSelectionForeground()
                    : fg);
            super.setBackground(bg == null ? table.getSelectionBackground()
                    : bg);
        } else {
            Color background = table.getBackground();
            if (background == null || background instanceof javax.swing.plaf.UIResource) {
                if (row % 2 == 0) {
                    Color alternateColor = LookupDelegate.getColor(this, ui, "Table.alternateRowColor");
                    if (alternateColor != null && row % 2 != 0) {
                        background = alternateColor;
                    }
                }
            }
            super.setForeground(table.getForeground());
            super.setBackground(background);
        }

        setFont(table.getFont());

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
                    super.setForeground(col);
                }
                col = LookupDelegate.getColor(this, ui, "Table.focusCellBackground");
                if (col != null) {
                    super.setBackground(col);
                }
            }
        } else {
            setBorder(getNoFocusBorder());
        }

        // Make actions to show post data

        postProxy.prepareRenderer(this);

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
    }

    /**
     * Overridden for performance reasons. See the <a href="#override">Implementation Note</a> for more information.
     */
    public void validate() {
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


    /**
     * A subclass of {@code DefaultTableCellRenderer} that implements {@code UIResource}.
     * {@code DefaultTableCellRenderer} doesn't implement {@code UIResource} directly so that applications can
     * safely override the {@code cellRenderer} property with {@code DefaultTableCellRenderer} subclasses.
     * <p/>
     * <strong>Warning:</strong> Serialized objects of this class will not be compatible with future Swing releases. The
     * current serialization support is appropriate for short term storage or RMI between applications running the same
     * version of Swing.  As of 1.4, support for long term storage of all JavaBeans<sup><font size="-2">TM</font></sup>
     * has been added to the {@code java.beans} package. Please see {@link java.beans.XMLEncoder}.
     */
    public static class UIResource extends PostTableCellRenderer
            implements javax.swing.plaf.UIResource {
    }

}
