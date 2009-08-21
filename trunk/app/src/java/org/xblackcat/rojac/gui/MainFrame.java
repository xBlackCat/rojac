package org.xblackcat.rojac.gui;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.flexdock.docking.DockingConstants;
import org.flexdock.docking.DockingManager;
import org.flexdock.util.SwingUtility;
import org.flexdock.view.View;
import org.flexdock.view.Viewport;
import org.xblackcat.rojac.RojacException;
import org.xblackcat.rojac.data.Forum;
import org.xblackcat.rojac.gui.dialogs.LoadMessageDialog;
import org.xblackcat.rojac.gui.frame.message.MessagePane;
import org.xblackcat.rojac.gui.frame.progress.ITask;
import org.xblackcat.rojac.gui.frame.progress.ProgressTrackerDialog;
import org.xblackcat.rojac.gui.frame.thread.ForumThreadsView;
import org.xblackcat.rojac.gui.frame.thread.ThreadDoubleView;
import org.xblackcat.rojac.gui.view.FavoritesView;
import org.xblackcat.rojac.gui.view.ForumsListView;
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
import org.xblackcat.rojac.util.WindowsUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Date: 23 груд 2007
 *
 * @author xBlackCat
 */

public class MainFrame extends JFrame implements IConfigurable, IRootPane {
    private static final Log log = LogFactory.getLog(MainFrame.class);

    private IView forumsListView;
    private IView favoritesView;

    // Components
    private JTabbedPane threads;
    private View viewForums;
    private View viewThreads;
    private View viewFavorites;

    // Data tracking
    private Map<Integer, Component> openedForums = new HashMap<Integer, Component>();
    private final IOptionsService os;
    protected IResultHandler<AffectedPosts> changeHandler = new IResultHandler<AffectedPosts>() {
        public void process(AffectedPosts results) throws RojacException {
            forumsListView.updateData(results.getAffectedForumIds());
            favoritesView.updateData(results.getAffectedMessageIds());
        }
    };

    public MainFrame(IOptionsService optionsService) {
        super(Messages.MAIN_WINDOW_TITLE.get());

        os = optionsService;

        forumsListView = new ForumsListView(this);
        favoritesView = new FavoritesView();

        initialize();

        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

        // Default position/size
        setSize(640, 480);

        SwingUtility.centerOnScreen(this);

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

        threads = new JTabbedPane();

        Viewport viewport = new Viewport();
        cp.add(viewport, BorderLayout.CENTER);

        // FlexDock initialization
        DockingManager.setFloatingEnabled(true);

        // Center of the window is used with tabbed pane.
        viewThreads = new View("threads_view", null, null);
        viewThreads.setTerritoryBlocked(DockingConstants.CENTER_REGION, true);
        viewThreads.setTitlebar(null);
        viewThreads.setContentPane(createCenterPane());

        viewport.dock(viewThreads);

        // Setup forums view
        viewForums = createView(
                "forums_view",
                Messages.VIEW_FORUMS_TITLE,
                Messages.VIEW_FORUMS_TAB_TEXT,
                forumsListView.getComponent()
        );

        viewThreads.dock(viewForums, DockingConstants.WEST_REGION, 0.4f);

        // Setup favorites view
        viewFavorites = createView(
                "favorites_view",
                Messages.VIEW_FAVORITES_TITLE,
                Messages.VIEW_FAVORITES_TAB_TEXT,
                favoritesView.getComponent()
        );

        viewThreads.dock(viewFavorites, DockingConstants.EAST_REGION, 0.2f);

        DockingManager.setMinimized(viewFavorites, true);
    }

    private JComponent createCenterPane() {
        final JPanel p = new JPanel(new BorderLayout());
        p.add(threads);

        JPanel topPane = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPane.add(WindowsUtils.setupImageButton("update", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showProgressDialog(new SynchronizeCommand(changeHandler));
            }
        }, Messages.MAINFRAME_BUTTON_UPDATE));
        topPane.add(WindowsUtils.setupImageButton("update", new ActionListener() {
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
        }, Messages.MAINFRAME_BUTTON_LOADMESSAGE));

        p.add(topPane, BorderLayout.NORTH);

        return p;
    }

    private View createView(String id, Messages title, Messages tabText, JComponent comp) {
        final View view = new View(
                id,
                title.get(),
                tabText.get()
        );
        view.addAction(DockingConstants.CLOSE_ACTION);
        view.addAction(DockingConstants.PIN_ACTION);
        view.setContentPane(comp);

        return view;
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
        Component c = openedForums.get(f.getForumId());
        if (c != null) {
            threads.setSelectedComponent(c);
            return;
        }

        ThreadDoubleView $ = new ThreadDoubleView(new ForumThreadsView(this), new MessagePane(this), false, this);
        openedForums.put(f.getForumId(), $);

        threads.addTab(f.getForumName(), $);
        int idx = threads.indexOfComponent($);
        threads.setTabComponentAt(idx, new TabHeader(f));

        $.viewItem(f.getForumId(), false);
    }

    public void showProgressDialog(ITask task) {
        ProgressTrackerDialog tr = new ProgressTrackerDialog(this, task);

        SwingUtility.center(tr, this);
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

    private class TabHeader extends JPanel {
        private TabHeader(Forum f) {
            super();
            setLayout(new FlowLayout(FlowLayout.LEFT));
            setOpaque(false);
            final int fId = f.getForumId();

            JLabel l = new JLabel() {
                public String getText() {
                    int i = threads.indexOfTabComponent(TabHeader.this);
                    if (i != -1) {
                        return threads.getTitleAt(i);
                    }
                    return null;
                }
            };

            l.setToolTipText(f.getForumName());
            l.setMaximumSize(new Dimension(100, 100));
            l.setBorder(null);

            setBorder(null);

            add(l);

            ActionListener al = new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    int idx = threads.indexOfTabComponent(TabHeader.this);
                    if (idx != -1) {
                        openedForums.remove(fId);

                        threads.remove(idx);
                    }
                }
            };
            JButton b = WindowsUtils.setupImageButton("tabclose", al, f.getForumName());

            add(b);

            l.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    int idx = threads.indexOfTabComponent(TabHeader.this);
                    if (idx != -1) {
                        threads.setSelectedIndex(idx);
                    }
                }
            });
        }
    }
}
