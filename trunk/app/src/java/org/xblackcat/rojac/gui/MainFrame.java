package org.xblackcat.rojac.gui;

import net.infonode.docking.*;
import net.infonode.docking.drop.DropFilter;
import net.infonode.docking.drop.DropInfo;
import net.infonode.docking.properties.DockingWindowProperties;
import net.infonode.docking.title.SimpleDockingWindowTitleProvider;
import net.infonode.docking.util.StringViewMap;
import net.infonode.docking.util.WindowMenuUtil;
import net.infonode.util.Direction;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xblackcat.rojac.gui.component.AButtonAction;
import org.xblackcat.rojac.gui.component.ShortCut;
import org.xblackcat.rojac.gui.dialog.EditMessageDialog;
import org.xblackcat.rojac.gui.dialog.subscribtion.SubscriptionDialog;
import org.xblackcat.rojac.gui.view.MessageChecker;
import org.xblackcat.rojac.gui.view.ViewHelper;
import org.xblackcat.rojac.gui.view.ViewId;
import org.xblackcat.rojac.gui.view.ViewType;
import org.xblackcat.rojac.gui.view.favorites.FavoritesView;
import org.xblackcat.rojac.gui.view.forumlist.ForumsListView;
import org.xblackcat.rojac.i18n.Messages;
import org.xblackcat.rojac.service.datahandler.IDataHandler;
import org.xblackcat.rojac.service.datahandler.IPacket;
import org.xblackcat.rojac.util.*;
import org.xblackcat.utils.ResourceUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

import static org.xblackcat.rojac.service.options.Property.*;

/**
 * @author xBlackCat
 */

public class MainFrame extends JFrame implements IConfigurable, IAppControl, IDataHandler {
    private static final Log log = LogFactory.getLog(MainFrame.class);

    private IView forumsListView;
    private IView favoritesView;

    // Data tracking
    private Map<ViewId, View> openedViews = new HashMap<ViewId, View>();
    protected RootWindow threadsRootWindow;

    private static final String FORUMS_VIEW_ID = "forums_view";
    private static final String FAVORITES_VIEW_ID = "favorites_view";
    private static final String THREADS_VIEW_ID = "threads_view_id";

    protected final DropFilter noAuxViewsFilter = new DropFilter() {
        @Override
        public boolean acceptDrop(DropInfo dropInfo) {
            DockingWindow dw = dropInfo.getWindow();

            if (dw instanceof View) {
                View v = (View) dw;
                if (v.getComponent() instanceof IView) {
                    return dropInfo.getDropWindow().getRootWindow() == threadsRootWindow;
                }
            }

            return true;
        }
    };

    protected final NavigationHistoryTracker history = new NavigationHistoryTracker();
    protected final IStateListener navigationListener = new IStateListener() {
        @Override
        public void stateChanged(IView source, IViewState newState) {
            history.addHistoryItem(new NavigationHistoryItem(source.getId(), newState));

            // Update navigation buttons.
            updateNavigationButtons();
        }
    };

    private RootWindow rootWindow;
    private JButton navigationBackButton;
    private JButton navigationForwardButton;

    public MainFrame() {
        super(RojacUtils.VERSION_STRING);

        setIconImage(ResourceUtils.loadImage("images/rojac-icon.png"));

        forumsListView = new ForumsListView(this);
        favoritesView = new FavoritesView(this);

        initialize();

        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

        // Default position/size
        setSize(640, 480);

        WindowsUtils.centerOnScreen(this);

        addWindowStateListener(new WindowStateListener() {
            @Override
            public void windowStateChanged(WindowEvent e) {
                storeWindowState();
            }
        });

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                storeWindowState();
            }

