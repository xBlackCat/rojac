package org.xblackcat.rojac.gui.view.recenttopics;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.ArrayUtils;

import javax.swing.*;
import java.util.Collection;

/**
 * @author xBlackCat
 */

class RecentThreadsModel extends AbstractListModel {
    public static final LastPostInfo[] EMPTY_LIST = new LastPostInfo[0];

    private LastPostInfo[] lastPosts = EMPTY_LIST;

    @Override
    public int getSize() {
        return lastPosts.length;
    }

    @Override
    public LastPostInfo getElementAt(int index) {
        return lastPosts[index];
    }

    public void clear() {
        lastPosts = EMPTY_LIST;

        fireIntervalRemoved(this, 0, getSize() - 1);
    }

    public void addLatestPosts(Collection<LastPostInfo> posts) {
        int firstRow = lastPosts.length;

        if (CollectionUtils.isNotEmpty(posts)) {
            LastPostInfo[] newList = new LastPostInfo[lastPosts.length + posts.size()];
            System.arraycopy(lastPosts, 0, newList, 0, lastPosts.length);

            int i = firstRow;
            for (LastPostInfo p : posts) {
                newList[i++] = p;
            }

            lastPosts = newList;
            fireIntervalAdded(this, firstRow, lastPosts.length - 1);
        }

    }
}
