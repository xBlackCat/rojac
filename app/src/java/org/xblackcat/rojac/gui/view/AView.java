package org.xblackcat.rojac.gui.view;

import org.xblackcat.rojac.gui.IRootPane;
import org.xblackcat.rojac.gui.IView;
import org.xblackcat.rojac.service.ServiceFactory;
import org.xblackcat.rojac.service.datahandler.IPacket;
import org.xblackcat.rojac.service.datahandler.IPacketProcessor;
import org.xblackcat.rojac.service.datahandler.PacketDispatcher;
import org.xblackcat.rojac.service.storage.IStorage;

import javax.swing.*;
import java.awt.*;

/**
 * @author xBlackCat
 */

public abstract class AView extends JPanel implements IView {
    protected final IStorage storage = ServiceFactory.getInstance().getStorage();
    private final PacketDispatcher packetDispatcher;

    protected final IRootPane mainFrame;

    protected AView(IRootPane rootPane) {
        super(new BorderLayout());
        this.mainFrame = rootPane;
        packetDispatcher = new PacketDispatcher(getProcessors());
    }

    protected abstract IPacketProcessor<? extends IPacket>[] getProcessors();

    public void applySettings() {
    }

    public void updateSettings() {
    }

    public final JComponent getComponent() {
        return this;
    }

    @Override
    public final void processPacket(IPacket packet) {
        packetDispatcher.dispatch(packet);
    }
}
