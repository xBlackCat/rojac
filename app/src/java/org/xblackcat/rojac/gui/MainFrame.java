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
import org.xblackcat.rojac.RojacException;
import org.xblackcat.rojac.data.MessageData;
import org.xblackcat.rojac.gui.dialogs.AboutDialog;
import org.xblackcat.rojac.gui.dialogs.EditMessageDialog;
import org.xblackcat.rojac.gui.dialogs.ExceptionDialog;
import org.xblackcat.rojac.gui.dialogs.LoadMessageDialog;
import org.xblackcat.rojac.gui.dialogs.OptionsDialog;
import org.xblackcat.rojac.gui.view.FavoritesView;
import org.xblackcat.rojac.gui.view.ViewHelper;
import org.xblackcat.rojac.gui.view.forumlist.ForumsListView;
import org.xblackcat.rojac.gui.view.thread.ITreeItem;
import org.xblackcat.rojac.gui.view.thread.Post;
import org.xblackcat.rojac.i18n.Messages;
import org.xblackcat.rojac.service.ServiceFactory;
import org.xblackcat.rojac.service.datahandler.IDataHandler;
import org.xblackcat.rojac.service.datahandler.ProcessPacket;
import org.xblackcat.rojac.service.janus.commands.AffectedMessage;
import org.xblackcat.rojac.service.storage.IStorage;
import org.xblackcat.rojac.util.RojacUtils;
import org.xblackcat.rojac.util.RojacWorker;
import org.xblackcat.rojac.util.SynchronizationUtils;
import org.xblackcat.rojac.util.WindowsUtils;
import org.xblackcat.utils.ResourceUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowStateListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
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
        // TODO: implement loading data
    }

    private void initialize() {
        JPanel cp = new JPanel(new BorderLayout());
        setContentPane(cp);

        // Setup forums view
        View viewForums = createView(
                Messages.VIEW_FORUMS_TITLE,
                forumsListView
        );

        // Setup favorites view
        View viewFavorites = createView(
                Messages.VIEW_FAVORITES_TITLE,
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

        RootWindow rootWindow = new RootWindow(false, viewMap, new SplitWindow(true, 0.25f, new TabWindow(mainViews), threadsView));
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
        JButton updateButton = WindowsUtils.setupImageButton("update", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                SynchronizationUtils.startSynchronization(MainFrame.this);
            }
        }, Messages.MAINFRAME_BUTTON_UPDATE);
        JButton loadMessageButton = WindowsUtils.setupImageButton("extramessage", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                LoadMessageDialog lmd = new LoadMessageDialog(MainFrame.this);
                Integer messageId = lmd.readMessageId();
                if (messageId != null) {
                    boolean loadAtOnce = lmd.isLoadAtOnce();

                    new ExtraMessageLoader(messageId, loadAtOnce, MainFrame.this).execute();
                }
            }
        }, Messages.MAINFRAME_BUTTON_LOADMESSAGE);
        JButton settingsButton = WindowsUtils.setupImageButton("settings", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    OptionsDialog od = new OptionsDialog(MainFrame.this);

                    WindowsUtils.center(od, MainFrame.this);
                    od.setVisible(true);
                } catch (RojacException ex) {
                    log.error("A model for options dialog.", ex);
                }
            }
        }, Messages.MAINFRAME_BUTTON_SETTINGS);
        JButton aboutButton = WindowsUtils.setupImageButton("about", new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AboutDialog ad = new AboutDialog(MainFrame.this);
                ad.pack();
                WindowsUtils.centerOnScreen(ad);
                ad.setVisible(true);
            }
        }, Messages.MAINFRAME_BUTTON_ABOUT);

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

    private void addTabView(ViewId viewId, String viewName, final IItemView itemView) {
        final View view = new View(
                viewName,
                null,
                itemView.getComponent()
        );


        DockingWindowProperties props = view.getWindowProperties();
        props.setMinimizeEnabled(false);
        props.setTitleProvider(SimpleDockingWindowTitleProvider.INSTANCE);
        props.setUndockEnabled(false);

        itemView.addActionListener(new IActionListener() {
            @Override
            public void itemGotFocus(AffectedMessage itemId) {
            }

            @Override
            public void itemLostFocus(AffectedMessage itemId) {
            }

            @Override
            public void itemUpdated(AffectedMessage itemId) {
                String title = itemView.getTabTitle();
                view.getViewProperties().setTitle(title);
            }
        });

        openedViews.put(viewId, view);

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
     * Create a forum view layout depending on user settings.
     *
     * @return a new forum view layout.
     */
    private IItemView createForumViewWindow() {
//        return ViewHelper.makeTreeMessageView(this);
        return ViewHelper.makeTreeTableMessageView(this);
    }

    /**
     * Method for delegating changes to all sub containers.
     *
     * @param results
     */
    @Override
    public void processPacket(ProcessPacket results) {
        forumsListView.processPacket(results);
        favoritesView.processPacket(results);

        for (View v : openedViews.values()) {
            ((IView) v.getComponent()).processPacket(results);
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

        // TODO: implement
    }

    @Override
    public void updateSettings() {
        forumsListView.updateSettings();
        favoritesView.updateSettings();

        storeWindowState();

        // TODO: implement
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
        ViewId viewId = new ViewId(forumId, null);
        View c = openedViews.get(viewId);
        if (c != null) {
            c.makeVisible();
            return;
        }

        IItemView forumView = createForumViewWindow();

        addTabView(viewId, "#" + forumId, forumView);
        forumView.loadItem(new AffectedMessage(forumId));

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
    public void openMessage(int messageId, OpenMessageMethod method) {
        // Search thought existing data for the message first.
        AffectedMessage id = new AffectedMessage(AffectedMessage.DEFAULT_FORUM, messageId);
        for (View v : openedViews.values()) {
            if (v.getComponent() instanceof IItemView) {
                IItemView itemView = (IItemView) v.getComponent();

                ITreeItem item = itemView.searchItem(id);
                if (item != null) {
                    // Item found in the view.
                    // Make the view visible and open it.

                    v.makeVisible();
                    itemView.makeVisible(item);
                    return;
                }
            }
        }

        new MessageNavigator(messageId).execute();
    }

    private static class ThreadViewSerializer implements ViewSerializer {
        @Override
        public void writeView(View view, ObjectOutputStream out) throws IOException {
        }

        @Override
        public View readView(ObjectInputStream in) throws IOException {
            return null;
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
                // TODO: show dialog to confirm message loading in next synchronization and put the id to load queue.
                return;
            }

            int forumId = message.getForumId();
            ITreeItem item = new Post(message, null);
            AffectedMessage targetId = new AffectedMessage(forumId, messageId);

            ViewId messageViewId = new ViewId(message);
            ViewId forumViewId = new ViewId(forumId, null);

            View c = openedViews.get(forumViewId);
            if (c != null) {
                c.makeVisible();
                IItemView itemView = (IItemView) c.getComponent();
                itemView.makeVisible(item);
                return;
            }

            // Check if the message view is already opened
            c = openedViews.get(messageViewId);
            if (c != null) {
                c.makeVisible();
                return;
            }

            openForumTab(forumId);
            c = openedViews.get(forumViewId);
            if (c != null) {
                // Just in case :)
                c.makeVisible();
                IItemView itemView = (IItemView) c.getComponent();
                itemView.makeVisible(item);
            } else {
                ExceptionDialog.showExceptionDialog(new RuntimeException("F*cka-morgana! Forum view is not loaded!"));
            }
        }
    }
}