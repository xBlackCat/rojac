package org.xblackcat.rojac.gui.dialog.options;

import org.jdesktop.swingx.tree.TreeModelSupport;
import org.xblackcat.rojac.service.options.IValueChecker;
import org.xblackcat.rojac.service.options.Property;

import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import java.util.Collection;
import java.util.LinkedList;

/**
 * @author xBlackCat
 */
class PropertiesModel implements TreeModel {
    private final PropertyNode root;
    private final TreeModelSupport modelSupport = new TreeModelSupport(this);

    public PropertiesModel(PropertyNode root) {
        this.root = root;
    }

    @Override
    public PropertyNode getRoot() {
        return root;
    }

    @Override
    public PropertyNode getChild(Object parent, int index) {
        PropertyNode n = (PropertyNode) parent;
        return n.getChild(index);
    }

    @Override
    public int getChildCount(Object parent) {
        PropertyNode n = (PropertyNode) parent;
        return n.childrenCount();
    }

    @Override
    public boolean isLeaf(Object node) {
        PropertyNode n = (PropertyNode) node;
        return n.isEmpty();
    }

    @Override
    @SuppressWarnings({"unchecked"})
    public void valueForPathChanged(TreePath path, Object newValue) {
        PropertyNode n = (PropertyNode) path.getLastPathComponent();

        IValueChecker checker = n.getProperty().getChecker();
        if (checker == null || checker.isValueCorrect(newValue)) {
            n.setValue(newValue);

            modelSupport.firePathChanged(path);
        }
    }

    @Override
    @SuppressWarnings({"unchecked"})
    public int getIndexOfChild(Object parent, Object child) {
        PropertyNode n = (PropertyNode) parent;
        return n.indexOf((PropertyNode<?>) child);
    }

    @Override
    public void addTreeModelListener(TreeModelListener l) {
        modelSupport.addTreeModelListener(l);
    }

    @Override
    public void removeTreeModelListener(TreeModelListener l) {
        modelSupport.removeTreeModelListener(l);
    }

    /**
     * Store all new values in system properties.
     */
    public Collection<Property<?>> applySettings() {
        Collection<Property<?>> changedProperties = new LinkedList<>();
        affect(changedProperties, root, false);

        return changedProperties;
    }

    public Collection<Property<?>> revertSettings() {
        Collection<Property<?>> changedProperties = new LinkedList<>();
        affect(changedProperties, root, true);

        return changedProperties;
    }

    /**
     * Apply or revert properties recoursivly.
     *
     * @param changedProperties
     * @param n                 node to recoursivly affect.
     * @param revert            <code>true</code> - revert value; <code>false</code> - apply value
     */
    private void affect(Collection<Property<?>> changedProperties, PropertyNode n, boolean revert) {
        if (n == null) {
            return;
        }

        if (n.isChanged()) {
            changedProperties.add(n.getProperty());

            if (revert) {
                n.revert();
                modelSupport.firePathChanged(n.getPath());
            } else {
                n.apply();
            }
        }

        int count = n.childrenCount();

        for (int i = 0; i < count; i++) {
            affect(changedProperties, n.getChild(i), revert);
        }
    }
}
