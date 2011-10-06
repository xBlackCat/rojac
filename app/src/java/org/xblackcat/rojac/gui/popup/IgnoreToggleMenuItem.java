package org.xblackcat.rojac.gui.popup;

import org.xblackcat.rojac.i18n.Message;
import org.xblackcat.rojac.service.datahandler.IgnoreUpdatedPacket;
import org.xblackcat.rojac.service.datahandler.ReloadDataPacket;
import org.xblackcat.rojac.service.options.Property;
import org.xblackcat.rojac.service.storage.IMiscAH;
import org.xblackcat.rojac.service.storage.Storage;
import org.xblackcat.rojac.util.RojacWorker;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * @author xBlackCat
 */
class IgnoreToggleMenuItem extends JMenuItem {
    public IgnoreToggleMenuItem(final int topicId, final boolean ignored) {
        setText(ignored ? Message.Popup_Ignore_Reset.get() : Message.Popup_Ignore_Set.get());
        addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new RojacWorker<Void, Void>() {
                    @Override
                    protected Void perform() throws Exception {
                        IMiscAH miscAH = Storage.get(IMiscAH.class);

                        if (ignored) {
                            miscAH.removeFromIgnoredTopicList(topicId);
                        } else {
                            miscAH.addToIgnoredTopicList(topicId);
                        }

                        publish();

                        return null;
                    }

                    @Override
                    protected void process(List<Void> chunks) {
                        if (Property.HIDE_IGNORED_TOPICS.get(false)) {
                            new ReloadDataPacket().dispatch();
                        } else {
                            new IgnoreUpdatedPacket(topicId, !ignored).dispatch();
                        }
                    }
                }.execute();
            }
        });
    }
}
