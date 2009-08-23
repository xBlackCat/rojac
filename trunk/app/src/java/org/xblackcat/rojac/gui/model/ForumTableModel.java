package org.xblackcat.rojac.gui.model;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xblackcat.rojac.data.Forum;
import org.xblackcat.rojac.data.ForumStatistic;
import org.xblackcat.rojac.service.ServiceFactory;
import org.xblackcat.rojac.service.executor.IExecutor;
import org.xblackcat.rojac.service.storage.IForumAH;
import org.xblackcat.rojac.service.storage.IStorage;
import org.xblackcat.rojac.service.storage.StorageException;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * Date: 12 лип 2008
 *
 * @author xBlackCat
 */

public class ForumTableModel extends AbstractTableModel {
    protected final IStorage storage = ServiceFactory.getInstance().getStorage();
    protected final IExecutor executor = ServiceFactory.getInstance().getExecutor();

    private static final Log log = LogFactory.getLog(ForumTableModel.class);

    private List<ForumData> forums = new ArrayList<ForumData>();

    private ForumInfoProcessor processor = new ForumInfoProcessor();

    public ForumData getValueAt(int rowIndex, int columnIndex) {
        return forums.get(rowIndex);
    }

    public int getColumnCount() {
        return 1;
    }

    public int getRowCount() {
        return forums.size();
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return ForumData.class;
    }

    private ForumData getForumData(int id) {
        for (ForumData fd : forums) {
            if (fd.getForumId() == id) {
                return fd;
            }
        }
        return null;
    }

    public void updateForums(int... forumIds) {
        for (int f : forumIds) {
            ForumData fd = new ForumData(f);
            if (!forums.contains(fd)) {
                forums.add(fd);
                
                int ind = forums.indexOf(fd);
                fireTableRowsInserted(ind, ind);
            }
        }

        processor.processForums(forumIds);
    }

    public void reloadInfo(int forumId) {
        processor.processForums(forumId);
    }

    private class ForumInfoProcessor {
        private final Queue<Integer> forumIds = new LinkedList<Integer>();

        private final Runnable processor = new Runnable() {
            public void run() {
                IForumAH fah = storage.getForumAH();

                boolean process;
                do {
                    final int id;
                    synchronized (forumIds) {
                        id = forumIds.remove();
                    }

                    int total = 0;
                    int unread = 0;
                    Forum f;
                    try {
                        f = fah.getForumById(id);
                    } catch (StorageException e) {
                        log.error("Can not load forum information for forum [id:" + id + "].", e);
                        f = null;
                    }

                    try {
                        total = fah.getMessagesInForum(id);
                        unread = fah.getUnreadMessagesInForum(id);
                    } catch (StorageException e) {
                        log.error("Can not load statistic for forum [id:" + id + "].", e);
                    }

                    final ForumStatistic stat = new ForumStatistic(total, unread);
                    final Forum forum = f;

                    SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            ForumData data = getForumData(id);

                            if (forum != null) {
                                data.setForum(forum);
                            }

                            data.setStat(stat);

                            int ind = forums.indexOf(data);
                            fireTableRowsUpdated(ind, ind);
                        }
                    });

                    synchronized (forumIds) {
                        process = !forumIds.isEmpty();
                    }
                } while (process);
            }
        };

        public void processForums(int... id) {
            if (ArrayUtils.isEmpty(id)) {
                return;
            }

            boolean startTask;
            synchronized (forumIds) {
                startTask = forumIds.isEmpty();
                forumIds.addAll(Arrays.asList(ArrayUtils.toObject(id)));
            }
            if (startTask) {
                executor.execute(processor);
            }
        }
    }
}
