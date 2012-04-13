package org.xblackcat.rojac.gui.view.navigation;

import org.jdesktop.swingx.tree.TreeModelSupport;
import org.jdesktop.swingx.treetable.TreeTableModel;
import org.xblackcat.rojac.gui.view.model.FavoriteType;
import org.xblackcat.rojac.service.datahandler.*;

import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreePath;
import java.util.ArrayList;
import java.util.Collection;

/**
 * @author xBlackCat
 */
class NavigationModel implements TreeTableModel, IModelControl {
    private final TreeModelSupport support = new TreeModelSupport(this);
    private final AnItem root;

    private final PacketDispatcher packetDispatcher = new PacketDispatcher(
            new IPacketProcessor<IgnoreUserUpdatedPacket>() {
                @Override
                public void process(IgnoreUserUpdatedPacket p) {
                    new LoadTaskExecutor(
                            forumDecorator.reloadForums(),
                            personalDecorator.reloadInfo(false)
                    ).execute();
                }
            },
            new IPacketProcessor<FavoritesUpdatedPacket>() {
                @Override
                public void process(FavoritesUpdatedPacket p) {
                    new LoadTaskExecutor(
                            favoritesDecorator.reloadFavorites()
                    ).execute();
                }
            },
            new IPacketProcessor<FavoriteCategoryUpdatedPacket>() {
                @Override
                public void process(FavoriteCategoryUpdatedPacket p) {
                    new LoadTaskExecutor(
                            favoritesDecorator.updateFavoriteData(FavoriteType.Category)
                    ).execute();
                }
            },
            new IPacketProcessor<NewMessagesUpdatedPacket>() {
                @Override
                public void process(NewMessagesUpdatedPacket p) {
                    new LoadTaskExecutor(
                            personalDecorator.reloadOutbox()
                    ).execute();
                }
            },
            new IPacketProcessor<IgnoreUpdatedPacket>() {
                @Override
                public void process(IgnoreUpdatedPacket p) {
                    new LoadTaskExecutor(
                            forumDecorator.loadForumStatistic(p.getForumId()),
                            personalDecorator.reloadInfo(false),
                            personalDecorator.reloadIgnored()
                    ).execute();
                }
            },
            new IPacketProcessor<SetForumReadPacket>() {
                @Override
                public void process(SetForumReadPacket p) {
                    new LoadTaskExecutor(
                            personalDecorator.reloadInfo(false),
                            favoritesDecorator.updateFavoriteData(null),
                            forumDecorator.loadForumStatistic(p.getForumId())
                    ).execute();
                }
            },
            new IPacketProcessor<ForumsUpdated>() {
                @Override
                public void process(ForumsUpdated p) {
                    new LoadTaskExecutor(
                            forumDecorator.reloadForums()
                    ).execute();
                }
            },
            new IPacketProcessor<IForumUpdatePacket>() {
                @Override
                public void process(IForumUpdatePacket p) {
                    new LoadTaskExecutor(
                            personalDecorator.reloadInfo(true),
                            favoritesDecorator.updateFavoriteData(null),
                            forumDecorator.loadForumStatistic(p.getForumIds())
                    ).execute();
                }
            },
            new IPacketProcessor<SetSubThreadReadPacket>() {
                @Override
                public void process(SetSubThreadReadPacket p) {
                    new LoadTaskExecutor(
                            personalDecorator.reloadInfo(false),
                            favoritesDecorator.updateFavoriteData(null),
                            forumDecorator.loadForumStatistic(p.getForumId())
                    ).execute();
                }
            },
            new IPacketProcessor<SetPostReadPacket>() {
                @Override
                public void process(SetPostReadPacket p) {
                    new LoadTaskExecutor(
                            favoritesDecorator.alterReadStatus(p.getPost(), p.isRead()),
                            forumDecorator.alterReadStatus(p.getPost(), p.isRead()),
                            personalDecorator.alterReadStatus(p.getPost(), p.isRead())
                    ).execute();
                }
            },
            new IPacketProcessor<SubscriptionChangedPacket>() {
                @Override
                public void process(SubscriptionChangedPacket p) {
                    Collection<ILoadTask> tasks = new ArrayList<>();
                    for (SubscriptionChangedPacket.Subscription s : p.getNewSubscriptions()) {
                        ILoadTask task = forumDecorator.updateSubscribed(s.getForumId(), s.isSubscribed());
                        if (task != null) {
                            tasks.add(task);
                        }
                    }

                    if (!tasks.isEmpty()) {
                        new LoadTaskExecutor(tasks).execute();
                    }
                }
            },
            new IPacketProcessor<ReloadDataPacket>() {
                @Override
                public void process(ReloadDataPacket p) {
                    new LoadTaskExecutor(
                            personalDecorator.reloadIgnored(),
                            personalDecorator.reloadInfo(true),
                            forumDecorator.reloadForums(),
                            favoritesDecorator.reloadFavorites()
                    ).execute();
                }
            }
    );

