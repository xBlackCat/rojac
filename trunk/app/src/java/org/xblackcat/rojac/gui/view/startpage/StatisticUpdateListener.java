package org.xblackcat.rojac.gui.view.startpage;

import org.xblackcat.rojac.data.DiscussionStatistic;
import org.xblackcat.rojac.gui.tray.IStatisticListener;
import org.xblackcat.rojac.util.RojacUtils;

import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import java.util.ArrayList;
import java.util.Collection;

/**
* 10.05.12 13:09
*
* @author xBlackCat
*/
class StatisticUpdateListener implements TreeModelListener {
    private final Collection<IStatisticListener> statisticListeners = new ArrayList<>();
    private NavigationModel model;

    public StatisticUpdateListener(NavigationModel model) {
        this.model = model;
    }

    @Override
    public void treeNodesChanged(TreeModelEvent e) {
        updateGlobalStatistic();
    }

    @Override
    public void treeNodesInserted(TreeModelEvent e) {
        updateGlobalStatistic();
    }

    @Override
    public void treeNodesRemoved(TreeModelEvent e) {
        updateGlobalStatistic();
    }

    @Override
    public void treeStructureChanged(TreeModelEvent e) {
    }

    public void addStatisticListener(IStatisticListener l) {
        assert RojacUtils.checkThread(true);

        statisticListeners.add(l);
    }

    public void removeStatisticListener(IStatisticListener l) {
        assert RojacUtils.checkThread(true);

        statisticListeners.remove(l);
    }

    private void updateGlobalStatistic() {
        final DiscussionStatistic globalStatistic = model.getGlobalStatistic();

        for (IStatisticListener l : statisticListeners) {
            l.statisticUpdated(globalStatistic);
        }
    }
}
