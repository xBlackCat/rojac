package org.xblackcat.rojac.gui.view;

import org.xblackcat.rojac.gui.IRootPane;
import org.xblackcat.rojac.service.datahandler.IPacket;
import org.xblackcat.rojac.service.datahandler.IPacketProcessor;

import javax.swing.*;

/**
 * @author xBlackCat
 */

public class FavoritesView extends AView {
    public FavoritesView(IRootPane rootPane) {
        super(null, rootPane);

        add(new JScrollPane(new JTable(10, 2)));
    }

    @Override
    protected IPacketProcessor<IPacket>[] getProcessors() {
        return null;
    }
}
