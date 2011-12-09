package org.xblackcat.rojac.gui.view.thread;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdesktop.swingx.JXTreeTable;
import org.jdesktop.swingx.table.TableColumnExt;
import org.xblackcat.rojac.RojacDebugException;
import org.xblackcat.rojac.data.MessageData;
import org.xblackcat.rojac.data.NewMessage;
import org.xblackcat.rojac.data.NewMessageData;
import org.xblackcat.rojac.gui.*;
import org.xblackcat.rojac.gui.component.AButtonAction;
import org.xblackcat.rojac.gui.component.ShortCut;
import org.xblackcat.rojac.gui.view.AnItemView;
import org.xblackcat.rojac.gui.view.MessageChecker;
import org.xblackcat.rojac.gui.view.ThreadState;
import org.xblackcat.rojac.gui.view.ViewId;
import org.xblackcat.rojac.gui.view.message.MessageDataHolder;
import org.xblackcat.rojac.gui.view.message.MessagePane;
import org.xblackcat.rojac.gui.view.model.*;
import org.xblackcat.rojac.service.datahandler.IPacket;
import org.xblackcat.rojac.service.datahandler.ReloadDataPacket;
import org.xblackcat.rojac.service.options.Property;
import org.xblackcat.rojac.service.storage.IMessageAH;
import org.xblackcat.rojac.service.storage.INewMessageAH;
import org.xblackcat.rojac.service.storage.Storage;
import org.xblackcat.rojac.service.storage.StorageException;
import org.xblackcat.rojac.util.MessageUtils;
import org.xblackcat.rojac.util.RojacUtils;
import org.xblackcat.rojac.util.RojacWorker;
import org.xblackcat.rojac.util.ShortCutUtils;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.Collection;
import java.util.Enumeration;
import java.util.LinkedList;

/**
 * @author xBlackCat
 */

public class TreeTableThreadView extends AnItemView {
    private static final Log log = LogFactory.getLog(TreeTableThreadView.class);

    protected final SortedThreadsModel model = new SortedThreadsModel();
    private final IModelControl modelControl;

    private JToolBar toolbar;
    private final JXTreeTable threads;
    private final MessagePane messagePane;

    // Current root id
    protected int rootItemId;
    protected ThreadState state;
    protected MessageData selectedItem;
    protected boolean resortingModel = false;

    private TableThreadViewLayout layout;
    private final JSplitPane splitPane;

    public TreeTableThreadView(ViewId id, IAppControl appControl, ModelControl modelControl) {
        super(id, appControl);

        this.modelControl = modelControl.get();

        model.addTreeModelListener(new DataIntegrityMonitor());

        Runnable onScrollEnd = new Runnable() {
            @Override
            public void run() {
                if (Property.VIEW_THREAD_SET_READ_ON_SCROLL.get()) {
                    if (!selectedItem.isRead()) {
                        MessageUtils.markMessageRead(getId(), selectedItem, 0);
                        selectedItem = selectedItem.setRead(true);
                    }
                }

                // Go to next unread post
                if (selectedItem != null) {
                    selectNextPost(selectedItem.getMessageId());
                }
            }
        };

        messagePane = new MessagePane(appControl, onScrollEnd);
        threads = getThreadsContainer();
        // Initialize tree
        JScrollPane threadsScroll = new JScrollPane(threads);

        splitPane = new JSplitPane(
                JSplitPane.VERTICAL_SPLIT,
                true,
                threadsScroll,
                messagePane
        );
        splitPane.setDividerLocation(200);
        splitPane.setResizeWeight(.1);

        add(splitPane, BorderLayout.CENTER);

        ShortCutUtils.mergeInputMaps(this, messagePane);

        threads.setInputMap(
                WHEN_ANCESTOR_OF_FOCUSED_COMPONENT,
                ShortCutUtils.mergeInputMaps(
                        getInputMap(WHEN_ANCESTOR_OF_FOCUSED_COMPONENT),
                        threads.getInputMap(WHEN_ANCESTOR_OF_FOCUSED_COMPONENT)
                )
        );

        // Install toolbar
        toolbar = createToolBar();

        if (toolbar != null) {
            add(toolbar, BorderLayout.NORTH);
        } else {
            // Tool bar will be created lately
            IInfoChangeListener toolBarTracker = new IInfoChangeListener() {
                @Override
                public void infoChanged() {
                    toolbar = createToolBar();

                    if (toolbar == null) {
                        // Still waiting for toolbar
                        return;
                    }

                    removeInfoChangeListener(this);

                    if (layout != null) {
                        toolbar.setOrientation(layout.getToolbarOrientation());
                        add(toolbar, layout.getToolbarPosition());
                    } else {
                        add(toolbar, BorderLayout.NORTH);
                    }
                }
            };
            addInfoChangeListener(toolBarTracker);
        }
    }

