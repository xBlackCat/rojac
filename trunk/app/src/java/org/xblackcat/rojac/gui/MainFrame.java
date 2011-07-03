package org.xblackcat.rojac.gui;

import net.infonode.docking.*;
import net.infonode.docking.drop.DropFilter;
import net.infonode.docking.drop.DropInfo;
import net.infonode.docking.properties.DockingWindowProperties;
import net.infonode.docking.properties.RootWindowProperties;
import net.infonode.docking.properties.TabWindowProperties;
import net.infonode.docking.title.DockingWindowTitleProvider;
import net.infonode.docking.title.SimpleDockingWindowTitleProvider;
import net.infonode.docking.util.StringViewMap;
import net.infonode.docking.util.WindowMenuUtil;
import net.infonode.gui.UIManagerUtil;
import net.infonode.util.Direction;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xblackcat.rojac.RojacDebugException;
import org.xblackcat.rojac.gui.component.AButtonAction;
import org.xblackcat.rojac.gui.component.ShortCut;
import org.xblackcat.rojac.gui.component.TrimingDockingWindowTitleProvider;
import org.xblackcat.rojac.gui.dialog.EditMessageDialog;
import org.xblackcat.rojac.gui.dialog.LoadMessageDialog;
import org.xblackcat.rojac.gui.dialog.OpenMessageDialog;
import org.xblackcat.rojac.gui.dialog.ProgressTrackerDialog;
import org.xblackcat.rojac.gui.view.MessageChecker;
import org.xblackcat.rojac.gui.view.ViewId;
import org.xblackcat.rojac.gui.view.factory.ViewHelper;
import org.xblackcat.rojac.gui.view.favorites.FavoritesView;
import org.xblackcat.rojac.gui.view.forumlist.ForumsListView;
import org.xblackcat.rojac.gui.view.recenttopics.RecentTopicsView;
import org.xblackcat.rojac.i18n.JLOptionPane;
import org.xblackcat.rojac.i18n.LocaleControl;
import org.xblackcat.rojac.i18n.Message;
import org.xblackcat.rojac.service.ServiceFactory;
import org.xblackcat.rojac.service.datahandler.*;
import org.xblackcat.rojac.service.executor.IExecutor;
import org.xblackcat.rojac.service.janus.commands.ASwingThreadedHandler;
import org.xblackcat.rojac.service.janus.commands.Request;
import org.xblackcat.rojac.service.progress.IProgressController;
import org.xblackcat.rojac.service.storage.IForumAH;
import org.xblackcat.rojac.service.storage.IMiscAH;
import org.xblackcat.rojac.service.storage.StorageException;
import org.xblackcat.rojac.util.*;
import org.xblackcat.utils.ResourceUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static org.xblackcat.rojac.service.options.Property.*;

/**
 * @author xBlackCat
 */

public class MainFrame extends JFrame implements IStatefull, IAppControl, IDataHandler {
    private static final Log log = LogFactory.getLog(MainFrame.class);
    private static final String SCHEDULED_TASK_ID = "SCHEDULED_SYNCHRONIZER";

    // Data tracking
    private Map<ViewId, View> openedViews = new HashMap<ViewId, View>();
    protected RootWindow threadsRootWindow;

