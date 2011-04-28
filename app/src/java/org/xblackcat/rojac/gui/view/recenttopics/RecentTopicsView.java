package org.xblackcat.rojac.gui.view.recenttopics;

import org.xblackcat.rojac.gui.*;
import org.xblackcat.rojac.gui.view.AView;
import org.xblackcat.rojac.gui.view.ViewId;
import org.xblackcat.rojac.gui.view.forumlist.ForumData;
import org.xblackcat.rojac.service.datahandler.IPacket;
import org.xblackcat.rojac.service.datahandler.IPacketProcessor;
import org.xblackcat.rojac.service.datahandler.SynchronizationCompletePacket;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;

/**
 * @author xBlackCat
 */

public class RecentTopicsView extends AView {
    private final RecentThreadsModel model = new RecentThreadsModel();

    public RecentTopicsView(IAppControl appControl) {
        super(null, appControl);
        setLayout(new BorderLayout(5, 5));

        initializeLayout();

        reloadLastPosts();
    }

    private void reloadLastPosts() {
        model.clear();

        new LatestPostsLoader(model).execute();
    }


    private void initializeLayout() {
        final JList lastPostList = new JList(model);
        add(new JScrollPane(lastPostList));

        lastPostList.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

                LastPostInfo lpi = (LastPostInfo) value;

                String text = "<html><body><p>Post: by " + lpi.getLastPost().getUserName() + "</p><p>Thread: " + lpi.getTopicRoot().getSubject() + "</p><p>Forum: " + lpi.getForum().getForumName() + "</p>";

                setText(text);

                return this;
            }
        });

        lastPostList.addMouseListener(new PopupMouseAdapter() {
            private LastPostInfo getTopicInfo(MouseEvent e) {
                int ind = lastPostList.locationToIndex(e.getPoint());

                return model.getElementAt(ind);
            }

            @Override
            protected void triggerDoubleClick(MouseEvent e) {
                final LastPostInfo info = getTopicInfo(e);
                appControl.openMessage(info.getTopicRoot().getMessageId(), OpenMessageMethod.Default);
            }

            @Override
            protected void triggerPopup(MouseEvent e) {
            }
        });
    }

    @SuppressWarnings({"unchecked"})
    @Override
    protected IPacketProcessor<IPacket>[] getProcessors() {
        return (IPacketProcessor<IPacket>[]) new IPacketProcessor[] {
                new IPacketProcessor<SynchronizationCompletePacket>() {
                    @Override
                    public void process(SynchronizationCompletePacket p) {
                        reloadLastPosts();
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
