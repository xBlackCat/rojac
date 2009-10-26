package org.xblackcat.rojac.util;

import junit.framework.TestCase;
import org.xblackcat.rojac.data.Mark;
import org.xblackcat.rojac.data.NewRating;
import org.xblackcat.rojac.gui.dialogs.PropertyNode;
import org.xblackcat.rojac.service.options.Property;
import ru.rsdn.Janus.PostRatingInfo;

/**
 * @author xBlackCat
 */

public class RojacUtilsTest extends TestCase {
    public void testArrayConvertor() {
        NewRating[] r = new NewRating[]{
                new NewRating(0, 0, Mark.Agree.getValue()),
                new NewRating(1, 1, Mark.Agree.getValue()),
                new NewRating(2, 2, Mark.Agree.getValue()),
        };

        PostRatingInfo[] oo = RojacUtils.getRSDNObject(r);

        assertEquals(oo.length, r.length);
        for (PostRatingInfo o : oo) {
            assertEquals(o.getClass(), PostRatingInfo.class);
        }
    }

    public void testMakePropertyPath() {
        // rojac.viewer.show.marks.pane (5 nodes)
        Property p = Property.MESSAGE_PANE_SHOW_MARKS;

        PropertyNode node = RojacUtils.propertyPath(p);

        assertEquals("rojac", node.getName());
        assertNull(node.getProperty());
        assertNull(node.getParent());

        node = next(node);
        assertEquals("viewer", node.getName());
        assertNull(node.getProperty());
        assertNotNull(node.getParent());

        node = next(node);
        assertEquals("show", node.getName());
        assertNull(node.getProperty());
        assertNotNull(node.getParent());

        node = next(node);
        assertEquals("marks", node.getName());
        assertNull(node.getProperty());
        assertNotNull(node.getParent());

        node = next(node);
        assertEquals("pane", node.getName());
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
        assertTrue(RojacUtils.addProperty(rootNode, p));

        assertEquals(1, rootNode.childrenCount());
        assertEquals(2, childLevel1.childrenCount());
        assertEquals(0, childLevel2.childrenCount());

        // Child should not be changed
        assertTrue(rootNode.getChild(0) == childLevel1);
        assertTrue(childLevel1.has(new PropertyNode("show")));
    }

    private static PropertyNode next(PropertyNode n) {
        return (n == null || n.isEmpty()) ? null : n.getChild(0);
    }
}
