package org.xblackcat.rojac.gui.frame.mtree;
/**
 *
 * @author xBlackCat
 */


import junit.framework.TestCase;

public class TreeTableModelTest extends TestCase {
    private TreeTableModel treeTableModel;

    protected void setUp() {
        treeTableModel = new TreeTableModel();

        treeTableModel.setRoots(setupTestRoots(treeTableModel));
    }

    static MessageItem[] setupTestRoots(TreeTableModel treeTableModel) {
        MessageItem root1 = new MessageItem(treeTableModel, 1);
        root1.setStatus(ItemStatus.NOT_EXPLORED);

        MessageItem root2 = new MessageItem(treeTableModel, 2);
        root2.setStatus(ItemStatus.EXPLORED);
        root2.setChildren(new MessageItem[]{
                new MessageItem(root2, 4),
                new MessageItem(root2, 5),
                new MessageItem(root2, 6)
        });

        MessageItem root3 = new MessageItem(treeTableModel, 3);
        root3.setStatus(ItemStatus.EXPLORED);

        MessageItem[] roots = new MessageItem[]{root1, root2, root3};
        return roots;
    }

    public void testGetItemAtRow() {
        assertEquals(1, treeTableModel.getElementAt(0).getMessageId());
        assertEquals(2, treeTableModel.getElementAt(1).getMessageId());
        assertEquals(4, treeTableModel.getElementAt(2).getMessageId());
        assertEquals(5, treeTableModel.getElementAt(3).getMessageId());
        assertEquals(6, treeTableModel.getElementAt(4).getMessageId());
        assertEquals(3, treeTableModel.getElementAt(5).getMessageId());

        assertNull(treeTableModel.getElementAt(-1));
        assertNull(treeTableModel.getElementAt(6));
    }

    public void testGetRowCount() throws Exception {
        assertEquals(6, treeTableModel.getRowCount());
    }
}