    final ForumDecorator forumDecorator = new ForumDecorator(this);
    final FavoritesDecorator favoritesDecorator = new FavoritesDecorator(this);
    final PersonalDecorator personalDecorator = new PersonalDecorator(this);

    NavigationModel() {

        root = new RootItem(
                personalDecorator,
                forumDecorator,
                favoritesDecorator
        );
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return AnItem.class;
    }

    @Override
    public int getColumnCount() {
        return 2;
    }

    @Override
    public String getColumnName(int column) {
        return null;
    }

    @Override
    public int getHierarchicalColumn() {
        return 0;
    }

    @Override
    public AnItem getValueAt(Object node, int column) {
        return (AnItem) node;
    }

    @Override
    public boolean isCellEditable(Object node, int column) {
        return false;
    }

    @Override
    public void setValueAt(Object value, Object node, int column) {
    }

    @Override
    public AnItem getRoot() {
        return root;
    }

    @Override
    public Object getChild(Object parent, int index) {
        return ((AnItem) parent).getChild(index);
    }

    @Override
    public int getChildCount(Object parent) {
        return ((AnItem) parent).getChildCount();
    }

    @Override
    public boolean isLeaf(Object node) {
        assert node instanceof AnItem;
        return !((AnItem) node).isGroup();
    }

    @Override
    public void valueForPathChanged(TreePath path, Object newValue) {
    }

    @Override
    public int getIndexOfChild(Object parent, Object child) {
        if (parent == null || child == null) {
            return -1;
        }

        assert parent instanceof AnItem;
        assert child instanceof AnItem;

        AnItem c = (AnItem) child;
        AnItem p = (AnItem) parent;

        if (c.getParent() != parent) {
            // Only strict equality!!
            return -1;
        }

        if (!p.isGroup()) {
            return -1;
        }

        // Not null!
        return p.indexOf(c);
    }

    @Override
    public void addTreeModelListener(TreeModelListener l) {
        support.addTreeModelListener(l);
    }

    @Override
    public void removeTreeModelListener(TreeModelListener l) {
        support.removeTreeModelListener(l);
    }

    TreePath getPathToRoot(AnItem aNode) {
        return new TreePath(getPathToRoot(aNode, 0));
    }

    AnItem[] getPathToRoot(AnItem aNode, int depth) {
        AnItem[] retNodes;
        // This method recurses, traversing towards the root in order
        // size the array. On the way back, it fills in the nodes,
        // starting from the root and working back to the original node.

        /* Check for null, in case someone passed in a null node, or
           they passed in an element that isn't rooted at root. */
        if (aNode == null) {
            if (depth == 0) {
                return null;
            } else {
                retNodes = new AnItem[depth];
            }
        } else {
            depth++;
            if (aNode == root) {
                retNodes = new AnItem[depth];
            } else {
                retNodes = getPathToRoot(aNode.getParent(), depth);
            }
            retNodes[retNodes.length - depth] = aNode;
        }
        return retNodes;
    }

    // Helper methods
    @Override
    public <T extends AnItem> void safeRemoveChild(AGroupItem<T> parent, T child) {
        int idx = parent.indexOf(child);
        if (idx != -1) {
            AnItem removed = parent.remove(idx);
            support.fireChildRemoved(getPathToRoot(parent), idx, removed);
        }
    }

    /**
     * Add a child to item and notify listeners about this.
     *
     * @param parent parent item to add child to
     * @param child  a new child item
     */
    @Override
    public <T extends AnItem> void addChild(AGroupItem<T> parent, T child) {
        int idx = parent.add(child);

        support.fireChildAdded(getPathToRoot(parent), idx, child);
    }

    /**
     * Notify listeners that item was updated (and whole path to it too)
     *
     * @param item updated item
     */
    @Override
    public void itemUpdated(AnItem item) {
        TreePath path = getPathToRoot(item);
        while (path != null) {
            support.firePathChanged(path);
            path = path.getParentPath();
        }
    }

    @Override
    public void removeAllChildren(AGroupItem parent) {
        parent.clear();
        support.fireTreeStructureChanged(getPathToRoot(parent));
    }

    public void dispatch(IPacket packet) {
        packetDispatcher.dispatch(packet);
    }
}
