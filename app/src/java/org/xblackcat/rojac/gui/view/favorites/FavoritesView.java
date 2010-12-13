package org.xblackcat.rojac.gui.view.favorites;

import org.xblackcat.rojac.data.favorite.FavoriteType;
import org.xblackcat.rojac.data.favorite.IFavorite;
import org.xblackcat.rojac.gui.IAppControl;
import org.xblackcat.rojac.gui.view.AView;
import org.xblackcat.rojac.service.datahandler.*;
import org.xblackcat.rojac.util.RojacWorker;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

/**
 * @author xBlackCat
 */

public class FavoritesView extends AView {
    private final FavoritesModel favoritesModel = new FavoritesModel();

    public FavoritesView(IAppControl appControl) {
        super(null, appControl);

        final JTable favoritesList = new JTable(favoritesModel);
        favoritesList.setTableHeader(null);
        favoritesList.setDefaultRenderer(IFavorite.class, new FavoriteCellRenderer());
        JScrollPane scrollPane = new JScrollPane(favoritesList);

        add(scrollPane);

        favoritesList.addMouseListener(new MouseAdapter() {
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

                int ind = favoritesList.rowAtPoint(p);

                int modelInd = favoritesList.convertRowIndexToModel(ind);

                IFavorite favorite = favoritesModel.getValueAt(modelInd, 0);

                if (e.isPopupTrigger()) {
//                    JPopupMenu menu = PopupMenuBuilder.getForumViewMenu(forum, forumsModel, mainFrame);
//
//                    menu.show(e.getComponent(), p.x, p.y);
                } else if (e.getClickCount() > 1 && e.getButton() == MouseEvent.BUTTON1) {
//                    mainFrame.openFavoriteTab(favorite);
                }
            }
        });

        reloadFavorites();
    }

    private void reloadFavorites() {
        new RojacWorker<Void, IFavorite>() {
            @Override
            protected Void perform() throws Exception {
                publish(storage.getFavoriteAH().getFavorites());

                return null;
            }

            @Override
            protected void process(List<IFavorite> chunks) {
                favoritesModel.reload(chunks);
            }
        }.execute();
    }

    @SuppressWarnings({"unchecked"})
    @Override
    protected IPacketProcessor<IPacket>[] getProcessors() {
        return new IPacketProcessor[]{
                new IPacketProcessor<FavoritesUpdatedPacket>() {
                    @Override
                    public void process(FavoritesUpdatedPacket p) {
                        reloadFavorites();
                    }
                },
                new IPacketProcessor<SetForumReadPacket>() {
                    @Override
                    public void process(SetForumReadPacket p) {
                        favoritesModel.updateFavoriteData(null);
                    }
                },
                new IPacketProcessor<FavoriteCategoryUpdatedPacket>() {
                    @Override
                    public void process(FavoriteCategoryUpdatedPacket p) {
                        favoritesModel.updateFavoriteData(FavoriteType.Category);
                    }
                }
        };
    }

}
