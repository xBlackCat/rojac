package org.xblackcat.rojac.gui.dialogs;

import org.xblackcat.rojac.service.ServiceFactory;
import org.xblackcat.rojac.service.options.IOptionsService;
import org.xblackcat.rojac.service.options.Property;

import javax.swing.tree.TreePath;
import java.util.ArrayList;
import java.util.List;

/**
* @author xBlackCat
*/
public class PropertyNode {
    private final Property property;
    private final String name;
    private PropertyNode parent;

    private Object value;

    private final List<PropertyNode> children = new ArrayList<PropertyNode>();
    private static final IOptionsService OPTIONS_SERVICE = ServiceFactory.getInstance().getOptionsService();

    public PropertyNode(String name) {
        this(name, null);
    }

    public PropertyNode(String name, PropertyNode parent) {
        this(name, parent, null);
    }

    public PropertyNode(String name, PropertyNode parent, Property property) {
        this.property = property;
        this.name = name;
        this.parent = parent;

        if (property != null) {
            value = OPTIONS_SERVICE.getProperty(property);
        }
    }

    public Property getProperty() {
        return property;
    }

    public String getName() {
        return name;
    }

    public PropertyNode getParent() {
        return parent;
    }

    public int childrenCount() {
        return children.size();
    }

    public int indexOf(PropertyNode n) {
        return children.indexOf(n);
    }

    public PropertyNode getChild(int i) {
        return children.get(i);
    }

    public void addChild(PropertyNode n) {
        children.add(n);
        n.parent = this;
    }

    public boolean isEmpty() {
        return children.isEmpty();
    }

    public boolean has(PropertyNode n) {
        return children.contains(n);
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public void apply() {
        if (property != null) {
            OPTIONS_SERVICE.setProperty(property, value);
        }
    }

    public void revert() {
        if (property != null) {
            value = OPTIONS_SERVICE.getProperty(property);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PropertyNode that = (PropertyNode) o;

        return name.equals(that.name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("PropertyNode");
        sb.append("{name='").append(name).append('\'');
        sb.append('}');
        return sb.toString();
    }

    public TreePath getPath() {
        return new TreePath(makePath(0));
    }

    private PropertyNode[] makePath(int depth) {
        if (parent == null) {
            PropertyNode[] path = new PropertyNode[depth + 1];
            path[0] = this;
            return path;
        }

        PropertyNode[] path = parent.makePath(depth + 1);
        path[path.length - depth - 1] = this;
        return path;
    }

}
