package org.xblackcat.rojac.gui;

import net.infonode.docking.*;
import net.infonode.docking.drop.DropFilter;
import net.infonode.docking.drop.DropInfo;
import net.infonode.docking.properties.DockingWindowProperties;
import net.infonode.docking.title.LengthLimitedDockingWindowTitleProvider;
import net.infonode.docking.util.StringViewMap;
import net.infonode.docking.util.WindowMenuUtil;
import net.infonode.util.Direction;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xblackcat.rojac.RojacException;
import org.xblackcat.rojac.data.Forum;
import org.xblackcat.rojac.gui.dialogs.EditMessageDialog;
import org.xblackcat.rojac.gui.dialogs.LoadMessageDialog;
import org.xblackcat.rojac.gui.dialogs.OptionsDialog;
import org.xblackcat.rojac.gui.dialogs.progress.ITask;
import org.xblackcat.rojac.gui.dialogs.progress.ProgressTrackerDialog;
import org.xblackcat.rojac.gui.view.FavoritesView;
import org.xblackcat.rojac.gui.view.ForumsListView;
import org.xblackcat.rojac.gui.view.ViewHelper;
import org.xblackcat.rojac.i18n.Messages;
import org.xblackcat.rojac.service.ServiceFactory;
import org.xblackcat.rojac.service.commands.AffectedPosts;
import org.xblackcat.rojac.service.commands.IResultHandler;
import org.xblackcat.rojac.service.commands.LoadExtraMessagesCommand;
import org.xblackcat.rojac.service.commands.SynchronizeCommand;
import org.xblackcat.rojac.service.options.IOptionsService;
import org.xblackcat.rojac.service.options.Property;
import org.xblackcat.rojac.service.storage.IMiscAH;
import org.xblackcat.rojac.service.storage.StorageException;
import org.xblackcat.rojac.util.RojacUtils;
import org.xblackcat.rojac.util.WindowsUtils;

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

/**
 * @author xBlackCat
 */

public class MainFrame extends JFrame implements IConfigurable, IRootPane {
    private static final Log log = LogFactory.getLog(MainFrame.class);

    private IView forumsListView;
    private IView favoritesView;

    // Data tracking
    private Map<Integer, View> openedForums = new HashMap<Integer, View>();
    private final IOptionsService os;
    protected IResultHandler<AffectedPosts> changeHandler = new IResultHandler<AffectedPosts>() {
        public void process(AffectedPosts results) throws RojacException {
            forumsListView.updateData(results.getAffectedForumIds());
            favoritesView.updateData(results.getAffectedMessageIds());
        }
    };
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

    public MainFrame(IOptionsService optionsService) {
        super(RojacUtils.VERSION_STRING);

        os = optionsService;

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
                showProgressDialog(new SynchronizeCommand(changeHandler));
            }
        }, Messages.MAINFRAME_BUTTON_UPDATE);
        JButton loadMessageButton = WindowsUtils.setupImageButton("update", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                LoadMessageDialog lmd = new LoadMessageDialog(MainFrame.this);
                Integer messageId = lmd.readMessageId();
                if (messageId != null) {
                    IMiscAH s = ServiceFactory.getInstance().getStorage().getMiscAH();

                    try {
                        s.storeExtraMessage(messageId);
                        if (lmd.isLoadAtOnce()) {
                            showProgressDialog(new LoadExtraMessagesCommand(changeHandler));
                        }
                    } catch (StorageException e1) {
                        log.error("Can not store extra message id", e1);
                    }
                }
            }
        }, Messages.MAINFRAME_BUTTON_LOADMESSAGE);
        JButton settingsButton = WindowsUtils.setupImageButton("update", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                OptionsDialog od = new OptionsDialog(MainFrame.this);

                WindowsUtils.center(od, MainFrame.this);
                od.setVisible(true);
            }
        }, Messages.MAINFRAME_BUTTON_LOADMESSAGE);

        threadsPane.add(WindowsUtils.createToolBar(updateButton, loadMessageButton, settingsButton), BorderLayout.NORTH);
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

    private View createForumView(Forum f) {
        IMessageView forumView = createForumViewWindow();

        final View view = new View(
                f.getForumName(),
                null,
                forumView.getComponent()
        );


        DockingWindowProperties props = view.getWindowProperties();
        props.setMinimizeEnabled(false);
        props.setTitleProvider(new LengthLimitedDockingWindowTitleProvider(50));
        props.setUndockEnabled(false);

        view.addListener(new CloseThreadListener(f.getForumId()));

        forumView.loadItem(f.getForumId());

        return view;
    }

    /**
     * Create a forum view layout depending on user settings.
     *
     * @return a new forum view layout.
     */
    private IMessageView createForumViewWindow() {
        return ViewHelper.makeTreeMessageView(this);
    }

    public void applySettings() {
        forumsListView.applySettings();
        favoritesView.applySettings();

        Point pos = os.getProperty(Property.ROJAC_MAIN_FRAME_POSITION);
        if (pos != null) {
            setLocation(pos);
        }

        Dimension size = os.getProperty(Property.ROJAC_MAIN_FRAME_SIZE);
        if (size != null) {
            setSize(size);
        }

        Integer state = os.getProperty(Property.ROJAC_MAIN_FRAME_STATE);
        if (state != null) {
            setExtendedState(state);
        }

        // TODO: implement
    }

    public void updateSettings() {
        forumsListView.updateSettings();
        favoritesView.updateSettings();

        storeWindowState();

        // TODO: implement
    }

    public void storeWindowState() {
        int state = getExtendedState();

        os.setProperty(Property.ROJAC_MAIN_FRAME_STATE, state);
        if (state == NORMAL) {
            os.setProperty(Property.ROJAC_MAIN_FRAME_POSITION, getLocation());
            os.setProperty(Property.ROJAC_MAIN_FRAME_SIZE, getSize());
        }
    }

    public void openForumTab(Forum f) {
        View c = openedForums.get(f.getForumId());
        if (c != null) {
            c.makeVisible();
            return;
        }

        View fv = createForumView(f);

        openedForums.put(f.getForumId(), fv);

        DockingWindow rootWindow = threadsRootWindow.getWindow();
        if (rootWindow != null) {
            if (rootWindow instanceof TabWindow) {
                ((TabWindow) rootWindow).addTab(fv);
            } else {
                TabWindow tw = searchForTabWindow(rootWindow);
                if (tw != null) {
                    tw.addTab(fv);
                } else {
                    rootWindow.split(fv, Direction.RIGHT, 0.5f);
                }
            }
        } else {
            threadsRootWindow.setWindow(new TabWindow(fv));
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

    public void showProgressDialog(ITask task) {
        ProgressTrackerDialog tr = new ProgressTrackerDialog(this, task);

        WindowsUtils.center(tr, this);
        tr.setVisible(true);
        tr.startTask();
    }

    public void editMessage(Integer forumId, Integer messageId) {
        EditMessageDialog editDlg = new EditMessageDialog(this);

        if (forumId == null) {
            editDlg.editMessage(messageId);
        } else {
            editDlg.answerOn(messageId);
        }
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

    private class CloseThreadListener extends DockingWindowAdapter {
        private final int forumId;

        public CloseThreadListener(int forumId) {
            this.forumId = forumId;
        }

        @Override
        public void windowClosed(DockingWindow window) {
            openedForums.remove(forumId);
        }
    }
}
