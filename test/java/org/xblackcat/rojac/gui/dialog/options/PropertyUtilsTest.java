package org.xblackcat.rojac.gui.dialog.options;

import junit.framework.TestCase;
import org.xblackcat.rojac.service.options.Property;

/**
 * @author xBlackCat
 */

@SuppressWarnings({"unchecked"})
public class PropertyUtilsTest extends TestCase {
    public void testMakePropertyPath() {
        // rojac.viewer.show.marks.pane (5 nodes)
        Property p = Property.MESSAGE_PANE_SHOW_MARKS;

        PropertyNode node = PropertyUtils.propertyPath(p);

        assertEquals("rojac", node.getName());
        assertNull(node.getProperty());
        assertNull(node.getParent());

        node = next(node);
        assertEquals("viewer", node.getName());
        assertNull(node.getProperty());
        assertNotNull(node.getParent());

        node = next(node);
        assertEquals("show_marks_pane", node.getName());
        assertNotNull(node.getProperty());
        assertNotNull(node.getParent());

        node = next(node);
        assertNull(node);
    }

    public void testMergePropertyPathes() {
        // rojac.viewer.show.marks.pane (5 nodes)
        Property p = Property.MESSAGE_PANE_SHOW_MARKS;

        // Root node
        final PropertyNode rootNode = new PropertyNode("rojac");
        final PropertyNode childLevel1 = new PropertyNode("viewer", rootNode);
        final PropertyNode childLevel2 = new PropertyNode("testing", childLevel1);

        rootNode.addChild(childLevel1);
        childLevel1.addChild(childLevel2);

        // Checks before test.
        assertEquals(1, rootNode.childrenCount());
        assertEquals(1, childLevel1.childrenCount());
        assertEquals(0, childLevel2.childrenCount());

        // Process merge
        assertTrue(PropertyUtils.addProperty(rootNode, p));

        assertEquals(1, rootNode.childrenCount());
        assertEquals(2, childLevel1.childrenCount());
        assertEquals(0, childLevel2.childrenCount());

        // Child should not be changed
        assertTrue(rootNode.getChild(0) == childLevel1);
        assertTrue(childLevel1.has(new PropertyNode("show_marks_pane")));
    }

    private static PropertyNode next(PropertyNode n) {
        return (n == null || n.isEmpty()) ? null : n.getChild(0);
    }
}
