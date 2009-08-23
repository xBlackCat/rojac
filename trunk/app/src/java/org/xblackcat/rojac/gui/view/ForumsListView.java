package org.xblackcat.rojac.gui.view;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xblackcat.rojac.data.Forum;
import org.xblackcat.rojac.gui.IRootPane;
import org.xblackcat.rojac.gui.IView;
import org.xblackcat.rojac.gui.model.ForumData;
import org.xblackcat.rojac.gui.model.ForumTableModel;
import org.xblackcat.rojac.gui.popup.PopupMenuBuilder;
import org.xblackcat.rojac.i18n.Messages;
import org.xblackcat.rojac.service.ServiceFactory;
import org.xblackcat.rojac.service.commands.GetForumListCommand;
import org.xblackcat.rojac.service.commands.IResultHandler;
import org.xblackcat.rojac.service.executor.IExecutor;
import org.xblackcat.rojac.service.storage.IStorage;
import org.xblackcat.rojac.service.storage.StorageException;
import org.xblackcat.rojac.util.WindowsUtils;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableRowSorter;
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
    protected final IExecutor executor = ServiceFactory.getInstance().getExecutor();
    protected final IStorage storage = ServiceFactory.getInstance().getStorage();
    
    private static final Log log = LogFactory.getLog(ForumsListView.class);
    // Data and models
    private ForumTableModel forumsModel = new ForumTableModel();
    private final IRootPane mainFrame;

    public ForumsListView(IRootPane rootPane) {
        super(new BorderLayout(2, 2));
        this.mainFrame = rootPane;
        final JTable forums = new JTable(forumsModel);
        forums.setTableHeader(null);
        add(new JScrollPane(forums));

        forums.setFont(forums.getFont().deriveFont(Font.PLAIN));
        forums.setDefaultRenderer(ForumData.class, new ForumCellRenderer());
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
                final Point p = e.getPoint();

                int ind = forums.rowAtPoint(p);

                int modelInd = forums.convertRowIndexToModel(ind);

                Forum forum = forumsModel.getValueAt(modelInd, 0).getForum();

                if (e.isPopupTrigger()) {
                    JPopupMenu menu = PopupMenuBuilder.getForumViewMenu(forum, forumsModel, mainFrame);

                    menu.show(forums, p.x, p.y);
                } else if (e.getClickCount() > 1 && e.getButton() == MouseEvent.BUTTON1) {
                    mainFrame.openForumTab(forum);
                }
            }
        });

        final ForumsRowFilter forumsRowFilter = new ForumsRowFilter();
        final TableRowSorter<ForumTableModel> forumsRowSorter = new TableRowSorter<ForumTableModel>(forumsModel);
        forumsRowSorter.setComparator(0, new ForumDataComparator());
        forumsRowSorter.setStringConverter(new ForumsTableStringConverter());
        forumsRowSorter.setSortable(0, true);
        forumsRowSorter.setSortsOnUpdates(false);
        forumsRowSorter.setRowFilter(forumsRowFilter);
        forumsRowSorter.sort();
        forums.setRowSorter(forumsRowSorter);

        forumsModel.addTableModelListener(new TableModelListener() {
            public void tableChanged(TableModelEvent e) {
                if (e.getType() == TableModelEvent.UPDATE) {
                    forumsRowSorter.sort();
                }
            }
        });

        JToolBar toolBar = WindowsUtils.createToolBar(
                WindowsUtils.setupImageButton("update", new UpdateActionListener(), Messages.VIEW_FORUMS_BUTTON_UPDATE),
                WindowsUtils.setupToggleButton("update", new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        forumsRowFilter.setNotEmpty(!forumsRowFilter.isNotEmpty());
                        forumsRowSorter.sort();
                    }
                }, Messages.VIEW_FORUMS_BUTTON_FILLED),
                WindowsUtils.setupToggleButton("update", new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        forumsRowFilter.setSubscribed(!forumsRowFilter.isSubscribed());
                        forumsRowSorter.sort();
                    }
                }, Messages.VIEW_FORUMS_BUTTON_SUBSCRIBED),
                WindowsUtils.setupToggleButton("update", new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        forumsRowFilter.setUnread(!forumsRowFilter.isUnread());
                        forumsRowSorter.sort();
                    }
                }, Messages.VIEW_FORUMS_BUTTON_HASUNREAD));

        add(toolBar, BorderLayout.NORTH);

    }

    public void applySettings() {
        // TODO: implement

        executor.execute(new Runnable() {
            public void run() {
                try {
                    final int[] allForums = storage.getForumAH().getAllForumIds();
                    SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            forumsModel.updateForums(allForums);
                        }
                    });
                } catch (StorageException e) {
                    log.error("Can not load forum list", e);
                }
            }
        });
            }

    public void updateSettings() {
        // TODO: implement
    }

    public ForumsListView getComponent() {
        return this;
    }

    public void updateData(int... ids) {
        forumsModel.updateForums(ids);
    }

    private class UpdateActionListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            mainFrame.showProgressDialog(new GetForumListCommand(new IResultHandler<int[]>() {
                public void process(final int[] forumIds) throws StorageException {
                    SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            forumsModel.updateForums(forumIds);
                        }
                    });
                }
            }));
        }
    }
}
