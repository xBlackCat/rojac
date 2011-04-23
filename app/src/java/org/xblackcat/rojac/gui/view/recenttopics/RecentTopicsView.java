package org.xblackcat.rojac.gui.view.recenttopics;

import org.xblackcat.rojac.gui.IAppControl;
import org.xblackcat.rojac.gui.IViewLayout;
import org.xblackcat.rojac.gui.IViewState;
import org.xblackcat.rojac.gui.view.AView;
import org.xblackcat.rojac.gui.view.ViewId;
import org.xblackcat.rojac.service.datahandler.IPacket;
import org.xblackcat.rojac.service.datahandler.IPacketProcessor;
import org.xblackcat.rojac.service.datahandler.SynchronizationCompletePacket;

/**
 * @author xBlackCat
 */

public class RecentTopicsView extends AView {
    private final RecentThreadsModel model = new RecentThreadsModel();

    public RecentTopicsView(IAppControl appControl) {
        super(null, appControl);

        initializeLayout();
    }


    private void initializeLayout() {

    }

    @SuppressWarnings({"unchecked"})
    @Override
    protected IPacketProcessor<IPacket>[] getProcessors() {
        return (IPacketProcessor<IPacket>[]) new IPacketProcessor[] {
                new IPacketProcessor<SynchronizationCompletePacket>() {
                    @Override
                    public void process(SynchronizationCompletePacket p) {
                        new LatestPostsLoader(model).execute();
                    }
                }
        };
    }

    @Override
    public IViewState getState() {
        return null;
    }

    @Override
    public void setState(IViewState state) {
    }

    @Override
    public IViewLayout storeLayout() {
        return null;
    }

    @Override
    public void setupLayout(IViewLayout o) {
    }
}
