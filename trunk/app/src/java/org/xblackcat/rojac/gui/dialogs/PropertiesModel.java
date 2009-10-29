package org.xblackcat.rojac.gui.dialogs;

import org.jdesktop.swingx.tree.TreeModelSupport;
import org.xblackcat.rojac.service.options.IValueChecker;

import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

/**
* @author xBlackCat
*/
public class PropertiesModel implements TreeModel {
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
    public void valueForPathChanged(TreePath path, Object newValue) {
        PropertyNode n = (PropertyNode) path.getLastPathComponent();

        IValueChecker checker = n.getProperty().getChecker();
        if (checker == null || checker.isValueCorrect(newValue)) {
            n.setValue(newValue);

            modelSupport.firePathChanged(path);
        }
    }

    @Override
    public int getIndexOfChild(Object parent, Object child) {
        PropertyNode n = (PropertyNode) parent;
        return n.indexOf((PropertyNode) child);
    }

    @Override
    public void addTreeModelListener(TreeModelListener l) {
        modelSupport.addTreeModelListener(l);
    }

    @Override
    public void removeTreeModelListener(TreeModelListener l) {
        modelSupport.removeTreeModelListener(l);
    }
}
