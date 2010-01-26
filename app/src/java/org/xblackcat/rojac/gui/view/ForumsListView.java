package org.xblackcat.rojac.gui.view;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xblackcat.rojac.data.Forum;
import org.xblackcat.rojac.gui.IRootPane;
import org.xblackcat.rojac.gui.popup.PopupMenuBuilder;
import org.xblackcat.rojac.i18n.Messages;
import org.xblackcat.rojac.service.ProcessPacket;
import org.xblackcat.rojac.service.janus.commands.Request;
import org.xblackcat.rojac.service.storage.StorageException;
import org.xblackcat.rojac.util.RojacWorker;
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
import java.util.Arrays;
import java.util.List;

/**
 * Main class for forum view.
 *
 * @author xBlackCat
 */
public class ForumsListView extends AView {
    private static final Log log = LogFactory.getLog(ForumsListView.class);

    // Data and models
    private ForumTableModel forumsModel = new ForumTableModel();

    public ForumsListView(IRootPane rootPane) {
        super(rootPane);
        final JTable forums = new JTable(forumsModel);
        forums.setTableHeader(null);
        add(new JScrollPane(forums));

        forums.setDefaultRenderer(ForumData.class, new MultiLineForumRenderer());
//        forums.setDefaultRenderer(ForumData.class, new ForumCellRenderer());
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

                ForumData forum = forumsModel.getValueAt(modelInd, 0);

                if (e.isPopupTrigger()) {
                    JPopupMenu menu = PopupMenuBuilder.getForumViewMenu(forum, forumsModel, mainFrame);

                    menu.show(forums, p.x, p.y);
                } else if (e.getClickCount() > 1 && e.getButton() == MouseEvent.BUTTON1) {
                    mainFrame.openForumTab(forum.getForum());
                }
            }
        });

        final ForumsRowFilter forumsRowFilter = new ForumsRowFilter();
        final TableRowSorter<ForumTableModel> forumsRowSorter = new TableRowSorter<ForumTableModel>(forumsModel);
        forumsRowSorter.setComparator(0, new ForumDataComparator());
        forumsRowSorter.setStringConverter(new ForumsTableStringConverter());
        forumsRowSorter.setSortable(0, true);
        forumsRowSorter.setSortsOnUpdates(false);
        forumsRowSorter.setSortKeys(Arrays.asList(new RowSorter.SortKey(0, SortOrder.ASCENDING)));
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
                WindowsUtils.setupImageButton("update", new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        mainFrame.performRequest(Request.GET_FORUMS_LIST);
                    }
                }, Messages.VIEW_FORUMS_BUTTON_UPDATE),
                null,
                WindowsUtils.setupToggleButton("filled_only", new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        forumsRowFilter.setNotEmpty(!forumsRowFilter.isNotEmpty());
                        forumsRowSorter.sort();
                    }
                }, Messages.VIEW_FORUMS_BUTTON_FILLED),
                WindowsUtils.setupToggleButton("subscribed_only", new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        forumsRowFilter.setSubscribed(!forumsRowFilter.isSubscribed());
                        forumsRowSorter.sort();
                    }
                }, Messages.VIEW_FORUMS_BUTTON_SUBSCRIBED),
                WindowsUtils.setupToggleButton("unread_only", new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        forumsRowFilter.setUnread(!forumsRowFilter.isUnread());
                        forumsRowSorter.sort();
                    }
                }, Messages.VIEW_FORUMS_BUTTON_HASUNREAD));

        add(toolBar, BorderLayout.NORTH);

    }

    public void applySettings() {
        super.applySettings();

        executor.execute(new ForumLoader());
    }

    public void processPacket(ProcessPacket changedData) {
        if (forumsModel.getRowCount() > 0) {
            forumsModel.updateForums(changedData.getForumIds());
        } else {
            executor.execute(new ForumLoader(changedData.getForumIds()));
        }
    }

    private class ForumLoader extends RojacWorker<Void, Forum> {
        private int[] ids;

        public ForumLoader() {
            this(null);
        }

        public ForumLoader(int[] ids) {
            this.ids = ids;
        }

        @Override
        protected Void perform() throws Exception {
            try {
                publish(storage.getForumAH().getAllForums());
            } catch (StorageException e) {
                log.error("Can not load forum list", e);
                throw e;
            }

            return null;
        }

        @Override
        protected void process(List<Forum> chunks) {
            Forum[] forums = chunks.toArray(new Forum[chunks.size()]);
            forumsModel.fillForums(forums);
            if (ids == null) {
                ids = new int[forums.length];

                for (int i = 0, forumsLength = forums.length; i < forumsLength; i++) {
                    ids[i] = forums[i].getForumId();
                }
            }
            forumsModel.updateForums(ids);
        }
    }
}
