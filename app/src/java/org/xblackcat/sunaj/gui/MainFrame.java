package org.xblackcat.sunaj.gui;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.flexdock.docking.DockingConstants;
import org.flexdock.docking.DockingManager;
import org.flexdock.util.SwingUtility;
import org.flexdock.view.View;
import org.flexdock.view.Viewport;
import org.xblackcat.sunaj.data.Forum;
import org.xblackcat.sunaj.gui.frame.message.MessagePane;
import org.xblackcat.sunaj.gui.frame.thread.ThreadDoubleView;
import org.xblackcat.sunaj.gui.frame.thread.TreeThreadView;
import org.xblackcat.sunaj.gui.view.FavoritesView;
import org.xblackcat.sunaj.gui.view.ForumsListView;
import org.xblackcat.sunaj.i18n.Messages;
import org.xblackcat.sunaj.service.ServiceFactory;
import org.xblackcat.sunaj.service.options.IOptionsService;
import org.xblackcat.sunaj.service.options.Property;

import javax.swing.*;
import java.awt.*;

/**
 * Date: 23 груд 2007
 *
 * @author xBlackCat
 */

public class MainFrame extends JFrame implements IConfigurable {
    private static final Log log = LogFactory.getLog(MainFrame.class);

    private IView forumsListView = new ForumsListView();
    private IView favoritesView = new FavoritesView();

    // Components
    private JTabbedPane threads;
    private View viewForums;
    private View viewThreads;
    private View viewFavorites;

    public MainFrame() {
        super(Messages.MAIN_WINDOW_TITLE.getMessage());

        initialize();

        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

        // Default position/size
        setSize(640, 480);

        SwingUtility.centerOnScreen(this);
    }

    public void loadData() {
        forumsListView.applySettings();
        favoritesView.applySettings();

        // For testing
        ThreadDoubleView $ = new ThreadDoubleView(new TreeThreadView(), new MessagePane(), false);
        threads.addTab("Test", $);
//        $.viewItem(2483908);// Biggest message
        $.viewItem(2484167);
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
        viewForums = createView(
                "forums_view",
                Messages.VIEW_FORUMS_TITLE,
                Messages.VIEW_FORUMS_TAB_TEXT,
                forumsListView.getComponent()
        );

        viewThreads.dock(viewForums, DockingConstants.WEST_REGION, 0.2f);

        // Setup favorites view
        viewFavorites = createView(
                "favorites_view",
                Messages.VIEW_FAVORITES_TITLE,
                Messages.VIEW_FAVORITES_TAB_TEXT,
                favoritesView.getComponent()
        );

        viewThreads.dock(viewFavorites, DockingConstants.EAST_REGION, 0.2f);
    }

    private View createView(String id, Messages title, Messages tabText, JComponent comp) {
        final View view = new View(
                id,
                title.getMessage(),
                tabText.getMessage()
        );
        view.addAction(DockingConstants.CLOSE_ACTION);
        view.addAction(DockingConstants.PIN_ACTION);
        view.setContentPane(comp);

        return view;
    }

    public void applySettings() {
        forumsListView.applySettings();
        favoritesView.applySettings();

        IOptionsService os = ServiceFactory.getInstance().getOptionsService();

        Point pos = os.getProperty(Property.SUNAJ_MAIN_FRAME_POSITION);
        if (pos != null) {
            setLocation(pos);
        }

        Dimension size = os.getProperty(Property.SUNAJ_MAIN_FRAME_SIZE);
        if (size != null) {
            setSize(size);
        }

        // TODO: implement
    }

    public void updateSettings() {
        forumsListView.updateSettings();
        favoritesView.updateSettings();

        IOptionsService os = ServiceFactory.getInstance().getOptionsService();

        os.setProperty(Property.SUNAJ_MAIN_FRAME_POSITION, getLocation());
        os.setProperty(Property.SUNAJ_MAIN_FRAME_SIZE, getSize());

        // TODO: implement
    }

    private void openForumTab(Forum f) {
        ThreadDoubleView $ = new ThreadDoubleView(new TreeThreadView(), new MessagePane(), false);
        threads.addTab(f.getForumName(), $);
        $.viewItem(f.getForumId());
    }
}
