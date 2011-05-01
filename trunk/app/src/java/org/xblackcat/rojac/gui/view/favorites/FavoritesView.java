package org.xblackcat.rojac.gui.view.favorites;

import org.xblackcat.rojac.data.IFavorite;
import org.xblackcat.rojac.gui.*;
import org.xblackcat.rojac.gui.popup.PopupMenuBuilder;
import org.xblackcat.rojac.gui.theme.IconPack;
import org.xblackcat.rojac.gui.theme.ViewIcon;
import org.xblackcat.rojac.gui.view.AView;
import org.xblackcat.rojac.gui.view.ViewType;
import org.xblackcat.rojac.gui.view.model.FavoriteType;
import org.xblackcat.rojac.i18n.Messages;
import org.xblackcat.rojac.service.datahandler.*;
import org.xblackcat.rojac.service.options.Property;
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

    public FavoritesView(final IAppControl appControl) {
        super(null, appControl);

        final JTable favoritesList = new JTable(favoritesModel);
        favoritesList.setTableHeader(null);
        favoritesList.setDefaultRenderer(IFavorite.class, new FavoriteCellRenderer());
        JScrollPane scrollPane = new JScrollPane(favoritesList);

        add(scrollPane);

        favoritesList.addMouseListener(new PopupMouseAdapter() {
            private IFavorite getFavorite(MouseEvent e) {
                int ind = favoritesList.rowAtPoint(e.getPoint());

                int modelInd = favoritesList.convertRowIndexToModel(ind);

                return favoritesModel.getValueAt(modelInd, 0);
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
        });

        reloadFavorites();
    }

    private void reloadFavorites() {
        new RojacWorker<Void, IFavorite>() {
            @Override
            protected Void perform() throws Exception {
                for (IFavorite f : storage.getFavoriteAH().getFavorites()) {
                    publish(f);
                }

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
                },
                new IPacketProcessor<SynchronizationCompletePacket>() {
                    @Override
                    public void process(SynchronizationCompletePacket p) {
                        favoritesModel.updateFavoriteData(null);
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
        return new NoViewLayout();
    }

    @Override
    public void setupLayout(IViewLayout o) {
    }

    @Override
    public String getTabTitle() {
        return Messages.View_Favorites_Title.get();
    }

    @Override
    public Icon getTabTitleIcon() {
        IconPack imagePack = Property.ROJAC_GUI_ICONPACK.get();
        return imagePack.getIcon(ViewIcon.Favorites);
    }

    @Override
    public JPopupMenu getTabTitleMenu() {
        return null;
    }
}
