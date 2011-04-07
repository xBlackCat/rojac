package org.xblackcat.rojac.gui.dialog.subscribtion;

import org.apache.commons.logging.Log;
import org.xblackcat.rojac.data.Forum;
import org.xblackcat.rojac.service.ServiceFactory;
import org.xblackcat.rojac.service.storage.IStorage;
import org.xblackcat.rojac.service.storage.StorageException;
import org.xblackcat.rojac.util.RojacWorker;

import javax.swing.*;
import java.awt.*;
import java.util.*;

/**
 * @author xBlackCat
 */
public class SubscribtionDialog extends JDialog {
    private SubscribeForumModel model = new SubscribeForumModel();

    public SubscribtionDialog(Window owner) {
        super(owner);

        initializeLayout();
    }

    private void initializeLayout() {
        JPanel content = new JPanel(new BorderLayout(5, 5));

    }

}