            @Override
            public void componentMoved(ComponentEvent e) {
                storeWindowState();
            }
        });
    }

    public void loadData() {
        File file = RojacUtils.getLayoutFile();
        if (file.isFile()) {
            if (log.isInfoEnabled()) {
                log.info("Load previous layout");
            }
            try {
                ObjectInputStream in = new ObjectInputStream(new BufferedInputStream(new FileInputStream(file)));
                try {
                    rootWindow.read(in);
                    threadsRootWindow.read(in);
                } finally {
                    in.close();
                }
            } catch (IOException e) {
                log.error("Can not load views layout.", e);
            }
        } else {
            if (log.isInfoEnabled()) {
                log.info("No previous layout is found - use default.");
            }
        }
    }

    private void initialize() {
        JPanel cp = new JPanel(new BorderLayout());
        setContentPane(cp);

        // Setup forums view
        View viewForums = createView(
                Messages.View_Forums_Title,
                forumsListView
        );

        // Setup favorites view
        View viewFavorites = createView(
                Messages.View_Favorites_Title,
                favoritesView
        );


        // Set up main tabbed window for forum views
        TabWindow threads = new TabWindow();
        threads.getWindowProperties().setUndockEnabled(false);
        threads.getWindowProperties().setMinimizeEnabled(false);

        threadsRootWindow = new RootWindow(false, new ThreadViewSerializer(), threads);
        threadsRootWindow.getRootWindowProperties().setDragRectangleBorderWidth(2);
        threadsRootWindow.getRootWindowProperties().setRecursiveTabsEnabled(false);
        threadsRootWindow.addListener(new CloseViewTabListener());

        threadsRootWindow
                .getRootWindowProperties()
                .getDockingWindowProperties()
                .getDropFilterProperties()
                .setInsertTabDropFilter(noAuxViewsFilter)
                .setInteriorDropFilter(noAuxViewsFilter)
                .setChildDropFilter(noAuxViewsFilter)
                .setSplitDropFilter(noAuxViewsFilter);

        View threadsView = createThreadsView(threadsRootWindow);
        View[] mainViews = new View[]{
                viewForums,
                viewFavorites
        };
        StringViewMap viewMap = new StringViewMap();
        viewMap.addView(FORUMS_VIEW_ID, viewForums);
        viewMap.addView(FAVORITES_VIEW_ID, viewFavorites);
        viewMap.addView(THREADS_VIEW_ID, threadsView);

        rootWindow = new RootWindow(false, viewMap, new SplitWindow(true, 0.25f, new TabWindow(mainViews), threadsView));
        rootWindow.getWindowBar(Direction.LEFT).setEnabled(true);
        rootWindow.getWindowBar(Direction.LEFT).addTab(viewForums, 0);
        rootWindow.getWindowBar(Direction.LEFT).addTab(viewFavorites, 1);
        rootWindow.getWindowBar(Direction.LEFT).getWindowBarProperties().setMinimumWidth(5);
        rootWindow.getWindowBar(Direction.RIGHT).setEnabled(true);
        rootWindow.getWindowBar(Direction.RIGHT).getWindowBarProperties().setMinimumWidth(5);
        rootWindow.getWindowBar(Direction.DOWN).setEnabled(true);
        rootWindow.getWindowBar(Direction.DOWN).getWindowBarProperties().setMinimumWidth(5);

        rootWindow.getRootWindowProperties().setDragRectangleBorderWidth(2);
        rootWindow.getRootWindowProperties().setRecursiveTabsEnabled(false);

        viewForums.restore();

        rootWindow.setPopupMenuFactory(WindowMenuUtil.createWindowMenuFactory(viewMap, true));

        cp.add(rootWindow);
    }

    private View createThreadsView(DockingWindow threads) {
        JPanel threadsPane = new JPanel(new BorderLayout(0, 0));

        // Setup toolbar
        navigationBackButton = WindowsUtils.registerImageButton(threadsPane, "nav_back", new GoBackAction());
        navigationForwardButton = WindowsUtils.registerImageButton(threadsPane, "nav_forward", new GoForwardAction());

        JButton updateButton = WindowsUtils.registerImageButton(threadsPane, "update", new SynchronizationAction());
        JButton loadMessageButton = WindowsUtils.registerImageButton(threadsPane, "extramessage", new LoadExtraMessagesAction());
        JButton subscribeButton = WindowsUtils.registerImageButton(threadsPane, "forum_manage", new SubscribeForum());
        JButton settingsButton = WindowsUtils.registerImageButton(threadsPane, "settings", new SettingsAction());
        JButton aboutButton = WindowsUtils.registerImageButton(threadsPane, "about", new AboutAction());

        JToolBar toolBar = WindowsUtils.createToolBar(
                navigationBackButton,
                navigationForwardButton,
                null,
                updateButton,
                loadMessageButton,
                null,
                subscribeButton,
                settingsButton,
                null,
                aboutButton
        );

        threadsPane.add(toolBar, BorderLayout.NORTH);
        threadsPane.add(threads, BorderLayout.CENTER);

        View view = new View(null, null, threadsPane);

        DockingWindowProperties props = view.getWindowProperties();
        props.setCloseEnabled(false);
        props.setMaximizeEnabled(false);
        props.setMinimizeEnabled(false);
        props.setUndockEnabled(false);
        props.setDragEnabled(false);
        props.setDockEnabled(false);

        view.getViewProperties().setAlwaysShowTitle(false);

        view.addListener(new CloseViewTabListener());

        updateNavigationButtons();

        return view;
    }

    private View createView(Messages title, IView comp) {
        final View view = new View(
                title.get(),
                null,
                comp.getComponent()
        );

        DockingWindowProperties props = view.getWindowProperties();
        props.setCloseEnabled(false);
        props.setMaximizeEnabled(false);
        props.setUndockEnabled(false);

        return view;
    }

    private View makeViewWindow(final IItemView itemView) {
        final View view = new View(
                "#" + itemView.getId().getAnchor(), // Default view name
                itemView.getTabTitleIcon(),
                itemView.getComponent()
        );

        itemView.addStateChangeListener(navigationListener);

        ShortCutUtils.mergeInputMaps(view, itemView.getComponent());

        DockingWindowProperties props = view.getWindowProperties();
        props.setMinimizeEnabled(false);
        props.setTitleProvider(SimpleDockingWindowTitleProvider.INSTANCE);
        props.setUndockEnabled(false);

        itemView.addActionListener(new TitleChangeTracker(itemView, view));

        view.setPopupMenuFactory(new ItemViewPopupFactory(itemView));

        openedViews.put(itemView.getId(), view);
        return view;
    }

    /**
     * Method for delegating changes to all sub containers.
     *
     * @param packet packet to be dispatched
     */
    @Override
    public void processPacket(IPacket packet) {
        forumsListView.processPacket(packet);
        favoritesView.processPacket(packet);

        for (View v : openedViews.values()) {
            ((IView) v.getComponent()).processPacket(packet);
        }
    }

    @Override
    public void applySettings() {
        if (ROJAC_MAIN_FRAME_POSITION.isSet()) {
            setLocation(ROJAC_MAIN_FRAME_POSITION.get());
        }

        if (ROJAC_MAIN_FRAME_SIZE.isSet()) {
            setSize(ROJAC_MAIN_FRAME_SIZE.get());
        }

        if (ROJAC_MAIN_FRAME_STATE.isSet()) {
            setExtendedState(ROJAC_MAIN_FRAME_STATE.get());
        }
    }

    @Override
    public void storeSettings() {
        storeWindowState();

        File file = RojacUtils.getLayoutFile();
        try {
            ObjectOutputStream out = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(file)));
            try {
                rootWindow.write(out);
                threadsRootWindow.write(out);
            } finally {
                out.close();
            }
        } catch (IOException e) {
            log.error("Can not store views layout.", e);
        }
    }

    private void storeWindowState() {
        int state = getExtendedState();

        ROJAC_MAIN_FRAME_STATE.set(state);
        if (state == NORMAL) {
            ROJAC_MAIN_FRAME_POSITION.set(getLocation());
            ROJAC_MAIN_FRAME_SIZE.set(getSize());
        }
    }

    @Override
    public View openTab(ViewId viewId) {
        View c = openedViews.get(viewId);
        if (c != null && c.getParent() != null) {
            c.makeVisible();
            return c;
        }

        IItemView v = ViewHelper.makeView(viewId, this);

        final View view = makeViewWindow(v);

        DockingWindow rootWindow = threadsRootWindow.getWindow();
        if (rootWindow != null) {
            if (rootWindow instanceof TabWindow) {
                ((TabWindow) rootWindow).addTab(view);
            } else {
                TabWindow tw = searchForTabWindow(rootWindow);
                if (tw != null) {
                    tw.addTab(view);
                } else {
                    rootWindow.split(view, Direction.RIGHT, 0.5f);
                }
            }
        } else {
            threadsRootWindow.setWindow(new TabWindow(view));
        }

        v.loadItem(viewId.getId());

        return view;
    }

    @Override
    public void closeTab(ViewId viewId) {
        if (openedViews.containsKey(viewId)) {
            openedViews.remove(viewId).close();
        }
    }

    private TabWindow searchForTabWindow(DockingWindow rootWindow) {
        TabWindow w = null;

        for (int i = 0; i < rootWindow.getChildWindowCount(); i++) {
            DockingWindow dw = rootWindow.getChildWindow(i);
            if (dw instanceof TabWindow) {
                w = (TabWindow) dw;
                break;
            }

            w = searchForTabWindow(dw);
            if (w != null) {
                break;
            }
        }

        return w;
    }

    @Override
    public Window getMainFrame() {
        return this;
    }

    @Override
    public void editMessage(Integer forumId, Integer messageId) {
        EditMessageDialog editDlg = new EditMessageDialog(this);

        if (forumId == null) {
            editDlg.editMessage(messageId);
        } else if (messageId == null) {
            editDlg.createTopic(forumId);
        } else {
            editDlg.answerOn(messageId);
        }
    }

    @Override
    public void openMessage(int messageId) {
        // Search thought existing data for the message first.
        for (View v : openedViews.values()) {
            if (v.getComponent() instanceof IItemView) {
                IItemView itemView = (IItemView) v.getComponent();

                if (itemView.containsItem(messageId)) {
                    // Item found in the view.
                    // Make the view visible and open it.
                    v.makeVisible();
                    itemView.makeVisible(messageId);
                    return;
                }
            }
        }

        new MessageNavigator(messageId).execute();
    }

    private void goToView(ViewId id, IViewState state) {
        View v = openTab(id);

        if (state != null) {
            ((IView) v.getComponent()).setState(state);
        }
    }

    private void updateNavigationButtons() {
        navigationBackButton.setEnabled(history.canGoBack());
        navigationForwardButton.setEnabled(history.canGoForward());
    }

    private class ThreadViewSerializer implements ViewSerializer {
        @Override
        public void writeView(View view, ObjectOutputStream out) throws IOException {
            ViewHelper.storeView(out, view);
        }

        @Override
        public View readView(ObjectInputStream in) throws IOException {
            try {
                IItemView v = ViewHelper.initializeView(in, MainFrame.this);

                View view = makeViewWindow(v);

                v.loadItem(v.getId().getId());

                return view;
            } catch (ClassNotFoundException e) {
                log.error("Can not obtain state object.", e);
                throw new IOException("Can not obtain state object.", e);
            }
        }
    }

    private class CloseViewTabListener extends DockingWindowAdapter {
        @Override
        public void windowClosed(DockingWindow window) {
            unregisterWindow(window);
            int views = window.getChildWindowCount();

            for (int i = 0; i < views; i++) {
                DockingWindow dw = window.getChildWindow(i);

                unregisterWindow(dw);
            }
        }

        private void unregisterWindow(DockingWindow dw) {
            if (openedViews.containsValue(dw)) {
                openedViews.values().remove(dw);
            }
        }
    }

    private class MessageNavigator extends MessageChecker {
        public MessageNavigator(int messageId) {
            super(messageId);
        }

        @Override
        protected void done() {
            if (data == null) {
                DialogHelper.extraMessagesDialog(MainFrame.this, messageId);
                return;
            }

            ViewId forumViewId = ViewType.Forum.makeId(data.getForumId());

            View c = openTab(forumViewId);
            if (c != null) {
                // Just in case :)
                c.makeVisible();
                IItemView itemView = (IItemView) c.getComponent();
                itemView.makeVisible(messageId);
            } else {
                DialogHelper.showExceptionDialog(new RuntimeException("F*cka-morgana! Forum view is not loaded!"));
            }
        }
    }

    private class AboutAction extends AButtonAction {
        public AboutAction() {
            super(Messages.MainFrame_Button_About, ShortCut.About);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            DialogHelper.showAboutDialog(MainFrame.this);
        }
    }

    private class SettingsAction extends AButtonAction {
        public SettingsAction() {
            super(Messages.MainFrame_Button_Settings, ShortCut.Settings);
        }

        public void actionPerformed(ActionEvent e) {
            DialogHelper.showOptionsDialog(MainFrame.this);
        }
    }

    private class LoadExtraMessagesAction extends AButtonAction {
        public LoadExtraMessagesAction() {
            super(Messages.MainFrame_Button_LoadMessage, ShortCut.LoadExtraMessages);
        }

        public void actionPerformed(ActionEvent e) {
            DialogHelper.extraMessagesDialog(MainFrame.this, null);
        }
    }

    private class SynchronizationAction extends AButtonAction {
        public SynchronizationAction() {
            super(Messages.MainFrame_Button_Update, ShortCut.Synchronization);
        }

        public void actionPerformed(ActionEvent e) {
            SynchronizationUtils.startSynchronization(MainFrame.this);
        }
    }

    private class GoBackAction extends AButtonAction {
        private GoBackAction() {
            super(Messages.MainFrame_Button_GoBack, ShortCut.GoBack);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (history.canGoBack()) {
                NavigationHistoryItem i = history.goBack();

                goToView(i.getViewId(), i.getState());
                updateNavigationButtons();
            }
        }
    }

    private class GoForwardAction extends AButtonAction {
        private GoForwardAction() {
            super(Messages.MainFrame_Button_GoForward, ShortCut.GoForward);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (history.canGoForward()) {
                NavigationHistoryItem i = history.goForward();

                goToView(i.getViewId(), i.getState());
                updateNavigationButtons();
            }
        }
    }

    private class SubscribeForum extends AButtonAction {
        public SubscribeForum() {
            super(Messages.MainFrame_Button_ForumManage, ShortCut.ForumManage);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            SubscriptionDialog dlg = new SubscriptionDialog(MainFrame.this);

            WindowsUtils.center(dlg, MainFrame.this);

            dlg.setVisible(true);
        }
    }
}
