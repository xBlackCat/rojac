package org.xblackcat.rojac.gui.view.thread;

import org.apache.commons.lang.StringUtils;
import org.xblackcat.rojac.data.MessageData;
import org.xblackcat.rojac.gui.component.ComplexTreeRenderer;
import org.xblackcat.rojac.gui.component.JLightPanel;
import org.xblackcat.rojac.gui.component.LineRenderer;
import org.xblackcat.rojac.gui.view.model.Post;
import org.xblackcat.rojac.i18n.Message;

import javax.swing.*;
import java.awt.*;
import java.text.DateFormat;
import java.util.Date;

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
class MultiLineThreadItemRenderer extends ComplexTreeRenderer {
    private LineRenderer titleLine = new LineRenderer(JLabel.LEFT);
    private LineRenderer dateLine = new LineRenderer(JLabel.RIGHT);
    private LineRenderer userLine = new LineRenderer(JLabel.LEFT);
    private LineRenderer iconLine = new LineRenderer(JLabel.LEFT);
    private JPanel titlePane = new JLightPanel(new BorderLayout(0, 2));

    /**
     * Returns a new instance of DefaultTreeCellRenderer.  Alignment is set to left aligned. Icons and text color are
     * determined from the UIManager.
     */
    public MultiLineThreadItemRenderer() {
        super(new BorderLayout(2, 0));

        add(iconLine, BorderLayout.WEST);
        add(titlePane, BorderLayout.CENTER);

        titlePane.add(titleLine, BorderLayout.NORTH);
        titlePane.add(userLine, BorderLayout.CENTER);
        titlePane.add(dateLine, BorderLayout.EAST);

        titlePane.setOpaque(true);

        setDelegatedComponents(titleLine, userLine, dateLine, iconLine, titlePane);
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
        super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);

        if (value != null) {
            Post p = (Post) value;
            MessageData md = p.getMessageData();

            dateLine.setText(null);
            userLine.setText(null);

            if (md != null) {
                titleLine.setText(md.getSubject());

                long forumDate = md.getMessageDate();
                if (forumDate > 0) {
                    DateFormat dateFormat = DateFormat.getDateTimeInstance(
                            DateFormat.MEDIUM,
                            DateFormat.SHORT,
                            Message.getLocale());
                    dateLine.setText(dateFormat.format(new Date(forumDate)));
                }

                if (StringUtils.isEmpty(md.getUserName())) {
                    userLine.setText(String.valueOf(md.getUserId()));
                } else {
                    userLine.setText(md.getUserName());
                }

                int style;
                switch (p.isRead()) {
                    default:
                    case Read:
                    case ReadPartially:
                        style = Font.PLAIN;
                        break;
                    case Unread:
                        style = Font.BOLD;
                        break;
                }

                Font font = tree.getFont().deriveFont(style);
                setFont(font);
                getDelegateComponent().setFont(font);

                if (sel) {
                    tree.setToolTipText(String.valueOf(md.getMessageId()));
                }
            } else {
                titleLine.setText("Loading...");
                Font font = tree.getFont().deriveFont(Font.ITALIC);
                setFont(font);
                getDelegateComponent().setFont(font);
            }
            titlePane.validate();
            titlePane.invalidate();
        }

        return this;
    }

    @Override
    protected void setDisabledIcon(Icon icon) {
        iconLine.setDisabledIcon(icon);
    }

    @Override
    protected void setIcon(Icon icon) {
        iconLine.setIcon(icon);
    }
}
