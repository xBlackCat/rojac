package org.xblackcat.rojac.gui.dialogs;

import org.xblackcat.rojac.service.ServiceFactory;
import org.xblackcat.rojac.service.options.Property;

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
            value = ServiceFactory.getInstance().getOptionsService().getProperty(property);
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
}
