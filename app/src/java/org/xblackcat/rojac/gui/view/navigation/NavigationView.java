package org.xblackcat.rojac.gui.view.navigation;

import org.jdesktop.swingx.JXTreeTable;
import org.jdesktop.swingx.table.TableColumnExt;
import org.xblackcat.rojac.gui.IAppControl;
import org.xblackcat.rojac.gui.IViewLayout;
import org.xblackcat.rojac.gui.PopupMouseAdapter;
import org.xblackcat.rojac.gui.theme.ViewIcon;
import org.xblackcat.rojac.gui.view.AView;
import org.xblackcat.rojac.i18n.Message;
import org.xblackcat.rojac.service.datahandler.IPacket;
import org.xblackcat.rojac.service.options.Property;

import javax.swing.*;
import javax.swing.table.TableColumnModel;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.awt.event.MouseEvent;

/**
 * @author xBlackCat Date: 15.07.11
 */
public class NavigationView extends AView {
    private NavigationModel model;
    private JXTreeTable viewTable;

    public NavigationView(IAppControl appControl) {
        super(appControl);

        model = new NavigationModel();
        viewTable = new JXTreeTable();
        viewTable.setAutoCreateColumnsFromModel(false);
        viewTable.setTreeTableModel(model);
        viewTable.setTableHeader(null);
        viewTable.setColumnMargin(0);

        TableColumnModel columnModel = viewTable.getColumnModel();
        columnModel.addColumn(new TableColumnExt(0, 100));
        TableColumnExt infoColumn = new TableColumnExt(1, 80, new InfoCellRenderer(), null);
        infoColumn.setMaxWidth(140);
        infoColumn.setMinWidth(30);
        columnModel.addColumn(infoColumn);

        viewTable.setTreeCellRenderer(new LabelCellRenderer());

        viewTable.addMouseListener(new PopupMouseAdapter() {
            private AnItem getItem(Point point) {
                TreePath path = viewTable.getPathForLocation(point.x, point.y);

                return path == null ? null : (AnItem) path.getLastPathComponent();
            }

            @Override
            protected void triggerClick(MouseEvent e) {
                if (Property.VIEW_NAVIGATION_OPEN_ONE_CLICK.get()) {
                    AnItem item = getItem(e.getPoint());
                    if (item != null) {
                        item.onDoubleClick(NavigationView.this.appControl);
                    }
                }
            }

            @Override
            protected void triggerDoubleClick(MouseEvent e) {
                if (!Property.VIEW_NAVIGATION_OPEN_ONE_CLICK.get()) {
                    AnItem item = getItem(e.getPoint());
                    if (item != null) {
                        item.onDoubleClick(NavigationView.this.appControl);
                    }
                }
            }

            @Override
            protected void triggerPopup(MouseEvent e) {
                AnItem item = getItem(e.getPoint());

                if (item == null) {
                    return;
                }

                JPopupMenu menu = item.getContextMenu(NavigationView.this.appControl);
                if (menu != null) {
                    final Point p = e.getPoint();
                    menu.show(e.getComponent(), p.x, p.y);
                }
            }
        });


        JScrollPane container = new JScrollPane(viewTable);

        add(container, BorderLayout.CENTER);
    }

    @Override
    public String getTabTitle() {
        return Message.View_Navigation_Title.get();
    }

    @Override
    public IViewLayout storeLayout() {
        AnItem root = model.getRoot();

        int i = 0;
        int size = root.getChildCount();

        boolean[] expanded = new boolean[size];

        while (i < size) {
            AnItem item = root.getChild(i);

            expanded[i] = viewTable.isExpanded(model.getPathToRoot(item));

            i++;
        }

        return new NavigationLayout(expanded);
    }

    @Override
    public void setupLayout(IViewLayout o) {
        if (o instanceof NavigationLayout) {
            NavigationLayout l = (NavigationLayout) o;

            AnItem root = model.getRoot();

            int i = 0;
            int size = root.getChildCount();

            boolean[] expanded = l.getExpandedStatus();

            if (expanded.length < size) {
                // Prevent IndexOutOfBoundsException exception
                size = expanded.length;
            }

            while (i < size) {
                AnItem item = root.getChild(i);

                if (expanded[i]) {
                    viewTable.expandPath(model.getPathToRoot(item));
                }

                i++;
            }
        }
    }

    @Override
    public Icon getTabTitleIcon() {
        return ViewIcon.Navigation;
    }

    @Override
    public JPopupMenu getTabTitleMenu() {
        return null;
    }

    @Override
    public void processPacket(IPacket packet) {
        model.dispatch(packet);
    }

}
