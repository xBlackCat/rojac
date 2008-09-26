package org.xblackcat.rojac.gui.view;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xblackcat.rojac.data.Forum;
import org.xblackcat.rojac.gui.IRootPane;
import org.xblackcat.rojac.gui.IView;
import org.xblackcat.rojac.gui.frame.progress.IProgressTracker;
import org.xblackcat.rojac.gui.model.ForumData;
import org.xblackcat.rojac.gui.model.ForumListModel;
import org.xblackcat.rojac.gui.model.ForumViewMode;
import org.xblackcat.rojac.gui.model.ForumViewModeModel;
import org.xblackcat.rojac.gui.render.ForumCellRenderer;
import org.xblackcat.rojac.gui.render.ForumListModeRenderer;
import org.xblackcat.rojac.i18n.Messages;
import org.xblackcat.rojac.service.ServiceFactory;
import org.xblackcat.rojac.service.executor.IExecutor;
import org.xblackcat.rojac.service.storage.IForumAH;
import org.xblackcat.rojac.service.storage.IStorage;
import org.xblackcat.rojac.service.storage.StorageException;
import org.xblackcat.rojac.service.synchronizer.GetForumListCommand;
import org.xblackcat.rojac.util.WindowsUtils;

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
    private final IRootPane mainFrame;

    public ForumsListView(IRootPane rootPane) {
        super(new BorderLayout(2, 2));
        this.mainFrame = rootPane;
        final JList forums = new JList(forumsModel);
        add(new JScrollPane(forums));

        forums.setFont(forums.getFont().deriveFont(Font.PLAIN));
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
                    final Point p = e.getPoint();

                    int ind = forums.locationToIndex(p);

                    Forum forum = ((ForumData) forumsModel.getElementAt(ind)).getForum();
                    JPopupMenu menu = createMenu(forum);

                    menu.show(forums, p.x, p.y);
                } else if (e.getClickCount() > 1 && e.getButton() == MouseEvent.BUTTON1) {
                    Forum forum = ((ForumData) forums.getSelectedValue()).getForum();
                    mainFrame.openForumTab(forum);
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
                mainFrame.showProgressDialog(new GetForumListTask());
            }
        }, Messages.VIEW_FORUMS_BUTTON_UPDATE));

        add(buttonsPane, BorderLayout.NORTH);
    }

    public void applySettings() {
        // TODO: implement

        try {
            reloadList();
        } catch (StorageException e) {
            log.error("Can not load forum list", e);
        }
    }

    private void reloadList() throws StorageException {
        IStorage storage = ServiceFactory.getInstance().getStorage();

        final Forum[] allForums = storage.getForumAH().getAllForums();
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                forumsModel.setForums(allForums);
            }
        });
    }

    public void updateSettings() {
        // TODO: implement
    }

    public ForumsListView getComponent() {
        return this;
    }

    private JPopupMenu createMenu(Forum f) {
        JPopupMenu m = new JPopupMenu(f.getForumName());

        final boolean subscribed = f.isSubscribed();
        final int forumId = f.getForumId();

        JCheckBoxMenuItem mi = new JCheckBoxMenuItem(Messages.VIEW_FORUMS_MENU_SUBSCRIBE.getMessage(), subscribed);
        mi.addActionListener(new SubscribeChangeListener(forumId, subscribed));

        m.add(mi);

        return m;
    }

    private class GetForumListTask extends GetForumListCommand {
        @Override
        public void doTask(IProgressTracker trac) throws Exception {
            super.doTask(trac);

            reloadList();
        }
    }

    private class SubscribeChangeListener implements ActionListener {
        private final int forumId;
        private final boolean subscribed;

        public SubscribeChangeListener(int forumId, boolean subscribed) {
            this.forumId = forumId;
            this.subscribed = subscribed;
        }

        public void actionPerformed(ActionEvent e) {
            IExecutor executor = ServiceFactory.getInstance().getExecutor();
            executor.execute(new Runnable() {
                public void run() {
                    IForumAH fah = ServiceFactory.getInstance().getStorage().getForumAH();
                    try {
                        fah.setSubscribeForum(forumId, !subscribed);
                        
                        SwingUtilities.invokeLater(new Runnable() {
                            public void run() {
                                forumsModel.reloadInfo(forumId);
                            }
                        });
                    } catch (StorageException e1) {
                        log.error("Can not update forum info. [id:" + forumId + "].", e1);
                    }
                }
            });
        }
    }
}