    protected JXTreeTable getThreadsContainer() {
        final JXTreeTable threads = new JXTreeTable();
        threads.setAutoCreateColumnsFromModel(false);
        threads.setTreeTableModel(model);
        threads.setEditable(false);
        threads.setShowsRootHandles(true);
        threads.setSortable(false);
        threads.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        threads.setRowSelectionAllowed(true);
        threads.setColumnSelectionAllowed(false);
        threads.setScrollsOnExpand(true);
        threads.setRootVisible(modelControl.isRootVisible());
        threads.setToggleClickCount(2);

        threads.setDefaultRenderer(APostProxy.class, new PostTableCellRenderer());
        threads.setTreeCellRenderer(new PostTreeCellRenderer());

        threads.addTreeSelectionListener(new PostSelector());
        threads.addTreeExpansionListener(new ThreadExpander());
        threads.addMouseListener(new ItemListener());

        // Restore selection on any table data changing.
        threads.getModel().addTableModelListener(new SelectionHolder());

        threads.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        for (Header h : Header.values()) {
            TableColumnExt column = new TableColumnExt(h.ordinal());
            if (h.getWidth() > 0) {
                column.setPreferredWidth(h.getWidth());
                column.setMaxWidth(h.getWidth() << 2);
            }
            column.setMinWidth(10);
            column.setIdentifier(h);
            column.setToolTipText(h.getTitle());
            threads.addColumn(column);
        }

        // Handle keyboard events to emulate tree navigation in TreeTable
        threads.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0), "parentOrCollapse");
        threads.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0), "nextOrExpand");

        threads.getActionMap().put("parentOrCollapse", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int row = threads.getSelectedRow();
                if (row == -1) {
                    return;
                }

                if (threads.isExpanded(row)) {
                    threads.collapseRow(row);
                    threads.scrollRowToVisible(row);
                } else if (row > 0) {
                    TreePath pathForRow = threads.getPathForRow(row);
                    if (pathForRow != null) {
                        TreePath parent = pathForRow.getParentPath();
                        if (parent != null) {
                            row = threads.getRowForPath(parent);

                            if (row >= 0) {
                                threads.setRowSelectionInterval(row, row);
                                threads.scrollRowToVisible(row);
                            }
                        }
                    }
                }
            }
        });
        threads.getActionMap().put("nextOrExpand", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int row = threads.getSelectedRow();
                if (row == -1) {
                    return;
                }

                if (!threads.isExpanded(row) && !model.isLeaf(threads.getPathForRow(row).getLastPathComponent())) {
                    threads.expandRow(row);
                    threads.scrollRowToVisible(row);
                } else if (row < threads.getRowCount() - 1) {
                    threads.scrollRowToVisible(row + 1);
                    threads.setRowSelectionInterval(row + 1, row + 1);
                }
            }
        });

        return threads;
    }

    protected void selectItem(Post post, boolean collapseChildren) {
        if (post != null) {
            TreePath path = model.getPathToRoot(post);

            if (model.isPathValid(path)) {
                TreePath parentPath = path.getParentPath();

                if (parentPath != null && threads.isCollapsed(parentPath)) {
                    threads.expandPath(parentPath);
                }
                scrollPathToVisible(path);

                if (collapseChildren) {
                    threads.collapsePath(path);
                }

                selectedItem = post.getMessageData();
                return;
            }
        }

        selectedItem = null;
        threads.clearSelection();
    }

    protected void scrollPathToVisible(TreePath path) {
        int row = threads.getRowForPath(path);
        if (row >= 0) {
            threads.setRowSelectionInterval(row, row);
            Rectangle bounds = threads.getCellRect(
                    row,
                    threads.convertColumnIndexToView(threads.getHierarchicalColumn()),
                    true);
            threads.scrollRectToVisible(bounds);
        }
        threads.scrollPathToVisible(path);
    }

    protected Post getSelectedItem() {
        int selectedRow = threads.getSelectedRow();
        if (selectedRow < 0) {
            return null;
        }

        TreePath path = threads.getPathForRow(selectedRow);
        return path == null ? null : (Post) path.getLastPathComponent();
    }

    private JToolBar createToolBar() {
        ThreadToolbarActions[] actions = modelControl.getToolbar();

        if (actions == null) {
            return null;
        }

        JToolBar toolBar = new JToolBar();

        for (ThreadToolbarActions c : actions) {
            if (c != null) {
                toolBar.add(c.makeButton(this));
            } else {
                toolBar.addSeparator();
            }
        }

        return toolBar;
    }

    @Override
    public void loadItem(int forumId) {
        this.rootItemId = forumId;

        modelControl.fillModelByItemId(model, forumId);
    }

    @Override
    public ThreadState getObjectState() {
        assert RojacUtils.checkThread(true);

        int messageId = selectedItem == null ? 0 : selectedItem.getMessageId();

        return new ThreadState(messageId);
    }

    @Override
    public void setObjectState(IState state) {
        assert RojacUtils.checkThread(true);

        if (state == null) {
            return;
        }
        if (!(state instanceof ThreadState)) {
            RojacUtils.fireDebugException("Invalid state object " + state.toString() + " [" + state.getClass() + "]");
            return;
        }

        this.state = (ThreadState) state;

        applyState();
    }

    @Override
    public void makeVisible(int messageId) {
        setObjectState(new ThreadState(messageId));
    }

    @Override
    public boolean containsItem(int messageId) {
        return modelControl.allowSearch() && model.getRoot().getMessageById(messageId) != null;
    }

    @Override
    public String getTabTitle() {
        return modelControl.getTitle(model);
    }

    @Override
    public JPopupMenu getTabTitleMenu() {
        return modelControl.getTitlePopup(model, appControl);
    }

    @Override
    public Icon getTabTitleIcon() {
        return modelControl.getTitleIcon(model);
    }

    protected Object getToolbarPlacement() {
        return toolbar == null ? 0 : ((BorderLayout) getLayout()).getConstraints(toolbar);
    }

    @Override
    public TableThreadViewLayout storeLayout() {
        TableColumnModel cm = threads.getColumnModel();

        TableThreadViewLayout.Column[] columns = new TableThreadViewLayout.Column[Header.values().length];
        int i = 0;

        for (Header h : Header.values()) {
            TableColumn c = cm.getColumn(h.ordinal());

            int width = c.getWidth();
            int columnIndex = cm.getColumnIndex(h);

            columns[i++] = new TableThreadViewLayout.Column(h, columnIndex, width);
        }

        return new TableThreadViewLayout(
                toolbar == null ? 0 : toolbar.getOrientation(),
                splitPane.getDividerLocation(),
                getToolbarPlacement(),
                columns);
    }

    @Override
    public void setupLayout(IViewLayout o) {
        if (o instanceof TableThreadViewLayout) {
            layout = (TableThreadViewLayout) o;

            if (toolbar != null) {
                remove(toolbar);
                toolbar.setOrientation(layout.getToolbarOrientation());
                add(toolbar, layout.getToolbarPosition());
            }

            splitPane.setDividerLocation(layout.getDividerLocation());

            TableColumnModel cm = threads.getColumnModel();

            for (TableThreadViewLayout.Column c : layout.getColumns()) {
                int idx = cm.getColumnIndex(c.getAnchor());

                cm.getColumn(idx).setWidth(c.getWidth());
                cm.getColumn(idx).setPreferredWidth(c.getWidth());
                cm.moveColumn(idx, c.getIndex());
            }
        }

    }

    protected final void selectItem(Post post) {
        selectItem(post, false);
    }

    public void selectNextPost(int postId) {
        Post post = model.getRoot().getMessageById(postId);

        if (post != null) {
            selectNextPost(post, true);
        }
    }

    private void applyState() {
        assert RojacUtils.checkThread(true);

        if (state == null) {
            return;
        }

        final int messageId = state.openedMessageId();

        if (!model.isInitialized()) {
            // Forum not yet loaded.
            model.addTreeModelListener(new ForumLoadWaiter(messageId));
        } else {
            expandThread(messageId);
        }
    }

    private void selectNextPost(Post currentPost, boolean unread) {
        selectNextPost(currentPost, unread, new LinkedList<Post>());
    }

    private void selectNextPost(Post currentPost, boolean unread, Collection<Post> toCollapse) {
        Post next = currentPost;
        boolean skip;
        do {
            next = findNextPost(next, 0, unread, toCollapse);

            skip = PostUtils.isPostIgnoredByUser(next);
        } while (skip);
        if (next != null) {
            selectItem(next);

            if (!toCollapse.isEmpty()) {
                for (Post post : toCollapse) {
                    threads.collapsePath(model.getPathToRoot(post));
                }

                scrollPathToVisible(model.getPathToRoot(next));
            }
        }
    }

    private void selectPrevPost(Post currentPost, boolean unread) {
        Post prev = currentPost;
        boolean skip;
        do {
            prev = findPrevPost(prev, unread);

            skip = PostUtils.isPostIgnoredByUser(prev);
        } while (skip);

        if (prev != null) {
            selectItem(prev);
        }
    }

    private Post findNextPost(Post post, int idx, boolean unread, Collection<Post> toCollapse) {
        if (post == null) {
            post = model.getRoot();
            if (post.getSize() == 0) {
                return null;
            }
        }

        boolean ignoreSubUserThread = Property.SKIP_IGNORED_USER_THREAD.get();

        if (post.isIgnored()) {
            return jumpNextParent(post, unread, toCollapse);
        }

        if (post.getLoadingState() == LoadingState.NotLoaded) {
            if (!unread || post.isRead() != ReadStatus.Read) {
                // Has unread children but their have not loaded yet.
                modelControl.loadThread(model, post, new LoadNextPost(post, unread, toCollapse));
                // Change post selection when children are loaded
                return null;
            }
        }

        if (post.getLoadingState() == LoadingState.Loaded && idx >= post.getSize() ||
                (unread && post.isRead() == ReadStatus.Read)) {
            // All items in the sub-tree are read.
            // Go to parent and search again
            return jumpNextParent(post, unread, toCollapse);
        }

        int i = idx;
        while (i < post.getSize()) {
            Post p = post.getChild(i);

            if (p.isIgnored()) {
                i++;
                continue;
            }

            if (!unread && !(ignoreSubUserThread && p.isIgnoredUser())) {
                return p;
            }

            switch (p.isRead()) {
                case Read:
                    // Go to next child of post
                    i++;
                    break;
                case ReadPartially:
                    switch (p.getLoadingState()) {
                        case Loaded:
                            // Go deep to next child of the child
                            i = 0;
                            post = p;
                            break;
                        case NotLoaded:
                            modelControl.loadThread(model, p, new LoadNextPost(p, unread, toCollapse));
                        case Loading:
                            return null;
                    }
                    break;
                case Unread:
                    if (ignoreSubUserThread && p.isIgnoredUser()) {
                        i++;
                    } else {
                        return p;
                    }
                    break;
            }
        }

        // Thread has unread posts only before the selected post. Go to parent and continue the search.
        Post parent = post.getParent();
        if (unread && parent != null) {
            int nextIdx = parent.getIndex(post) + 1;
            return findNextPost(parent, nextIdx, unread, toCollapse);
        } else {
            return null;
        }
    }

    private Post jumpNextParent(Post post, boolean unread, Collection<Post> toCollapse) {
        Post parent = post.getParent();
        if (parent != null) {
            int nextIdx = parent.getIndex(post) + 1;
            if (Property.VIEW_THREAD_COLLAPSE_THREADS_AFTER_GO2NEXT.get()) {
                toCollapse.add(post);
            }
            return findNextPost(parent, nextIdx, unread, toCollapse);
        } else {
            return null;
        }
    }

    private Post findPrevPost(Post post, boolean unread) {
        try {
            if (post == null) {
                return findLastPost(model.getRoot(), unread);
            }

            Post parent = post.getParent();
            if (parent == null) {
                return null;
            }

            int idx = parent.getIndex(post) - 1;

            if (post.isIgnored()) {
                if (idx >= 0) {
                    return findPrevPost(parent.getChild(idx), unread);
                } else {
                    return findPrevPost(parent, unread);
                }
            }

            if (unread) {
                while (idx >= 0) {
                    Post p = parent.getChild(idx);
                    if (p.isIgnored()) {
                        idx--;
                        continue;
                    }

                    switch (p.isRead()) {
                        case Read:
                            idx--;
                            break;
                        case ReadPartially:
                        case Unread:
                            return findLastPost(p, unread);
                    }
                }

                switch (parent.isRead()) {
                    case Read:
                    case ReadPartially:
                        return findPrevPost(parent, true);
                    case Unread:
                        return parent;
                }
            } else if (idx >= 0) {
                return findLastPost(parent.getChild(idx), unread);
            } else {
                return parent;
            }
        } catch (RuntimeException e) {
            // Just go through to restart search later
        }

        return null;
    }

    /**
     * Searches for the last unread post in the tree thread.
     *
     * @param post   root of sub-tree.
     * @param unread if set to true - unread post will be searched.
     * @return last unread post in sub-tree or <code>null</code> if no unread post is exist in sub-tree.
     * @throws RuntimeException will be thrown in case when data loading is needed to make correct search.
     */
    private Post findLastPost(Post post, boolean unread) throws RuntimeException {
        boolean ignoreSubUserThread = Property.SKIP_IGNORED_USER_THREAD.get();

        if (unread && post.isRead() == ReadStatus.Read || post.isIgnored()) {
            return null;
        }

        switch (post.getLoadingState()) {
            case NotLoaded:
                modelControl.loadThread(model, post, new LoadPreviousPost(post, unread));
            case Loading:
                throw new RojacDebugException("Restart search later");
        }

        int idx = post.getSize() - 1;
        while (idx >= 0) {
            Post p = findLastPost(post.getChild(idx), unread);
            if (p != null) {
                return p;
            }
            idx--;
        }

        if (unread && post.isRead() != ReadStatus.Unread ||
                post.isIgnored() ||
                (ignoreSubUserThread && post.isIgnoredUser())
                ) {
            return null;
        }

        return post;
    }

    private void expandThread(final int messageId) {
        // Check for threads
        Post post = model.getRoot().getMessageById(messageId);
        if (post != null) {
            selectItem(post);
        } else {
            new ThreadChecker(messageId).execute();
        }
    }

    private void resortAndReloadModel() {
        boolean skipSaveState = Property.VIEW_THREAD_COLLAPSE_THREADS_AFTER_SYNC.get();

        @SuppressWarnings("unchecked")
        Enumeration<TreePath> expanded = skipSaveState ? null : (Enumeration<TreePath>) threads.getExpandedDescendants(model.getPathToRoot(model.getRoot()));

        resortingModel = true;
        modelControl.resortModel(model);
        resortingModel = false;

        if (expanded != null) {
            while (expanded.hasMoreElements()) {
                threads.expandPath(expanded.nextElement());
            }
        }

        if (selectedItem != null) {
            Post selected = model.getRoot().getMessageById(selectedItem.getMessageId());
            selectItem(selected);
        } else {
            selectItem(null);
        }
    }

    @Override
    public final void processPacket(IPacket packet) {
        if (packet instanceof ReloadDataPacket) {
            loadItem(getId().getId());

            return;
        }

        // Just in case store a current selection
        final Post curSelection = getSelectedItem();
        Runnable postProcessor = new Runnable() {
            public void run() {
                if (curSelection != null &&
                        !curSelection.equals(getSelectedItem())) {
                    selectItem(curSelection);
                } else {
                    selectItem(null);
                }
            }
        };

        if (model.isInitialized()) {
            modelControl.processPacket(model, packet, postProcessor);
        }
    }

    private class SelectionHolder implements TableModelListener {
        @Override
        public void tableChanged(TableModelEvent e) {
            if (e.getType() == TableModelEvent.UPDATE &&
                    !(e.getColumn() == TableModelEvent.ALL_COLUMNS &&
                            e.getFirstRow() == TableModelEvent.HEADER_ROW &&
                            e.getLastRow() == TableModelEvent.HEADER_ROW)) {
                return;
            }

            ThreadState s = getObjectState();
            if (s.openedMessageId() == 0) {
                return;
            }

            Post post = model.getRoot().getMessageById(s.openedMessageId());
            if (post == null) {
                return;
            }

            final TreePath pathToRoot = model.getPathToRoot(post);
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    scrollPathToVisible(pathToRoot);
                }
            });
        }
    }

    // Toolbar possible actions
    class NewThreadAction extends AButtonAction {
        public NewThreadAction() {
            super(ShortCut.NewThread);
        }

        public void actionPerformed(ActionEvent e) {
            appControl.editMessage(rootItemId, null);
        }
    }

    class PreviousUnreadAction extends AButtonAction {
        public PreviousUnreadAction() {
            super(ShortCut.PrevUnreadMessage);
        }

        public void actionPerformed(ActionEvent e) {
            Post currentPost = getSelectedItem();
            selectPrevPost(currentPost, true);
        }
    }

    class NextUnreadAction extends AButtonAction {
        public NextUnreadAction() {
            super(ShortCut.NextUnreadMessage);
        }

        public void actionPerformed(ActionEvent e) {
            Post currentPost = getSelectedItem();
            selectNextPost(currentPost, true);
        }
    }

    class PreviousAction extends AButtonAction {
        public PreviousAction() {
            super(ShortCut.PrevMessage);
        }

        public void actionPerformed(ActionEvent e) {
            Post currentPost = getSelectedItem();
            selectPrevPost(currentPost, false);
        }
    }

    class NextAction extends AButtonAction {
        public NextAction() {
            super(ShortCut.NextMessage);
        }

        public void actionPerformed(ActionEvent e) {
            Post currentPost = getSelectedItem();
            selectNextPost(currentPost, false);
        }
    }

    class MarkSubTreeReadAction extends AButtonAction {
        public MarkSubTreeReadAction() {
            super(ShortCut.MarkSubTreeRead);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            Post post = getSelectedItem();
            if (post == null) {
                return;
            }

            if (post instanceof org.xblackcat.rojac.gui.view.model.Thread) {
                // Mark whole thread.
                new ThreadReadFlagSetter(true, post.getMessageData()).execute();
            } else {
                // Mark sub-tree as read
                new SubTreeReadFlagSetter(true, post).execute();
            }

        }
    }

    class MarkWholeThreadReadAction extends AButtonAction {
        public MarkWholeThreadReadAction() {
            super(ShortCut.MarkWholeThreadRead);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            Post post = getSelectedItem();
            if (post == null) {
                return;
            }

            // Mark whole thread.
            new ThreadReadFlagSetter(true, post.getThreadRoot().getMessageData()).execute();
        }
    }

    class ToThreadRootAction extends AButtonAction {
        public ToThreadRootAction() {
            super(ShortCut.ToThreadRoot);
        }

        public void actionPerformed(ActionEvent e) {
            Post currentPost = getSelectedItem();
            if (currentPost != null) {
                selectItem(modelControl.getTreeRoot(currentPost), true);
            }

        }
    }

    private class LoadNextPost implements Runnable {
        private final Post item;
        private final boolean unread;
        private Collection<Post> toCollapse;

        public LoadNextPost(Post item, boolean unread, Collection<Post> toCollapse) {
            this.item = item;
            this.unread = unread;
            this.toCollapse = toCollapse;
        }

        @Override
        public void run() {
            selectNextPost(item, unread, toCollapse);
        }
    }

    private class LoadPreviousPost implements Runnable {
        private final Post item;
        private final boolean unread;

        public LoadPreviousPost(Post item, boolean unread) {
            this.item = item;
            this.unread = unread;
        }

        @Override
        public void run() {
            Post prevUnread = findLastPost(item, unread);
            if (prevUnread != null) {
                selectItem(prevUnread);
            }
        }
    }

    private class ThreadChecker extends MessageChecker {
        public ThreadChecker(int messageId) {
            super(messageId);
        }

        @Override
        protected void done() {
            if (data != null) {
                final Post root = model.getRoot();
                Post rootMessage = root.getMessageById(data.getThreadRootId());
                if (rootMessage != null) {
                    modelControl.loadThread(model, rootMessage, new Runnable() {
                        @Override
                        public void run() {
                            selectItem(root.getMessageById(messageId));
                        }
                    });
                }
            } else {
                if (sourceStackTrace != null) {
                    log.error("Can't load message #" + messageId, sourceStackTrace);
                }
                appControl.closeTab(getId());
            }
        }
    }

    private class DataIntegrityMonitor implements TreeModelListener {
        @Override
        public void treeNodesChanged(TreeModelEvent e) {
            Post root = model.getRoot();
            if (e.getTreePath().getLastPathComponent() == root) {
                fireInfoChanged();
            }
        }

        @Override
        public void treeNodesInserted(TreeModelEvent e) {
        }

        @Override
        public void treeNodesRemoved(TreeModelEvent e) {
        }

        @Override
        public void treeStructureChanged(TreeModelEvent e) {
            Post root = model.getRoot();
            if (root == null) {
                TreeTableThreadView.this.appControl.closeTab(getId());
            } else {
                threads.setRootVisible(modelControl.isRootVisible());

                if (e.getTreePath() == null) {
                    // Resort model nodes
                    resortAndReloadModel();
                }

                fireInfoChanged();
            }
        }
    }

    protected class PostSelector implements TreeSelectionListener {
        public void valueChanged(TreeSelectionEvent e) {
            if (!resortingModel) {
                TreePath newLeadSelectionPath = e.getNewLeadSelectionPath();

                if (newLeadSelectionPath != null) {
                    if (newLeadSelectionPath.getLastPathComponent() != null) {
                        selectedItem = ((Post) newLeadSelectionPath.getLastPathComponent()).getMessageData();

                        // Load message text
                        new PostLoader(selectedItem).execute();
                    }
                }
            }

            fireViewStateChanged();
        }
    }

    protected class ThreadExpander implements TreeExpansionListener {
        public void treeExpanded(TreeExpansionEvent event) {
            final TreePath path = event.getPath();

            Post item = (Post) path.getLastPathComponent();

            if (item.getLoadingState() == LoadingState.NotLoaded) {
                modelControl.loadThread(model, item, new Runnable() {
                    @Override
                    public void run() {
                        threads.expandPath(path);
                    }
                });
            }

            if (item.getLoadingState() == LoadingState.Loaded) {
                if (item.getSize() == 1) {
                    Post child = item.getChild(0);

                    threads.expandPath(path.pathByAddingChild(child));
                }
            }
        }

        public void treeCollapsed(TreeExpansionEvent event) {
            final TreePath path = event.getPath();

            Post item = (Post) path.getLastPathComponent();

            if (item.getLoadingState() == LoadingState.Loaded) {
                if (item.getThreadRoot() == item) {
                    // Remove thread content after collapse.

                    modelControl.unloadThread(model, item);
                }
            }
        }
    }

    protected class ItemListener extends PopupMouseAdapter {
        @Override
        protected void triggerDoubleClick(MouseEvent e) {
            Point p = e.getPoint();

            TreePath path = threads.getPathForLocation(p.x, p.y);

            if (path != null) {
                Post mi = (Post) path.getLastPathComponent();

                modelControl.onDoubleClick(mi, appControl);
            }
        }

        @Override
        protected void triggerPopup(MouseEvent e) {
            Point p = e.getPoint();

            TreePath path = threads.getPathForLocation(p.x, p.y);

            if (path != null) {
                Post mi = (Post) path.getLastPathComponent();

                JPopupMenu m = modelControl.getItemMenu(mi, appControl);

                if (m != null) {
                    m.show(e.getComponent(), p.x, p.y);
                }
            }
        }
    }

    /**
     * Util class: waits until forum will be loaded and then select specified message in thread view
     */
    private class ForumLoadWaiter implements TreeModelListener {
        private final int messageId;

        public ForumLoadWaiter(int messageId) {
            this.messageId = messageId;
        }

        @Override
        public void treeNodesChanged(TreeModelEvent e) {
        }

        @Override
        public void treeNodesInserted(TreeModelEvent e) {
        }

        @Override
        public void treeNodesRemoved(TreeModelEvent e) {
        }

        @Override
        public void treeStructureChanged(TreeModelEvent e) {
            if (e.getPath() == null && model.isInitialized() && model.getRoot() != null) {
                model.removeTreeModelListener(this);
                if (messageId != 0) {
                    expandThread(messageId);
                } else {
                    selectItem(null);
                }
            }
        }
    }

    private class PostLoader extends RojacWorker<Void, MessageDataHolder> {
        private final int messageId;
        private final MessageData messageData;

        public PostLoader(int messageId) {
            this.messageId = messageId;
            messageData = null;
        }

        public PostLoader(MessageData messageData) {
            this.messageId = messageData.getMessageId();
            this.messageData = messageData;
        }

        @Override
        protected Void perform() throws Exception {
            if (messageId != 0) {
                String messageBody;
                MessageData messageData = this.messageData;
                try {
                    if (messageId > 0) {
                        // Regular message
                        IMessageAH messageAH = Storage.get(IMessageAH.class);
                        if (messageData == null) { // Not yet loaded
                            messageData = messageAH.getMessageData(messageId);
                            if (messageData == null) {
                                // Somehow message is not found - do not load it
                                return null;
                            }
                        }
                        messageBody = messageAH.getMessageBodyById(messageId);
                    } else {
                        // Local message
                        NewMessage newMessage = Storage.get(INewMessageAH.class).getNewMessageById(-messageId);
                        messageData = new NewMessageData(newMessage);

                        messageBody = newMessage.getMessage();
                    }
                } catch (StorageException e) {
                    throw new RuntimeException("Can't load message #" + messageId, e);
                }

                try {
                    publish(new MessageDataHolder(messageData, messageBody));
                } catch (Exception e) {
                    throw new RuntimeException("Can't parse message #" + messageId + ". Body: " + messageBody, e);
                }
            } else {
                publish(new MessageDataHolder(null, null));
            }

            return null;
        }

        @Override
        protected void process(java.util.List<MessageDataHolder> chunks) {
            for (MessageDataHolder md : chunks) {
                selectedItem = md.getMessage();
                messagePane.fillFrame(selectedItem, md.getMessageBody());
                threads.requestFocus();
                if (selectedItem == null) {
                    continue;
                }

                if (!messageData.isRead()) {
                    Long delay = Property.VIEW_THREAD_AUTOSET_READ.get();
                    if (delay != null && delay >= 0) {
                        MessageUtils.markMessageRead(getId(), messageData, delay);
                    }
                }
            }
        }
    }
}
