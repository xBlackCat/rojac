package org.xblackcat.rojac.gui.view.favorites;

import org.xblackcat.rojac.data.Favorite;
import org.xblackcat.rojac.gui.IAppControl;
import org.xblackcat.rojac.gui.IViewLayout;
import org.xblackcat.rojac.gui.NoViewLayout;
import org.xblackcat.rojac.gui.PopupMouseAdapter;
import org.xblackcat.rojac.gui.popup.PopupMenuBuilder;
import org.xblackcat.rojac.gui.theme.ViewIcon;
import org.xblackcat.rojac.gui.view.AView;
import org.xblackcat.rojac.gui.view.ViewType;
import org.xblackcat.rojac.gui.view.model.FavoriteType;
import org.xblackcat.rojac.i18n.Message;
import org.xblackcat.rojac.service.datahandler.*;
import org.xblackcat.rojac.service.storage.IFavoriteAH;
import org.xblackcat.rojac.service.storage.Storage;
import org.xblackcat.rojac.util.RojacWorker;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.List;

/**
 * @author xBlackCat
 */

public class FavoritesView extends AView {
    private final FavoritesModel favoritesModel = new FavoritesModel();
    private final PacketDispatcher packetDispatcher = new PacketDispatcher(
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
            new IPacketProcessor<SetPostReadPacket>() {
                @Override
                public void process(SetPostReadPacket p) {
                    favoritesModel.updateFavoriteData(null);
                }
            },
            new IPacketProcessor<SetSubThreadReadPacket>() {
                @Override
                public void process(SetSubThreadReadPacket p) {
                    favoritesModel.updateFavoriteData(null);
                }
            },
            new IPacketProcessor<SetReadExPacket>() {
                @Override
                public void process(SetReadExPacket p) {
                    favoritesModel.updateFavoriteData(null);
                }
            },
            new IPacketProcessor<FavoriteCategoryUpdatedPacket>() {
                @Override
                public void process(FavoriteCategoryUpdatedPacket p) {
                    favoritesModel.updateFavoriteData(FavoriteType.Category);
                }
            },
            new IPacketProcessor<SynchronizationCompletePacket>() {
                @Override
                public void process(SynchronizationCompletePacket p) {
                    favoritesModel.updateFavoriteData(null);
                }
            }
    );

    public FavoritesView(final IAppControl appControl) {
        super(appControl);

        final JTable favoritesList = new JTable(favoritesModel);
        favoritesList.setTableHeader(null);
        favoritesList.setDefaultRenderer(FavoriteData.class, new FavoriteCellRenderer());
        JScrollPane scrollPane = new JScrollPane(favoritesList);

        add(scrollPane);

        favoritesList.addMouseListener(
                new PopupMouseAdapter() {
                    private Favorite getFavorite(MouseEvent e) {
                        int ind = favoritesList.rowAtPoint(e.getPoint());

                        int modelInd = favoritesList.convertRowIndexToModel(ind);

                        return favoritesModel.getValueAt(modelInd, 0).getFavorite();
                    }

                    @Override
                    protected void triggerDoubleClick(MouseEvent e) {
                        appControl.openTab(ViewType.Favorite.makeId(getFavorite(e).getId()));
                    }

                    @Override
                    protected void triggerPopup(MouseEvent e) {
                        JPopupMenu menu = PopupMenuBuilder.getFavoritesMenu(getFavorite(e), appControl);

                        Point p = e.getPoint();
                        menu.show(e.getComponent(), p.x, p.y);
                    }
                }
        );

        reloadFavorites();
    }

    private void reloadFavorites() {
        new RojacWorker<Void, Favorite>() {
            @Override
            protected Void perform() throws Exception {
                for (Favorite f : Storage.get(IFavoriteAH.class).getFavorites()) {
                    publish(f);
                }

                return null;
            }

            @Override
            protected void process(List<Favorite> chunks) {
                favoritesModel.reload(chunks);
            }
        }.execute();
    }

    @Override
    public IViewLayout storeLayout() {
        return new NoViewLayout();
    }

    @Override
    public void setupLayout(IViewLayout o) {
    }

    @Override
    public String getTabTitle() {
        return Message.View_Favorites_Title.get();
    }

    @Override
    public Icon getTabTitleIcon() {
        return ViewIcon.Favorites;
    }

    @Override
    public JPopupMenu getTabTitleMenu() {
        return null;
    }

    @Override
    public final void processPacket(IPacket packet) {
        packetDispatcher.dispatch(packet);
    }
}
