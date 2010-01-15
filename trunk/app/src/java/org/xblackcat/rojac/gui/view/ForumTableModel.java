package org.xblackcat.rojac.gui.view;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xblackcat.rojac.data.Forum;
import org.xblackcat.rojac.data.ForumStatistic;
import org.xblackcat.rojac.service.ServiceFactory;
import org.xblackcat.rojac.service.executor.IExecutor;
import org.xblackcat.rojac.service.executor.TaskType;
import org.xblackcat.rojac.service.storage.IForumAH;
import org.xblackcat.rojac.service.storage.IStorage;
import org.xblackcat.rojac.util.RojacWorker;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author xBlackCat
 */

public class ForumTableModel extends AbstractTableModel {
    protected final IStorage storage = ServiceFactory.getInstance().getStorage();
    protected final IExecutor executor = ServiceFactory.getInstance().getExecutor();

    private static final Log log = LogFactory.getLog(ForumTableModel.class);

    private List<ForumData> forums = new ArrayList<ForumData>();

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

    public void updateForums(final int... forumIds) {
        RojacWorker<Void, ForumStatistic> infoLoader = new RojacWorker<Void, ForumStatistic>() {
            @Override
            protected Void perform() throws Exception {
                IForumAH fah = storage.getForumAH();

                Map<Integer, Integer> totalMessages = fah.getMessagesInForum(forumIds);
                Map<Integer, Integer> unreadMessages = fah.getUnreadMessagesInForum(forumIds);
                Map<Integer, Long> lastPostDate = fah.getLastMessageDateInForum(forumIds);

                for (int forumId : forumIds) {
                    publish(new ForumStatistic(
                            forumId,
                            totalMessages.get(forumId),
                            unreadMessages.get(forumId),
                            lastPostDate.get(forumId)
                    ));
                }

                return null;
            }

            @Override
            protected void process(List<ForumStatistic> chunks) {
                for (ForumStatistic stat : chunks) {
                    int idx = getForumDataIndex(stat.getForumId());
                    if (idx != -1) {
                        ForumData fd = forums.get(idx);
                        fd.setStat(stat);
                        fireTableRowsUpdated(idx, idx);
                    }
                }
            }
        };

        executor.execute(infoLoader, TaskType.MessageLoading);
    }

    void fillForums(Forum... forums) {
        this.forums.clear();

        for (Forum forum : forums) {
            this.forums.add(new ForumData(forum));
        }

        fireTableDataChanged();
    }

    private int getForumDataIndex(int forumId) {
        for (int i = 0, forumsSize = forums.size(); i < forumsSize; i++) {
            ForumData fd = forums.get(i);
            if (fd.getForumId() == forumId) {
                return i;
            }
        }

        return -1;
    }

    public void setSubscribed(int forumId, boolean subscribed) {
        int idx = getForumDataIndex(forumId);
        ForumData fd = forums.get(idx);
        fd.setSubscribed(subscribed);
        fireTableRowsUpdated(idx, idx);
    }
}
