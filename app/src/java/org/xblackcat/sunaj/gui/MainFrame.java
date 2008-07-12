package org.xblackcat.sunaj.gui;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.flexdock.docking.DockingConstants;
import org.flexdock.docking.DockingManager;
import org.flexdock.view.View;
import org.flexdock.view.Viewport;
import org.xblackcat.sunaj.data.Forum;
import org.xblackcat.sunaj.gui.model.ForumListModel;
import org.xblackcat.sunaj.i18n.Messages;
import org.xblackcat.sunaj.service.ServiceFactory;
import org.xblackcat.sunaj.service.options.IOptionsService;
import org.xblackcat.sunaj.service.options.Property;
import org.xblackcat.sunaj.service.storage.IStorage;
import org.xblackcat.sunaj.service.storage.StorageException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Date: 23 груд 2007
 *
 * @author xBlackCat
 */

public class MainFrame extends JFrame {
    private static final Log log = LogFactory.getLog(MainFrame.class);

    // Data and models
    private ForumListModel forumsModel = new ForumListModel();

    // Components
    private JTabbedPane threads;
    private View viewForums;
    private View viewThreads;
    private View viewFavorites;

    public MainFrame() {
        super(Messages.MAIN_WINDOW_TITLE.getMessage());

        initialize();

        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                storeSettings();

                System.exit(0);
            }
        });

        setSize(500, 300);

        loadData();

        setupUserSettings();
    }

    private void setupUserSettings() {
        // TODO: Load user settings from options
    }

    private void loadData() {
        IStorage storage = ServiceFactory.getInstance().getStorage();

        try {
            Forum[] allForums = storage.getForumAH().getAllForums();
            forumsModel.setForums(allForums);
        } catch (StorageException e) {
            log.error("Can not initialize forum list", e);
        }
    }

    private void storeSettings() {
        IOptionsService os = ServiceFactory.getInstance().getOptionsService();

        if (!os.getProperty(Property.RSDN_USER_PASSWORD_SAVE)) {
            os.setProperty(Property.RSDN_USER_PASSWORD, null);
        }

        os.storeSettings();
    }

    private void initialize() {
        JPanel cp = new JPanel(new BorderLayout());
        setContentPane(cp);

        threads = new JTabbedPane();

        Viewport viewport = new Viewport();
        cp.add(viewport, BorderLayout.CENTER);

        // FlexDock initialization
        DockingManager.setFloatingEnabled(true);

        // Center of the window is used with tabbed pane.
        viewThreads = new View("threads_view", null, null);
        viewThreads.setTerritoryBlocked(DockingConstants.CENTER_REGION, true);
        viewThreads.setTitlebar(null);
        viewThreads.setContentPane(threads);

        viewport.dock(viewThreads);

        // Setup forums view
        viewForums = new View(
                "forums_view",
                Messages.VIEW_FORUMS_TITLE.getMessage(),
                Messages.VIEW_FORUMS_TAB_TEXT.getMessage()
        );
        viewForums.addAction(DockingConstants.CLOSE_ACTION);
        viewForums.addAction(DockingConstants.PIN_ACTION);
        viewForums.setContentPane(createForumsView());

        viewThreads.dock(viewForums, DockingConstants.WEST_REGION, 0.3f);

        // Setup favorites view
        viewFavorites = new View(
                "favorites_view",
                Messages.VIEW_FAVORITES_TITLE.getMessage(),
                Messages.VIEW_FAVORITES_TAB_TEXT.getMessage()
        );
        viewFavorites.addAction(DockingConstants.CLOSE_ACTION);
        viewFavorites.addAction(DockingConstants.PIN_ACTION);
        viewFavorites.setContentPane(new JScrollPane(new JTable()));

        viewThreads.dock(viewFavorites, DockingConstants.EAST_REGION, 0.3f);
    }

    private JComponent createForumsView() {
        JPanel forumsPane = new JPanel(new BorderLayout(2, 2));
        JList forums = new JList(forumsModel);
        forumsPane.add(new JScrollPane(forums));

        forums.setCellRenderer(new ForumCellRenderer());
        
        return forumsPane;
    }

    private static class ForumCellRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

            Forum f = (Forum) value;

            setText(f.getForumName());

            return this;
        }
    }

}
