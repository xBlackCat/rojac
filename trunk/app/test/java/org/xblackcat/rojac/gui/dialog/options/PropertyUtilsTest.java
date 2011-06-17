package org.xblackcat.rojac.gui.dialog.options;

import junit.framework.TestCase;
import org.xblackcat.rojac.service.options.Property;

/**
 * @author xBlackCat
 */

@SuppressWarnings({"unchecked"})
public class PropertyUtilsTest extends TestCase {
    public void testMakePropertyPath() {
        // rojac.main_frame.tray.hide_on_minimize (5 nodes)
        Property p = Property.ROJAC_MAIN_FRAME_HIDE_ON_MINIMIZE;

        PropertyNode node = PropertyUtils.propertyPath(p);

        assertEquals("rojac", node.getName());
        assertNull(node.getProperty());
        assertNull(node.getParent());

        node = next(node);
        assertEquals("main_frame", node.getName());
        assertNull(node.getProperty());
        assertNotNull(node.getParent());

        node = next(node);
        assertEquals("tray", node.getName());
        assertNull(node.getProperty());
        assertNotNull(node.getParent());


        node = next(node);
        assertEquals("hide_on_minimize", node.getName());
        assertNotNull(node.getProperty());
        assertNotNull(node.getParent());

        node = next(node);
        assertNull(node);
    }

    public void testMergePropertyPathes() {
        // rojac.main_frame.tray.hide_on_minimize (5 nodes)
        Property p = Property.ROJAC_MAIN_FRAME_HIDE_ON_MINIMIZE;

        // Root node
        final PropertyNode rootNode = new PropertyNode("rojac");
        final PropertyNode childLevel1 = new PropertyNode("main_frame", rootNode);
        final PropertyNode childLevel2 = new PropertyNode("tray", childLevel1);

        rootNode.addChild(childLevel1);
        childLevel1.addChild(childLevel2);

        // Checks before test.
        assertEquals(1, rootNode.childrenCount());
        assertEquals(1, childLevel1.childrenCount());
        assertEquals(0, childLevel2.childrenCount());

        // Process merge
        assertTrue(PropertyUtils.addProperty(rootNode, p));

        assertEquals(1, rootNode.childrenCount());
        assertEquals(1, childLevel1.childrenCount());
        assertEquals(1, childLevel2.childrenCount());

        // Child should not be changed
        assertTrue(rootNode.getChild(0) == childLevel1);
        assertTrue(childLevel2.has(new PropertyNode("hide_on_minimize")));
    }

    private static PropertyNode next(PropertyNode n) {
        return (n == null || n.isEmpty()) ? null : n.getChild(0);
    }
}
