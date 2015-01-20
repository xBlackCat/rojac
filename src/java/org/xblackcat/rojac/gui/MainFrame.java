package org.xblackcat.rojac.gui;

import bibliothek.gui.DockController;
import bibliothek.gui.DockFrontend;
import bibliothek.gui.DockStation;
import bibliothek.gui.Dockable;
import bibliothek.gui.dock.DefaultDockable;
import bibliothek.gui.dock.SplitDockStation;
import bibliothek.gui.dock.event.DockFrontendAdapter;
import bibliothek.gui.dock.event.VetoableDockFrontendEvent;
import bibliothek.gui.dock.event.VetoableDockFrontendListener;
import bibliothek.gui.dock.station.split.SplitDockProperty;
import bibliothek.gui.dock.themes.basic.BasicDockTitleFactory;
import bibliothek.gui.dock.title.DockTitleFactory;
import bibliothek.gui.dock.title.DockTitleManager;
import bibliothek.gui.dock.util.Priority;
import chrriis.dj.nativeswing.swtimpl.components.JWebBrowser;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xblackcat.rojac.RojacDebugException;
import org.xblackcat.rojac.gui.component.AButtonAction;
import org.xblackcat.rojac.gui.component.ShortCut;
import org.xblackcat.rojac.gui.component.TrimmedDockTitleFactory;
import org.xblackcat.rojac.gui.dialog.EditMessageDialog;
import org.xblackcat.rojac.gui.dialog.LoadMessageDialog;
import org.xblackcat.rojac.gui.dialog.OpenMessageDialog;
import org.xblackcat.rojac.gui.dialog.ProgressTrackerDialog;
import org.xblackcat.rojac.gui.hint.HintContainer;
import org.xblackcat.rojac.gui.tray.IStatisticListener;
import org.xblackcat.rojac.gui.view.MessageChecker;
import org.xblackcat.rojac.gui.view.ViewId;
import org.xblackcat.rojac.gui.view.ViewType;
import org.xblackcat.rojac.gui.view.factory.ViewHelper;
import org.xblackcat.rojac.gui.view.startpage.StartPageView;
import org.xblackcat.rojac.i18n.JLOptionPane;
import org.xblackcat.rojac.i18n.LocaleControl;
import org.xblackcat.rojac.i18n.Message;
import org.xblackcat.rojac.service.ServiceFactory;
import org.xblackcat.rojac.service.datahandler.*;
import org.xblackcat.rojac.service.executor.IExecutor;
import org.xblackcat.rojac.service.janus.commands.ASwingThreadedHandler;
import org.xblackcat.rojac.service.janus.commands.Request;
import org.xblackcat.rojac.service.options.Property;
import org.xblackcat.rojac.service.progress.IProgressController;
import org.xblackcat.rojac.service.storage.IForumAH;
import org.xblackcat.rojac.service.storage.IMiscAH;
import org.xblackcat.rojac.service.storage.Storage;
import org.xblackcat.rojac.util.*;
import org.xblackcat.sjpu.storage.StorageException;
import org.xblackcat.utils.ResourceUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.*;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static org.xblackcat.rojac.service.options.Property.*;

/**
 * @author xBlackCat
 */

public class MainFrame extends JFrame implements IStateful, IAppControl, IDataHandler {
    private static final Log log = LogFactory.getLog(MainFrame.class);
    private static final String SCHEDULED_TASK_ID = "SCHEDULED_SYNCHRONIZER";

    // Data tracking
    private Map<ViewId, DefaultDockable> openedViews = new HashMap<>();
    protected SplitDockStation threadsRootWindow;

    private final StartPageView startPageView = new StartPageView(this);

    protected final NavigationHistoryTracker history = new NavigationHistoryTracker();
    protected final IStateListener navigationListener = (viewId, newState) -> {
        if (newState.isNavigatable()) {
            history.addHistoryItem(new NavigationHistoryItem(viewId, newState));

            // Update navigation buttons.
            updateNavigationButtons();
        }
    };

    private JButton navigationBackButton;
    private JButton navigationForwardButton;
    private ProgressComponent progressInToolbar;

