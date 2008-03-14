package org.xblackcat.sunaj.gui.frame.mtree;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Date: 14 груд 2007
 *
 * @author xBlackCat
 */

public class MessagesFrame extends JPanel {
    private TreeTableModel model = new TreeTableModel();

    public MessagesFrame() {
        super(new BorderLayout());

        add(new JScrollPane(setupTable()), BorderLayout.CENTER);
    }

    private Component setupTable() {
        final JTable table = new JTable();
        table.setAutoCreateColumnsFromModel(false);

        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        table.setRowSelectionAllowed(true);
        table.setColumnSelectionAllowed(false);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setAutoscrolls(true);

        TableColumnModel tcm = table.getColumnModel();
        tcm.addColumn(setupColumn(Columns.Id, 50, new IdCellRenderer()));
        tcm.addColumn(setupColumn(Columns.Topic, 300, new TopicCellRenderer()));
        tcm.addColumn(setupColumn(Columns.Date, 70, new DateCellRenderer()));
        tcm.addColumn(setupColumn(Columns.Ratings, 70, new RatingCellRenderer(null)));
        tcm.addColumn(setupColumn(Columns.Author, 80, new AuthorCellRenderer()));
        tcm.addColumn(setupColumn(Columns.Favorite, 20, new FavoriteCellRenderer()));

        table.setAutoCreateRowSorter(true);

        table.setModel(model);

        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                Point p = e.getPoint();
                int col = table.columnAtPoint(p);
                if (table.getColumnModel().getColumn(col).getIdentifier() == Columns.Topic) {
                    int row = table.rowAtPoint(p);

                    Rectangle bounds = table.getCellRect(row, col, false);

                    MessageItem i = model.getElementAt(row);

                    int shift = Utils.getTopicShift(i);

                    boolean clicked = p.x >= bounds.x + shift && p.x <= bounds.x + shift + 16;

                    if (clicked) {
                        model.setExpanded(i, !i.isExpanded());
                    }
                }
            }
        });

        return table;
    }

    private static TableColumn setupColumn(Columns id, int width, TableCellRenderer renderer) {
        TableColumn tc = new TableColumn(id.getIndex(), width, renderer, null);
        tc.setHeaderValue(id.getTitile());
        tc.setIdentifier(id);
        return tc;
    }

    public static void main(String[] args) {
        JFrame f = new JFrame();

        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        f.setContentPane(new JPanel(new BorderLayout()));
        MessagesFrame mf = new MessagesFrame();
        f.getContentPane().add(mf);

        mf.model.setRoots(setupTestRoots(mf.model));

        f.setSize(400, 300);
        f.setVisible(true);
    }

    private static MessageItem[] setupTestRoots(TreeTableModel treeTableModel) {
        MessageItem root1 = new MessageItem(treeTableModel, 1);
        root1.setStatus(ItemStatus.EXPLORED);

        MessageItem root2 = new MessageItem(treeTableModel, 2);
        root2.setStatus(ItemStatus.EXPLORED);
        root2.setChildren(new MessageItem[]{
                new MessageItem(root2, 4),
                new MessageItem(root2, 5),
                new MessageItem(root2, 6)
        });

        MessageItem root3 = new MessageItem(treeTableModel, 3);
        root3.setStatus(ItemStatus.EXPLORED);

        return new MessageItem[]{root1, root2, root3};
    }

}
