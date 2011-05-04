package org.xblackcat.rojac.gui.view.recenttopics;

import org.xblackcat.rojac.gui.*;
import org.xblackcat.rojac.gui.popup.PopupMenuBuilder;
import org.xblackcat.rojac.gui.theme.IconPack;
import org.xblackcat.rojac.gui.theme.ViewIcon;
import org.xblackcat.rojac.gui.view.AView;
import org.xblackcat.rojac.i18n.Messages;
import org.xblackcat.rojac.service.datahandler.IPacket;
import org.xblackcat.rojac.service.datahandler.IPacketProcessor;
import org.xblackcat.rojac.service.datahandler.OptionsUpdatedPacket;
import org.xblackcat.rojac.service.datahandler.SynchronizationCompletePacket;
import org.xblackcat.rojac.service.options.Property;

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
        if (!model.isLoading()) {
            model.clear();

            new LatestPostsLoader(model).execute();
        }
    }


    private void initializeLayout() {
        final JTable lastPostList = new JTable(model);
        lastPostList.setTableHeader(null);
        add(new JScrollPane(lastPostList));

        lastPostList.setDefaultRenderer(LastPostInfo.class, new TopicCellRenderer());

        lastPostList.addMouseListener(new PopupMouseAdapter() {
            private LastPostInfo getTopicInfo(MouseEvent e) {
                int ind = lastPostList.rowAtPoint(e.getPoint());

                return model.getValueAt(ind, 0);
            }

            @Override
            protected void triggerDoubleClick(MouseEvent e) {
                LastPostInfo info = getTopicInfo(e);
                OpenMessageMethod method = Property.OPEN_MESSAGE_BEHAVIOUR_RECENT_TOPICS.get();
                int messageId = info.getTopicRoot().getMessageId();

                appControl.openMessage(messageId, method);
            }

            @Override
            protected void triggerPopup(MouseEvent e) {
                LastPostInfo info = getTopicInfo(e);

                JPopupMenu menu = PopupMenuBuilder.getRecentPostsMenu(info.getTopicRoot(), appControl);

                final Point p = e.getPoint();
                menu.show(e.getComponent(), p.x, p.y);

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
                },
                new IPacketProcessor<OptionsUpdatedPacket>() {
                    @Override
                    public void process(OptionsUpdatedPacket p) {
                        if (p.isPropertyAffected(Property.VIEW_RECENT_TOPIC_LIST_SIZE)) {
                            reloadLastPosts();
                        }
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

    @Override
    public String getTabTitle() {
        return Messages.View_RecentTopics_Title.get();
    }

    @Override
    public Icon getTabTitleIcon() {
        IconPack imagePack = Property.ROJAC_GUI_ICONPACK.get();
        return imagePack.getIcon(ViewIcon.RecentTopics);
    }

    @Override
    public JPopupMenu getTabTitleMenu() {
        return null;
    }
}