    private final PacketDispatcher mainDispatcher = new PacketDispatcher(
            new APacketProcessor<OptionsUpdatedPacket>() {
                @Override
                public void process(OptionsUpdatedPacket p) {
                    if (p.isPropertyAffected(VIEW_THREAD_TAB_TITLE_LIMIT)) {
                        // Update title providers for all message tabs

                        updateTitleProvider(getTabTitleProvider());
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

                    if (p.isPropertyAffected(HIDE_IGNORED_TOPICS)) {
                        new ReloadDataPacket().dispatch();
                    }
                }

                private void updateTitleProvider(DockTitleFactory newTitleProvider) {
                    final DockTitleManager titleManager = dockController.getDockTitleManager();
                    titleManager.clearThemeFactories();
                    titleManager.register("", newTitleProvider, Priority.DEFAULT);
                }
            }
    );
    private MainFrameState frameState;

    private final Map<ViewId, IViewLayout> storedLayouts = new HashMap<>();
    private DockController dockController;
    private DockFrontend dockFrontend;

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

        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

        // Default position/size
        setSize(800, 600);

        WindowsUtils.centerOnScreen(this);

        addWindowStateListener(
                e -> storeWindowState()
        );

        addComponentListener(
                new ComponentAdapter() {
                    @Override
                    public void componentResized(ComponentEvent e) {
                        storeWindowState();
                    }

                    @Override
                    public void componentMoved(ComponentEvent e) {
                        storeWindowState();
                    }
                }
        );
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
                        IMiscAH s = Storage.get(IMiscAH.class);

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
                            Request.EXTRA_MESSAGES.process(
                                    MainFrame.this,
                                    Request.PACKET_HANDLER,
                                    new ASwingThreadedHandler<IPacket>() {
                                        @Override
                                        protected void execute(IPacket data) {
                                            SwingUtilities.invokeLater(
                                                    () -> openMessage(messageId, method)
                                            );
                                        }
                                    }
                            );
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
        dockController = new DockController();
        dockController.setPopupMenuFactory(new ItemViewPopupFactory());

        dockFrontend = new DockFrontend(dockController, getMainFrame());
        dockFrontend.addVetoableListener(
                new VetoableDockFrontendListener() {
                    @Override
                    public void showing(VetoableDockFrontendEvent event) {
                    }

                    @Override
                    public void shown(VetoableDockFrontendEvent event) {
                        final DockFrontend frontend = event.getFrontend();

                        for (Dockable d : event.getDockables()) {
                            if (d instanceof DefaultDockable) {
                                DefaultDockable dd = (DefaultDockable) d;
                                IItemView iv = (IItemView) dd.getClientComponent();
                                frontend.addDockable(iv.getId().getAnchor(), d);
                                frontend.setHideable(d, true);
                            }
                        }
                    }

                    @Override
                    public void hiding(VetoableDockFrontendEvent event) {
                    }

                    @Override
                    public void hidden(VetoableDockFrontendEvent event) {
                        final DockFrontend frontend = event.getFrontend();

                        for (Dockable d : event.getDockables()) {
                            if (!(d instanceof DefaultDockable)) {
                                continue;
                            }

                            IItemView itemView = (IItemView) ((DefaultDockable) d).getClientComponent();
                            if (openedViews.values().remove(d)) {
                                storedLayouts.put(itemView.getId(), itemView.storeLayout());
                            }

                            frontend.remove(d);
                        }
                    }
                }
        );

        threadsRootWindow = new SplitDockStation(false);

        dockFrontend.addRoot("threads", threadsRootWindow);

        JPanel cp = new JPanel(new BorderLayout());
        setContentPane(cp);

        threadsRootWindow.setExpandOnDoubleclick(false);

        dockFrontend.addFrontendListener(
                new DockFrontendAdapter() {
                    @Override
                    public void removed(DockFrontend frontend, Dockable dockable) {
                        super.removed(frontend, dockable);
                    }

                    @Override
                    public void hidden(DockFrontend fronend, Dockable dockable) {
                        super.hidden(fronend, dockable);
                    }


                }
        );

        cp.add(createThreadsView(threadsRootWindow), BorderLayout.CENTER);

        // Setup DnD
        DropTarget target = new DropTarget(
                this,
                DnDConstants.ACTION_COPY_OR_MOVE,
                new MainFrameTargetAdapter(),
                true
        );

        setDropTarget(target);
    }

