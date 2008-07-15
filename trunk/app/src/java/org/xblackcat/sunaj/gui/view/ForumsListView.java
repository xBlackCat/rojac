package org.xblackcat.sunaj.gui.view;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xblackcat.sunaj.data.Forum;
import org.xblackcat.sunaj.gui.model.ForumListModel;
import org.xblackcat.sunaj.gui.model.ForumViewMode;
import org.xblackcat.sunaj.gui.model.ForumViewModeModel;
import org.xblackcat.sunaj.gui.render.ForumCellRenderer;
import org.xblackcat.sunaj.gui.render.ForumListModeRenderer;
import org.xblackcat.sunaj.i18n.Messages;
import org.xblackcat.sunaj.service.ServiceFactory;
import org.xblackcat.sunaj.service.storage.IStorage;
import org.xblackcat.sunaj.service.storage.StorageException;
import org.xblackcat.sunaj.util.WindowsUtils;

import javax.swing.*;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Date: 15 лип 2008
 *
 * @author xBlackCat
 */

public class ForumsListView extends JPanel implements IView {
    private static final Log log = LogFactory.getLog(ForumsListView.class);
    // Data and models
    private ForumListModel forumsModel = new ForumListModel();

    public ForumsListView() {
        super(new BorderLayout(2, 2));
        JList forums = new JList(forumsModel);
        add(new JScrollPane(forums));

        forums.setCellRenderer(new ForumCellRenderer());
        forums.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                checkMenu(e);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                checkMenu(e);
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                checkMenu(e);
            }

            private void checkMenu(MouseEvent e) {
                if (e.isPopupTrigger()) {
                    // TODO: show forum popup menu.
                } else if (e.getClickCount() > 1) {
                    // TODO: make double-click action
                }
            }
        });

        JPanel buttonsPane = new JPanel(new FlowLayout(FlowLayout.RIGHT, 2, 2));

        final ForumViewModeModel modeModel = new ForumViewModeModel();
        final JComboBox viewMode = new JComboBox(modeModel);

        viewMode.setEditable(false);
        viewMode.setRenderer(new ForumListModeRenderer());

        modeModel.addListDataListener(new ListDataListener() {
            public void intervalAdded(ListDataEvent e) {
            }

            public void intervalRemoved(ListDataEvent e) {
            }

            public void contentsChanged(ListDataEvent e) {
                if (e.getIndex0() == -1 && e.getIndex1() == -1) {
                    ForumViewMode vm = modeModel.getSelectedItem();
                    viewMode.setToolTipText(vm.getTooltip());
                    forumsModel.setMode(vm);
                }
            }
        });

        buttonsPane.add(viewMode);
        buttonsPane.add(WindowsUtils.setupButton("update", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(ForumsListView.this, "Test");
            }
        }, Messages.VIEW_FORUMS_BUTTON_UPDATE));

        add(buttonsPane, BorderLayout.NORTH);
    }

    public void applySettings() {
        // TODO: implement
        IStorage storage = ServiceFactory.getInstance().getStorage();

        try {
            Forum[] allForums = storage.getForumAH().getAllForums();
            forumsModel.setForums(allForums);
        } catch (StorageException e) {
            log.error("Can not initialize forum list", e);
        }
    }

    public void updateSettings() {
        // TODO: implement
    }

    public ForumsListView getComponent() {
        return this;
    }
}
