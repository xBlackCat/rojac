package org.xblackcat.rojac.gui.dialog.options;

import org.xblackcat.rojac.service.options.Property;

import javax.swing.tree.TreePath;
import java.util.ArrayList;
import java.util.List;

/**
 * @author xBlackCat
 */
class PropertyNode<T> {
    private Property<T> property;
    private final String name;
    private PropertyNode<?> parent;

    private T value;

    private final List<PropertyNode<?>> children = new ArrayList<PropertyNode<?>>();

    PropertyNode(String name) {
        this(name, null);
    }

    PropertyNode(String name, PropertyNode<?> parent) {
        this(name, parent, null);
    }

    PropertyNode(String name, PropertyNode<?> parent, Property<T> property) {
        this.property = property;
        this.name = name;
        this.parent = parent;

        if (property != null) {
            value = property.get();
        }
    }

    Property<T> getProperty() {
        return property;
    }

    String getName() {
        return name;
    }

    PropertyNode<?> getParent() {
        return parent;
    }

    int childrenCount() {
        return children.size();
    }

    int indexOf(PropertyNode<?> n) {
        return children.indexOf(n);
    }

    PropertyNode<?> getChild(int i) {
        return children.get(i);
    }

    void addChild(PropertyNode<?> n) {
        children.add(n);
        n.parent = this;
    }

    boolean isEmpty() {
        return children.isEmpty();
    }

    boolean has(PropertyNode<?> n) {
        return children.contains(n);
    }

    T getValue() {
        return value;
    }

    boolean isChanged() {
        if (property == null) {
            return false;
        }

        T curValue = property.get();

        return value != null ? !value.equals(curValue) : curValue != null;
    }

    void setValue(T value) {
        this.value = value;
    }

    void apply() {
        if (property != null) {
            property.set(value);
        }
    }

    void revert() {
        if (property != null) {
            value = property.get();
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

    TreePath getPath() {
        return new TreePath(makePath(0));
    }

    private PropertyNode<?>[] makePath(int depth) {
        if (parent == null) {
            PropertyNode<?>[] path = new PropertyNode[depth + 1];
            path[0] = this;
            return path;
        }

        PropertyNode<?>[] path = parent.makePath(depth + 1);
        path[path.length - depth - 1] = this;
        return path;
    }

    /**
     * Copy a property from another node
     * @param path
     * @return
     */
    boolean setProperty(PropertyNode path) {
        if (path.property == null) {
            return false;
        }

        property = path.property;
        value = property.get();
        return true;
    }
}
