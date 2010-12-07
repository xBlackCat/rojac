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
import org.xblackcat.rojac.data.MessageData;
import org.xblackcat.rojac.gui.component.AButtonAction;
import org.xblackcat.rojac.gui.component.ShortCut;
import org.xblackcat.rojac.gui.dialogs.EditMessageDialog;
import org.xblackcat.rojac.gui.dialogs.LoadMessageDialog;
import org.xblackcat.rojac.gui.view.MessageChecker;
import org.xblackcat.rojac.gui.view.ViewHelper;
import org.xblackcat.rojac.gui.view.ViewIds;
import org.xblackcat.rojac.gui.view.favorites.FavoritesView;
import org.xblackcat.rojac.gui.view.forumlist.ForumsListView;
import org.xblackcat.rojac.i18n.Messages;
import org.xblackcat.rojac.service.ServiceFactory;
import org.xblackcat.rojac.service.datahandler.IDataHandler;
import org.xblackcat.rojac.service.datahandler.IPacket;
import org.xblackcat.rojac.service.storage.IStorage;
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

public class MainFrame extends JFrame implements IConfigurable, IRootPane, IDataHandler {
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
    private RootWindow rootWindow;

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
        JButton updateButton = WindowsUtils.registerImageButton(threadsPane, "update", new SynchronizationAction());
        JButton loadMessageButton = WindowsUtils.registerImageButton(threadsPane, "extramessage", new LoadExtraMessagesAction());
        JButton settingsButton = WindowsUtils.registerImageButton(threadsPane, "settings", new SettingsAction());
        JButton aboutButton = WindowsUtils.registerImageButton(threadsPane, "about", new AboutAction());

        JToolBar toolBar = WindowsUtils.createToolBar(
                updateButton,
                loadMessageButton,
                null,
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

        return view;
    }

    private void extraMessagesDialog(Integer id) {
        LoadMessageDialog lmd = new LoadMessageDialog(this, id);
        Integer messageId = lmd.readMessageId();
        if (messageId != null) {
            boolean loadAtOnce = lmd.isLoadAtOnce();

            new ExtraMessageLoader(messageId, loadAtOnce, this).execute();
        }
    }

    private static View createView(Messages title, IView comp) {
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

    private void addTabView(String viewName, final IItemView itemView) {
        final View view = new View(
                viewName,
                null,
                itemView.getComponent()
        );

        ShortCutUtils.mergeInputMaps(view, itemView.getComponent());

        DockingWindowProperties props = view.getWindowProperties();
        props.setMinimizeEnabled(false);
        props.setTitleProvider(SimpleDockingWindowTitleProvider.INSTANCE);
        props.setUndockEnabled(false);

        itemView.addActionListener(new IActionListener() {
            @Override
            public void itemGotFocus(Integer forumId, Integer messageId) {
            }

            @Override
            public void itemLostFocus(Integer forumId, Integer messageId) {
            }

            @Override
            public void itemUpdated(Integer forumId, Integer messageId) {
                String title = itemView.getTabTitle();
                view.getViewProperties().setTitle(title);
            }
        });

        openedViews.put(itemView.getId(), view);

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
        forumsListView.applySettings();
        favoritesView.applySettings();

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
        forumsListView.storeSettings();
        favoritesView.storeSettings();

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

    public void storeWindowState() {
        int state = getExtendedState();

        ROJAC_MAIN_FRAME_STATE.set(state);
        if (state == NORMAL) {
            ROJAC_MAIN_FRAME_POSITION.set(getLocation());
            ROJAC_MAIN_FRAME_SIZE.set(getSize());
        }
    }

    @Override
    public void openForumTab(int forumId) {
        ViewId viewId = ViewIds.getForumId(forumId);
        View c = openedViews.get(viewId);
        if (c != null && c.getParent() != null) {
            c.makeVisible();
            return;
        }

        //        return ViewHelper.makeTreeMessageView(this);
        IItemView forumView = ViewHelper.constructForumView(viewId, this);

        addTabView("#" + forumId, forumView);
        forumView.loadItem(forumId);

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

    @Override
    public void openMessageTab(int messageId) {
        // Search thought existing data for the message first.
        final ViewId id = ViewIds.getMessageId(messageId);
        if (openedViews.containsKey(id)) {
            openedViews.get(id).makeVisible();
            return;
        }

        new MessageChecker(messageId) {
            @Override
            protected void done() {
                if (data != null) {
                    IItemView messageView = ViewHelper.makeMessageView(id, MainFrame.this);
                    addTabView("#" + messageId, messageView);
                    messageView.loadItem(messageId);
                } else {
                    extraMessagesDialog(messageId);
                }
            }
        }.execute();
    }

    private class ThreadViewSerializer implements ViewSerializer {
        @Override
        public void writeView(View view, ObjectOutputStream out) throws IOException {
            ViewHelper.storeView(out, view);
        }

        @Override
        public View readView(ObjectInputStream in) throws IOException {
            try {
                View view = ViewHelper.initializeView(in, MainFrame.this);
                ViewId id = ((IView) view.getComponent()).getId();
                openedViews.put(id, view);
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

    private class MessageNavigator extends RojacWorker<Void, Void> {
        private final int messageId;
        protected MessageData message;

        public MessageNavigator(int messageId) {
            this.messageId = messageId;
        }

        @Override
        protected Void perform() throws Exception {
            // Loads a message info and forum info.
            IStorage storage = ServiceFactory.getInstance().getStorage();
            message = storage.getMessageAH().getMessageData(messageId);

            if (message == null) {
                return null;
            }

            return null;
        }

        @Override
        protected void done() {
            if (message == null) {
                extraMessagesDialog(messageId);
                return;
            }

            int forumId = message.getForumId();
            ViewId forumViewId = ViewIds.getForumId(forumId);

            openForumTab(forumId);
            View c = openedViews.get(forumViewId);
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
            extraMessagesDialog(null);
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
}
