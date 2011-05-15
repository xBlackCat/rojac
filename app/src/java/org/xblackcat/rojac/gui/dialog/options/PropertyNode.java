package org.xblackcat.rojac.gui.dialog.options;

import org.xblackcat.rojac.service.options.Property;

import javax.swing.tree.TreePath;
import java.util.ArrayList;
import java.util.List;

/**
 * @author xBlackCat
 */
class PropertyNode<T> {
    private final Property<T> property;
    private final String name;
    private PropertyNode<?> parent;

    private T value;

    private final List<PropertyNode<?>> children = new ArrayList<PropertyNode<?>>();

    public PropertyNode(String name) {
        this(name, null);
    }

    public PropertyNode(String name, PropertyNode<?> parent) {
        this(name, parent, null);
    }

    public PropertyNode(String name, PropertyNode<?> parent, Property<T> property) {
        this.property = property;
        this.name = name;
        this.parent = parent;

        if (property != null) {
            value = property.get();
        }
    }

    public Property<T> getProperty() {
        return property;
    }

    public String getName() {
        return name;
    }

    public PropertyNode<?> getParent() {
        return parent;
    }

    public int childrenCount() {
        return children.size();
    }

    public int indexOf(PropertyNode<?> n) {
        return children.indexOf(n);
    }

    public PropertyNode<?> getChild(int i) {
        return children.get(i);
    }

    public void addChild(PropertyNode<?> n) {
        children.add(n);
        n.parent = this;
    }

    public boolean isEmpty() {
        return children.isEmpty();
    }

    public boolean has(PropertyNode<?> n) {
        return children.contains(n);
    }

    public T getValue() {
        return value;
    }

    public boolean isChanged() {
        if (property == null) {
            return false;
        }

        T curValue = property.get();

        return value != null ? !value.equals(curValue) : curValue != null;
    }

    public void setValue(T value) {
        this.value = value;
    }

    public void apply() {
        if (property != null) {
            property.set(value);
        }
    }

    public void revert() {
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

    public TreePath getPath() {
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

}
