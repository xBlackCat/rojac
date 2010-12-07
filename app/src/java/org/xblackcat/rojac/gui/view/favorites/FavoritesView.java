package org.xblackcat.rojac.gui.view.favorites;

import org.xblackcat.rojac.gui.IRootPane;
import org.xblackcat.rojac.gui.view.AView;
import org.xblackcat.rojac.service.datahandler.IPacket;
import org.xblackcat.rojac.service.datahandler.IPacketProcessor;

import javax.swing.*;

/**
 * @author xBlackCat
 */

public class FavoritesView extends AView {
    private final FavoritesModel dataModel = new FavoritesModel();

    public FavoritesView(IRootPane rootPane) {
        super(null, rootPane);

        JTable favoritesList = new JTable(dataModel);
        favoritesList.setTableHeader(null);
        favoritesList.setDefaultRenderer(IFavorite.class, new FavoriteCellRenderer());
        JScrollPane scrollPane = new JScrollPane(favoritesList);

        dataModel.reload(new UnreadTrackingFavorite(), new UnreadTrackingFavorite());

        add(scrollPane);
    }

    @Override
    protected IPacketProcessor<IPacket>[] getProcessors() {
        return null;
    }

}