    private static final String FORUMS_VIEW_ID = "forums_view";
    private static final String FAVORITES_VIEW_ID = "favorites_view";
    private static final String RECENT_TOPICS_VIEW_ID = "lastPosts_view";
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
        public void stateChanged(IView source, IState newState) {
            history.addHistoryItem(new NavigationHistoryItem(source.getId(), newState));

            // Update navigation buttons.
            updateNavigationButtons();
        }
    };

    private RootWindow rootWindow;
    private JButton navigationBackButton;
    private JButton navigationForwardButton;
    private ProgressComponent progressInToolbar;

    private final PacketDispatcher mainDispatcher = new PacketDispatcher(
            new IPacketProcessor<OptionsUpdatedPacket>() {
                @Override
                public void process(OptionsUpdatedPacket p) {
                    if (p.isPropertyAffected(VIEW_THREAD_TAB_TITLE_LIMIT)) {
                        // Update title providers for all message tabs

                        DockingWindowTitleProvider newTitleProvider = getTabTitleProvider();

                        updateTitleProvider(newTitleProvider, threadsRootWindow);
                    }

                    // Load changed properties.
                    if (p.isPropertyAffected(ROJAC_GUI_LOOK_AND_FEEL)) {
                        LookAndFeel laf = ROJAC_GUI_LOOK_AND_FEEL.get();
                        try {
                            UIUtils.setLookAndFeel(laf);
                        } catch (UnsupportedLookAndFeelException e) {
                            log.warn("Can not initialize " + laf.getName() + " L&F.", e);
                        }
                    }

                    if (p.isPropertyAffected(ROJAC_GUI_LOCALE)) {
                        LocaleControl.getInstance().setLocale(ROJAC_GUI_LOCALE.get());
                    }

                    if (p.isPropertyAffected(SYNCHRONIZER_SCHEDULE_PERIOD)) {
                        setScheduleSynchronizer();
                    }

                }

                private void updateTitleProvider(DockingWindowTitleProvider newTitleProvider, DockingWindow view) {
                    view.getWindowProperties().setTitleProvider(newTitleProvider);

                    int i = 0;
                    while (i < view.getChildWindowCount()) {
                        updateTitleProvider(newTitleProvider, view.getChildWindow(i++));
                    }
                }
            }
    );
    private MainFrameState frameState;

    public MainFrame() {
        super(RojacUtils.VERSION_STRING);

        setIconImage(ResourceUtils.loadImage("images/rojac-icon.png"));

        ProgressTrackerDialog progressTrackerDialog = new ProgressTrackerDialog(this);
        progressInToolbar = new ProgressComponent(progressTrackerDialog);

        initialize();

        // Setup progress dialog.
        final IProgressController progressControl = ServiceFactory.getInstance().getProgressControl();
        progressControl.addProgressListener(progressTrackerDialog);
        progressControl.addProgressListener(progressInToolbar);
        WindowsUtils.center(progressTrackerDialog, this);


        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

        // Default position/size
        setSize(800, 600);

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

    private void extraMessagesDialog(Integer id, final OpenMessageMethod method) {
        LoadMessageDialog lmd = new LoadMessageDialog(this, id);
        final Integer messageId = lmd.readMessageId();

        if (messageId != null) {
            final boolean loadAtOnce = lmd.isLoadAtOnce();

            new RojacWorker<Void, Void>() {
                @Override
                protected Void perform() throws Exception {
                    try {
                        IMiscAH s = ServiceFactory.getInstance().getStorage().getMiscAH();

                        s.storeExtraMessage(messageId);
                    } catch (StorageException e) {
                        log.error("Can not store extra message #" + messageId, e);
                        RojacUtils.showExceptionDialog(e);
                    }

                    return null;
                }

                @SuppressWarnings({"unchecked"})
                @Override
                protected void done() {
                    if (loadAtOnce) {
                        if (method != null) {
                            // Open a message after loading.
                            Request.EXTRA_MESSAGES.process(MainFrame.this,
                                    Request.PACKET_HANDLER,
                                    new ASwingThreadedHandler<IPacket>() {
                                        @Override
                                        protected void execute(IPacket data) {
                                            SwingUtilities.invokeLater(new Runnable() {
                                                public void run() {
                                                    openMessage(messageId, method);
                                                }
                                            });
                                        }
                                    });
                        } else {
                            Request.EXTRA_MESSAGES.process(MainFrame.this, Request.PACKET_HANDLER);
                        }
                    }
                }
            }.execute();
        }
    }

    public void setupScheduler() {
        // Setup scheduled synchronizer
        setScheduleSynchronizer();


        // Start synchronization if needed
        if (SYNCHRONIZER_SCHEDULE_AT_START.get()) {
            startSynchronization();
        }
    }

    private void initialize() {
        final ForumsListView forumsListView = new ForumsListView(this);
        final FavoritesView favoritesView = new FavoritesView(this);
        final RecentTopicsView recentTopicsView = new RecentTopicsView(this);

        JPanel cp = new JPanel(new BorderLayout());
        setContentPane(cp);

        // Setup forums view
        View viewForums = createView(forumsListView);

        // Setup favorites view
        View viewFavorites = createView(favoritesView);

        View viewRecentTopics = createView(recentTopicsView);

        // Set up main tabbed window for forum views
        threadsRootWindow = new RootWindow(false, new ThreadViewSerializer());
        threadsRootWindow.addListener(new CloseViewTabListener());

        Color backgroundColor = UIManagerUtil.getColor("Panel.background", "control");

        RootWindowProperties threadsRootWindowProperties = threadsRootWindow.getRootWindowProperties();
        threadsRootWindowProperties.setDragRectangleBorderWidth(2);
        threadsRootWindowProperties.setRecursiveTabsEnabled(false);
        threadsRootWindowProperties.getWindowAreaProperties().setBackgroundColor(backgroundColor);

        threadsRootWindowProperties
                .getDockingWindowProperties()
                .getDropFilterProperties()
                .setInsertTabDropFilter(noAuxViewsFilter)
                .setInteriorDropFilter(noAuxViewsFilter)
                .setChildDropFilter(noAuxViewsFilter)
                .setSplitDropFilter(noAuxViewsFilter);

        TabWindowProperties threadsTabWindowProperties = threadsRootWindowProperties.getTabWindowProperties();
        threadsTabWindowProperties.getMinimizeButtonProperties().setVisible(false);
        threadsTabWindowProperties.getMaximizeButtonProperties().setVisible(false);
//        threadsTabWindowProperties.getUndockButtonProperties().setVisible(false);
//        threadsTabWindowProperties.getDockButtonProperties().setVisible(false);

        View threadsView = createThreadsView(threadsRootWindow);
        View[] mainViews = new View[]{
                viewForums,
                viewFavorites,
                viewRecentTopics
        };

        StringViewMap viewMap = new StringViewMap();
        viewMap.addView(FORUMS_VIEW_ID, viewForums);
        viewMap.addView(FAVORITES_VIEW_ID, viewFavorites);
        viewMap.addView(RECENT_TOPICS_VIEW_ID, viewRecentTopics);
        viewMap.addView(THREADS_VIEW_ID, threadsView);

        rootWindow = new RootWindow(false, viewMap,
                new SplitWindow(
                        true,
                        0.75f,
                        threadsView,
                        new SplitWindow(
                                false,
                                0.25f,
                                viewRecentTopics,
                                new SplitWindow(
                                        false,
                                        0.7f,
                                        viewForums,
                                        viewFavorites
                                )
                        )
                )
        );

        rootWindow.getWindowProperties().setCloseEnabled(false);
        rootWindow.getWindowProperties().setMaximizeEnabled(false);

        RootWindowProperties rootWindowProperties = rootWindow.getRootWindowProperties();

        rootWindowProperties.getDockingWindowProperties().setCloseEnabled(false);
        rootWindowProperties.getDockingWindowProperties().setUndockEnabled(false);
        rootWindowProperties.getWindowAreaProperties().setBackgroundColor(backgroundColor);

        TabWindowProperties tabWindowProperties = rootWindowProperties.getTabWindowProperties();
        tabWindowProperties.getMaximizeButtonProperties().setVisible(false);
        tabWindowProperties.getMinimizeButtonProperties().setVisible(false);
        tabWindowProperties.getCloseButtonProperties().setVisible(false);
        tabWindowProperties.getUndockButtonProperties().setVisible(false);
        tabWindowProperties.getDockButtonProperties().setVisible(false);

        rootWindow.getWindowBar(Direction.LEFT).setEnabled(true);
        rootWindow.getWindowBar(Direction.LEFT).getWindowBarProperties().setMinimumWidth(5);
        rootWindow.getWindowBar(Direction.RIGHT).setEnabled(true);
        rootWindow.getWindowBar(Direction.RIGHT).getWindowBarProperties().setMinimumWidth(5);
        rootWindow.getWindowBar(Direction.DOWN).setEnabled(true);
        rootWindow.getWindowBar(Direction.DOWN).getWindowBarProperties().setMinimumWidth(5);

        rootWindowProperties.setDragRectangleBorderWidth(2);
        rootWindowProperties.setRecursiveTabsEnabled(false);

        for (View view : mainViews) {
            rootWindow.getWindowBar(Direction.RIGHT).addTab(view);
            view.restore();
        }

        rootWindow.setPopupMenuFactory(WindowMenuUtil.createWindowMenuFactory(viewMap, true));

        cp.add(rootWindow);
    }

    private View createThreadsView(DockingWindow threads) {
        JPanel threadsPane = new JPanel(new BorderLayout(0, 0));

        // Setup toolbar
        navigationBackButton = WindowsUtils.registerImageButton(threadsPane, "nav_back", new GoBackAction());
        navigationForwardButton = WindowsUtils.registerImageButton(threadsPane, "nav_forward", new GoForwardAction());

        JButton goToMessageButton = WindowsUtils.registerImageButton(threadsPane, "goto_message", new GoToMessageAction());

        JButton updateButton = WindowsUtils.registerImageButton(threadsPane, "update", new SynchronizationAction());
        JButton loadMessageButton = WindowsUtils.registerImageButton(threadsPane, "extramessage", new LoadExtraMessagesAction());
        JButton subscribeButton = WindowsUtils.registerImageButton(threadsPane, "forum_manage", new SubscribeForum());
        JButton settingsButton = WindowsUtils.registerImageButton(threadsPane, "settings", new SettingsAction());
        JButton aboutButton = WindowsUtils.registerImageButton(threadsPane, "about", new AboutAction());

        final JToolBar toolBar = WindowsUtils.createToolBar(
                navigationBackButton,
                navigationForwardButton,
                goToMessageButton,
                null,
                loadMessageButton,
                updateButton,
                progressInToolbar,
                null,
                subscribeButton,
                settingsButton,
                null,
                aboutButton
        );

        toolBar.addPropertyChangeListener("orientation", new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                progressInToolbar.setOrientation(toolBar.getOrientation());
            }
        });

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

    private View createView(IView itemView) {
        ServiceFactory.getInstance().getDataDispatcher().addDataHandler(itemView);

        final View view = new View(
                itemView.getTabTitle(),
                itemView.getTabTitleIcon(),
                itemView.getComponent()
        );

        DockingWindowProperties props = view.getWindowProperties();
        props.getTabProperties().getTitledTabProperties().getNormalProperties().setIconTextGap(3);
        props.setCloseEnabled(false);
        props.setMaximizeEnabled(false);

        view.setPopupMenuFactory(new ItemViewPopupFactory(itemView));

        return view;
    }

    private View makeViewWindow(final IItemView itemView) {
        final View view = new View(
                itemView.getTabTitle(),
                itemView.getTabTitleIcon(),
                itemView.getComponent()
        );

        itemView.addStateChangeListener(navigationListener);

        ShortCutUtils.mergeInputMaps(view, itemView.getComponent());

        DockingWindowProperties props = view.getWindowProperties();
        props.setMinimizeEnabled(false);
        props.setTitleProvider(getTabTitleProvider());

        itemView.addInfoChangeListener(new TitleChangeTracker(itemView, view));

        view.setPopupMenuFactory(new ItemViewPopupFactory(itemView));

        openedViews.put(itemView.getId(), view);
        return view;
    }

    private DockingWindowTitleProvider getTabTitleProvider() {
        int tabTitleLimit = VIEW_THREAD_TAB_TITLE_LIMIT.get(0);

        if (tabTitleLimit > 0) {
            return new TrimingDockingWindowTitleProvider(tabTitleLimit);
        } else {
            return SimpleDockingWindowTitleProvider.INSTANCE;
        }
    }

    /**
     * Method for delegating changes to all sub containers.
     *
     * @param packet packet to be dispatched
     */
    @Override
    public void processPacket(IPacket packet) {
        mainDispatcher.dispatch(packet);

        for (View v : openedViews.values()) {
            ((IView) v.getComponent()).processPacket(packet);
        }
    }

    @Override
    public IState getObjectState() {
        return frameState;
    }

    @Override
    public void setObjectState(IState state) {
        if (state instanceof MainFrameState) {
            MainFrameState frameState = (MainFrameState) state;

            setLocation(frameState.getLocation());
            setSize(frameState.getSize());
            setExtendedState(frameState.getWindowState());
        }
    }

    public void applySettings() {
        File file = RojacUtils.getLayoutFile();
        if (file.isFile()) {
            if (log.isInfoEnabled()) {
                log.info("Load previous layout");
            }
            try {
                ObjectInputStream in = new ObjectInputStream(new BufferedInputStream(new FileInputStream(file)));
                try {
                    rootWindow.read(in, false);
                    threadsRootWindow.read(in, false);

                    setObjectState((IState) in.readObject());
                } finally {
                    in.close();
                }
            } catch (ClassNotFoundException e) {
                log.error("Main frame state class is not found", e);
            } catch (IOException e) {
                log.error("Can not load views layout.", e);
            }
        } else {
            if (log.isInfoEnabled()) {
                log.info("No previous layout is found - use default.");
            }
        }
    }

    public void storeSettings() {
        File file = RojacUtils.getLayoutFile();
        try {
            ObjectOutputStream out = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(file)));
            try {
                rootWindow.write(out, false);
                threadsRootWindow.write(out, false);
                // Last item - main frame state
                out.writeObject(getObjectState());
            } finally {
                out.close();
            }
        } catch (IOException e) {
            log.error("Can not store views layout.", e);
        }
    }

    private void storeWindowState() {
        int state = getExtendedState();

        if (state == NORMAL || frameState == null) {
            frameState = new MainFrameState(state, getLocation(), getSize());
        } else {
            frameState = frameState.changeState(state);
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
    public void openMessage(int messageId, OpenMessageMethod openMessageMethod) {
        if (openMessageMethod == null) {
            throw new RojacDebugException("Open message type can not be null");
        }

        Iterator<View> iterator = openedViews.values().iterator();
        while (iterator.hasNext()) {
            View v = iterator.next();
            if (v.getComponent() instanceof IItemView) {
                if (v.getParent() == null) {
                    iterator.remove();
                    continue;
                }

                IItemView itemView = (IItemView) v.getComponent();

                if (openMessageMethod.getAssociatedViewType() == itemView.getId().getType()) {
                    if (itemView.containsItem(messageId)) {
                        // Item found in the view.
                        // Make the view visible and open it.
                        v.makeVisible();
                        itemView.makeVisible(messageId);
                        return;
                    }
                }
            }
        }

        new MessageNavigator(messageId, openMessageMethod).execute();
    }

    private void goToView(ViewId id, IState state) {
        View v = openTab(id);

        if (state != null) {
            ((IStatefull) v.getComponent()).setObjectState(state);
        }
    }

    private void updateNavigationButtons() {
        navigationBackButton.setEnabled(history.canGoBack());
        navigationForwardButton.setEnabled(history.canGoForward());
    }

    private void setScheduleSynchronizer() {
        Integer period = SYNCHRONIZER_SCHEDULE_PERIOD.get();

        IExecutor executor = ServiceFactory.getInstance().getExecutor();
        executor.killTimer(SCHEDULED_TASK_ID);

        if (period <= 0) {
            return;
        }

        // Convert minutes into seconds
        period *= 60;

        executor.setupPeriodicTask(SCHEDULED_TASK_ID, new ScheduleSynchronization(), period);
    }

    private void startSynchronization() {
        new RojacWorker<Void, Boolean>() {
            @Override
            protected Void perform() throws Exception {
                IForumAH forumAH = ServiceFactory.getInstance().getStorage().getForumAH();

                int[] subscribedForumIds = forumAH.getSubscribedForumIds();
                publish(!ArrayUtils.isEmpty(subscribedForumIds));
                return null;
            }

            @Override
            protected void process(List<Boolean> chunks) {
                boolean canProcess = chunks.iterator().next();

                if (canProcess) {
                    Request requests =
                            SYNCHRONIZER_LOAD_USERS.get() ?
                                    Request.SYNCHRONIZE_WITH_USERS :
                                    Request.SYNCHRONIZE;

                    requests.process(MainFrame.this);
                } else {
                    int response = JLOptionPane.showConfirmDialog(
                            MainFrame.this,
                            Message.WarnDialog_NothingToSync_Question.get(),
                            Message.WarnDialog_NothingToSync_Title.get(),
                            JOptionPane.YES_NO_OPTION
                    );
                    if (response == JOptionPane.YES_OPTION) {
                        DialogHelper.openForumSubscriptionDialog(MainFrame.this, new Runnable() {
                            @Override
                            public void run() {
                                assert RojacUtils.checkThread(true);

                                // Check again for forum changes
                                startSynchronization();
                            }
                        });
                    }
                }
            }
        }.execute();
    }

    private class ScheduleSynchronization implements Runnable {
        @Override
        public void run() {
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    startSynchronization();
                }
            });
        }
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
        }

        @SuppressWarnings({"SuspiciousMethodCalls"})
        private void unregisterWindow(DockingWindow dw) {
            if (dw == null) {
                return;
            }

            if (openedViews.containsValue(dw)) {
                openedViews.values().remove(dw);
            }

            int views = dw.getChildWindowCount();

            for (int i = 0; i < views; i++) {
                DockingWindow w = dw.getChildWindow(i);

                unregisterWindow(w);
            }
        }
    }

    private class MessageNavigator extends MessageChecker {
        private final OpenMessageMethod openMessageMethod;

        public MessageNavigator(int messageId, OpenMessageMethod openMessageMethod) {
            super(messageId);
            if (openMessageMethod == null) {
                throw new RojacDebugException("Open message method can not be null");
            }

            this.openMessageMethod = openMessageMethod;
        }

        @Override
        protected void done() {
            if (data == null) {
                extraMessagesDialog(messageId, openMessageMethod);
                return;
            }

            ViewId viewId = openMessageMethod.getAssociatedViewType().makeId(data);

            View c = openTab(viewId);
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
            super(ShortCut.About);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            DialogHelper.showAboutDialog(MainFrame.this);
        }
    }

    private class SettingsAction extends AButtonAction {
        public SettingsAction() {
            super(ShortCut.Settings);
        }

        public void actionPerformed(ActionEvent e) {
            DialogHelper.showOptionsDialog(MainFrame.this);
        }
    }

    private class LoadExtraMessagesAction extends AButtonAction {
        public LoadExtraMessagesAction() {
            super(ShortCut.LoadExtraMessages);
        }

        public void actionPerformed(ActionEvent e) {
            extraMessagesDialog(null, null);
        }
    }

    private class GoToMessageAction extends AButtonAction {
        public GoToMessageAction() {
            super(ShortCut.GoToMessage);
        }

        public void actionPerformed(ActionEvent e) {
            OpenMessageDialog omd = new OpenMessageDialog(MainFrame.this);
            Integer messageId = omd.readMessageId();
            if (messageId != null) {
                openMessage(messageId, omd.getOpenMethod());
            }
        }
    }

    private class SynchronizationAction extends AButtonAction {
        public SynchronizationAction() {
            super(ShortCut.Synchronization);
        }

        public void actionPerformed(ActionEvent e) {
            startSynchronization();
        }
    }

    private class GoBackAction extends AButtonAction {
        private GoBackAction() {
            super(ShortCut.GoBack);
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
            super(ShortCut.GoForward);
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
            super(ShortCut.ForumManage);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            DialogHelper.openForumSubscriptionDialog(MainFrame.this);
        }
    }
}