    private JPanel createThreadsView(DockStation threads) {
        JPanel readingPane = new JPanel(new BorderLayout(0, 0));

        // Setup toolbar
        JButton showHome = WindowsUtils.registerImageButton(readingPane, "home", new GoHomeAction());

        navigationBackButton = WindowsUtils.registerImageButton(readingPane, "nav_back", new GoBackAction());
        navigationForwardButton = WindowsUtils.registerImageButton(readingPane, "nav_forward", new GoForwardAction());

        JButton goToMessageButton = WindowsUtils.registerImageButton(
                readingPane,
                "goto_message",
                new GoToMessageAction()
        );

        JButton updateButton = WindowsUtils.registerImageButton(readingPane, "update", new SynchronizationAction());
        JButton loadMessageButton = WindowsUtils.registerImageButton(
                readingPane,
                "extramessage",
                new LoadExtraMessagesAction()
        );
        JButton subscribeButton = WindowsUtils.registerImageButton(readingPane, "forum_manage", new SubscribeForum());
        JButton settingsButton = WindowsUtils.registerImageButton(readingPane, "settings", new SettingsAction());
        JButton aboutButton = WindowsUtils.registerImageButton(readingPane, "about", new AboutAction());

        final JToolBar toolBar = WindowsUtils.createToolBar(
                showHome,
                null,
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

        toolBar.addPropertyChangeListener(
                "orientation", evt -> progressInToolbar.setOrientation(toolBar.getOrientation())
        );

        readingPane.add(toolBar, BorderLayout.NORTH);

        JPanel threadsPane = new JPanel(new BorderLayout());
        threadsPane.add(threads.asDockable().getComponent(), BorderLayout.CENTER);
        threadsPane.add(setupHintContainer(), BorderLayout.NORTH);

        readingPane.add(threadsPane, BorderLayout.CENTER);

        updateNavigationButtons();

        return readingPane;
    }

    private HintContainer setupHintContainer() {
        final HintContainer hintContainer = new HintContainer();

        addStatisticUpdateListener(new UnreadRepliesHintController(this, hintContainer));

        return hintContainer;
    }

    private DefaultDockable makeViewWindow(final IItemView itemView) {
        final DefaultDockable view = new DefaultDockable(
                itemView.getComponent(),
                itemView.getTabTitle(),
                itemView.getTabTitleIcon()
        );

        itemView.addStateChangeListener(navigationListener);

        ShortCutUtils.mergeInputMaps((JComponent) view.getComponent(), itemView.getComponent());

        itemView.addInfoChangeListener(new TitleChangeTracker(itemView, view));

        ViewId viewId = itemView.getId();
        openedViews.put(viewId, view);
        IViewLayout layout = storedLayouts.get(viewId);
        if (layout != null) {
            itemView.setupLayout(layout);
        }

        return view;
    }

    private DockTitleFactory getTabTitleProvider() {
        int tabTitleLimit = VIEW_THREAD_TAB_TITLE_LIMIT.get();

        if (tabTitleLimit > 0) {
            return new TrimmedDockTitleFactory(tabTitleLimit);
        } else {
            return new BasicDockTitleFactory();
        }
    }

    @Override
    public void setVisible(boolean b) {
        super.setVisible(b);
    }

    /**
     * Method for delegating changes to all sub containers.
     *
     * @param packet packet to be dispatched
     */
    @Override
    public void processPacket(IPacket packet) {
        mainDispatcher.dispatch(packet);
        startPageView.processPacket(packet);

        for (DefaultDockable v : openedViews.values()) {
            final IView view = (IView) v.getClientComponent();
            if (view != startPageView) {
                view.processPacket(packet);
            }
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
//        File file = RojacUtils.getLayoutFile();
//        if (file.isFile()) {
//            if (log.isInfoEnabled()) {
//                log.info("Load previous layout");
//            }
//            try {
//                try (ObjectInputStream in = new ObjectInputStream(new BufferedInputStream(new FileInputStream(file)))) {
//                    threadsRootWindow.read(in, false);
//
//                    try {
//                        setObjectState((IState) in.readObject());
//
//                        // Null means end of list
//                        ViewId viewId;
//                        while ((viewId = (ViewId) in.readObject()) != null) {
//                            storedLayouts.put(viewId, (IViewLayout) in.readObject());
//                        }
//                    } catch (EOFException e) {
//                        // Ignore to support previous revisions
//                    }
//                }
//
//                return;
//            } catch (ClassNotFoundException e) {
//                log.error("Main frame state class is not found", e);
//            } catch (IOException | RuntimeException e) {
//                log.error("Can not load views layout.", e);
//            }
//        } else {
//            if (log.isInfoEnabled()) {
//                log.info("No previous layout is found - use default.");
//            }
//        }

        // Setup default layout
        openTab(startPageView.getId());
    }

    public void storeSettings() {
//        File file = RojacUtils.getLayoutFile();
//        try {
//            try (ObjectOutputStream out = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(file)))) {
//                threadsRootWindow.write(out, false);
//                // Last item - main frame state
//                out.writeObject(getObjectState());
//
//                // Write forum layouts only
//                for (Map.Entry<ViewId, IViewLayout> element : storedLayouts.entrySet()) {
//                    if (element.getKey().getType() == ViewType.Forum ||
//                            element.getKey().getType() == ViewType.OutBox ||
//                            element.getKey().getType() == ViewType.Favorite) {
//                        out.writeObject(element.getKey()); // Identifier
//                        out.writeObject(element.getValue()); // Panel layout
//                    }
//                }
//                out.writeObject(null);
//            }
//
//        } catch (IOException e) {
//            log.error("Can not store views layout.", e);
//        }
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
    public DefaultDockable openTab(ViewId viewId) {
        final DefaultDockable dockable = openedViews.get(viewId);
        if (dockable != null) {
            dockFrontend.getController().setFocusedDockable(dockable, true);
            return dockable;
        }

        IItemView v = ViewHelper.makeView(viewId, this);

        final DefaultDockable view = makeViewWindow(v);

        final Dockable frontDockable = threadsRootWindow.getFrontDockable();
        if (frontDockable == null) {
            threadsRootWindow.drop(view, SplitDockProperty.EAST);
        } else {
            threadsRootWindow.drop(view, threadsRootWindow.getDockablePathProperty(frontDockable));
        }

        v.loadItem(viewId.getId());

        return view;
    }

    @Override
    public void closeTab(ViewId viewId) {
        if (openedViews.containsKey(viewId)) {
            final Dockable dockable = openedViews.remove(viewId);

            dockFrontend.remove(dockable);
        }
    }

    @Override
    public JFrame getMainFrame() {
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

        Iterator<DefaultDockable> iterator = openedViews.values().iterator();
        while (iterator.hasNext()) {
            DefaultDockable v = iterator.next();
            if (v.getClientComponent() instanceof IItemView) {
                if (v.asDockable().getDockParent() == null) {
                    iterator.remove();
                    continue;
                }

                IItemView itemView = (IItemView) v.getClientComponent();

                if (openMessageMethod.getAssociatedViewType() == itemView.getId().getType()) {
                    if (itemView.containsItem(messageId)) {
                        // Item found in the view.
                        // Make the view visible and open it.
                        dockFrontend.getController().setFocusedDockable(v, true);
                        itemView.makeVisible(messageId);
                        return;
                    }
                }
            }
        }

        new MessageNavigator(messageId, openMessageMethod).execute();
    }

    private void goToView(ViewId id, IState state) {
        DefaultDockable v = openTab(id);

        if (state != null) {
            ((IStateful) v.getClientComponent()).setObjectState(state);
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
                IForumAH forumAH = Storage.get(IForumAH.class);

                publish(forumAH.hasSubscribedForums());
                return null;
            }

            @Override
            protected void process(List<Boolean> chunks) {
                boolean canProcess = chunks.iterator().next();

                if (canProcess) {
                    Request.SYNCHRONIZE.process(MainFrame.this);
                } else {
                    int response = JLOptionPane.showConfirmDialog(
                            MainFrame.this,
                            Message.WarnDialog_NothingToSync_Question.get(),
                            Message.WarnDialog_NothingToSync_Title.get(),
                            JOptionPane.YES_NO_OPTION
                    );
                    if (response == JOptionPane.YES_OPTION) {
                        DialogHelper.openForumSubscriptionDialog(
                                MainFrame.this,
                                () -> {
                                    assert RojacUtils.checkThread(true);

                                    // Check again for forum changes
                                    startSynchronization();
                                }
                        );
                    }
                }
            }
        }.execute();
    }

    @Override
    public StartPageView getStartPageView() {
        return startPageView;
    }

    public void installBrowser(final JWebBrowser browser) {
        JDialog window = new JDialog(this);
        JScrollPane pane = new JScrollPane(browser);
        window.setContentPane(pane);

        getContentPane().add(pane, BorderLayout.SOUTH);

        pane.setPreferredSize(new Dimension(0, 0));
        pane.setVisible(false);
        window.setVisible(false);
    }

    public void addStatisticUpdateListener(IStatisticListener listener) {
        startPageView.addStatisticListener(listener);
    }

    private class ScheduleSynchronization implements Runnable {
        @Override
        public void run() {
            SwingUtilities.invokeLater(
                    () -> startSynchronization()
            );
        }
    }

//    private class ThreadViewSerializer implements ViewSerializer {
//        @Override
//        public void writeView(View view, ObjectOutputStream out) throws IOException {
//            assert view.getComponent() instanceof IItemView;
//
//            ViewHelper.storeView(out, (IItemView) view.getComponent());
//        }
//
//        @Override
//        public View readView(ObjectInputStream in) throws IOException {
//            try {
//                IItemView v = ViewHelper.initializeView(in, MainFrame.this);
//
//                return makeViewWindow(v);
//            } catch (ClassNotFoundException e) {
//                log.error("Can not obtain state object.", e);
//                throw new IOException("Can not obtain state object.", e);
//            }
//        }
//    }
//

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

            DefaultDockable c = openTab(viewId);
            if (c != null) {

                // Just in case :)
                dockController.setFocusedDockable(c, true);
                IItemView itemView = (IItemView) c.getClientComponent();
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

    private class GoHomeAction extends AButtonAction {
        private GoHomeAction() {
            super(ShortCut.GoHome);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            openTab(ViewType.StartPage.makeId(0));
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

    private class MainFrameTargetAdapter extends DropTargetAdapter {
        private DataFlavor acceptableType = null;

        @Override
        public void dragEnter(DropTargetDragEvent dtde) {
            // Get the type of object being transferred and determine
            // whether it is appropriate.
            checkTransferType(dtde);

            // Accept or reject the drag.
            acceptOrRejectDrag(dtde);
        }

        @Override
        public void drop(DropTargetDropEvent dtde) {
            if ((dtde.getDropAction() & DnDConstants.ACTION_COPY_OR_MOVE) != 0) {
                // Accept the drop and get the transfer data
                dtde.acceptDrop(dtde.getDropAction());
                Transferable transferable = dtde.getTransferable();

                try {
                    String url = (String) transferable.getTransferData(DataFlavor.stringFlavor);

                    Integer idFromUrl = LinkUtils.getMessageId(url);
                    OpenMessageMethod method;
                    if (idFromUrl != null) {
                        method = Property.DROP_BEHAVIOUR_URL_MESSAGE.get();
                    } else if ((idFromUrl = LinkUtils.getThreadId(url)) != null) {
                        method = Property.DROP_BEHAVIOUR_URL_TOPIC.get();
                    } else if ((idFromUrl = LinkUtils.getMessageIdFromUrl(url)) != null) {
                        method = Property.DROP_BEHAVIOUR_URL_OTHERS.get();
                    } else {
                        dtde.dropComplete(false);
                        return;
                    }

                    openMessage(idFromUrl, method);

                    dtde.dropComplete(true);
                } catch (Exception e) {
                    dtde.dropComplete(false);
                }
            } else {
                dtde.rejectDrop();
            }
        }

        protected boolean acceptOrRejectDrag(DropTargetDragEvent dtde) {
            int dropAction = dtde.getDropAction();
            int sourceActions = dtde.getSourceActions();
            boolean acceptedDrag = false;

            // Reject if the object being transferred
            // or the operations available are not acceptable.
            if (acceptableType == null
                    || (sourceActions & DnDConstants.ACTION_COPY_OR_MOVE) == 0) {
                dtde.rejectDrag();
            } else if ((dropAction & DnDConstants.ACTION_COPY_OR_MOVE) == 0) {
                // Not offering copy or move - suggest a copy
                dtde.acceptDrag(DnDConstants.ACTION_COPY);
                acceptedDrag = true;
            } else {
                // Offering an acceptable operation: accept
                dtde.acceptDrag(dropAction);
                acceptedDrag = true;
            }

            return acceptedDrag;
        }

        protected void checkTransferType(DropTargetDragEvent dtde) {
//            // Only accept a flavor that returns a Component
//            DataFlavor[] fl = dtde.getCurrentDataFlavors();
//            for (DataFlavor aFl : fl) {
//                if (log.isInfoEnabled()) {
//                    log.info(aFl);
//                }
//
//                Class dataClass = aFl.getRepresentationClass();
//                if (URL.class.isAssignableFrom(dataClass)) {
//                    acceptableType = aFl;
//                    return;
//                }
//
//                // Add more custom DataFlavors here
//            }

            if (dtde.getTransferable().isDataFlavorSupported(DataFlavor.stringFlavor)) {
                acceptableType = DataFlavor.stringFlavor;
            }
        }
    }
}
