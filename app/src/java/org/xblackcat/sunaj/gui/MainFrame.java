package org.xblackcat.sunaj.gui;

import org.xblackcat.sunaj.i18n.Messages;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * Date: 23 груд 2007
 *
 * @author xBlackCat
 */

public class MainFrame extends JFrame {
    private JTabbedPane forums;

    public MainFrame() {
        super(Messages.MAIN_WINDOW_TITLE.getMessage());

        initialize();

//        setJMenuBar(initializeMenu());

        initializeToolBars();

        setDefaultCloseOperation(EXIT_ON_CLOSE);

        pack();
    }

    private void initialize() {
        JPanel cp = new JPanel(new BorderLayout());
        setContentPane(cp);

        // Initialize forums pane
        forums = new JTabbedPane();

        // Initialize forums tree
        JTree forumTree = new JTree();

        JSplitPane sp = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, new JScrollPane(forumTree), forums);
        sp.setContinuousLayout(true);
        sp.setOneTouchExpandable(true);
        sp.setResizeWeight(0);
        // Prevent hiding forums tab pane.
        sp.addPropertyChangeListener(new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                if (evt.getPropertyName().equals(JSplitPane.DIVIDER_LOCATION_PROPERTY)) {
                    JSplitPane sp = (JSplitPane) evt.getSource();
                    if (((Number) evt.getNewValue()).intValue() >= sp.getMaximumDividerLocation()) {
                        sp.setDividerLocation(((Number) evt.getOldValue()).intValue());
                    }
                }
            }
        });
        cp.add(sp, BorderLayout.CENTER);
    }

    private JMenuBar initializeMenu() {
        return null;
    }

    private void initializeToolBars() {
        JToolBar navBar = new JToolBar("Navigation", JToolBar.HORIZONTAL);
        navBar.setLayout(new BorderLayout());
        navBar.setRollover(true);

        JPanel b = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        b.add(new JButton(new AbstractAction("1") {
            public void actionPerformed(ActionEvent e) {
            }
        }));
        b.add(new JButton(new AbstractAction("2") {
            public void actionPerformed(ActionEvent e) {
            }
        }));
        b.add(new JButton(new AbstractAction("3") {
            public void actionPerformed(ActionEvent e) {
            }
        }));


        navBar.add(b, BorderLayout.NORTH);
        navBar.add(new JScrollPane(new JTree()));
        add(navBar, BorderLayout.PAGE_START);
    }
}
