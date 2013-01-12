package org.xblackcat.rojac.gui.dialog.options;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xblackcat.rojac.RojacDebugException;
import org.xblackcat.rojac.i18n.Message;
import org.xblackcat.rojac.i18n.NodeText;
import org.xblackcat.rojac.service.datahandler.OptionsUpdatedPacket;
import org.xblackcat.rojac.service.options.Property;

import javax.swing.*;
import java.awt.*;

/**
 * @author xBlackCat
 */
class PropertiesPage extends APage {
    private static final Log log = LogFactory.getLog(PropertiesPage.class);

    protected PropertiesModel model = createModel();
    private JComponent propertiesTree;

    protected PropertiesPage() {
        initializeLayout();
    }

    protected void initializeLayout() {
        add(new JLabel(Message.Dialog_Options_Description_General.get()), BorderLayout.NORTH);

        if (model != null) {
            JComponent tree = setupTree();

            propertiesTree = new JScrollPane(tree);
        } else {
            propertiesTree = new JLabel("Can not initialize model");
        }
        add(propertiesTree, BorderLayout.CENTER);
    }

    @Override
    public Message getTitle() {
        return Message.Dialog_Options_Title_General;
    }

    @Override
    public void placeFocus() {
        propertiesTree.requestFocus();
    }

    @Override
    protected void applySettings(Window mainFrame) {
        OptionsUpdatedPacket packet = new OptionsUpdatedPacket(model.applySettings());

        packet.dispatch();
    }

    /**
     * Builds options dialog model from the list of properties.
     *
     * @return constructed and filled model of properties for property tree.
     * @throws RojacDebugException is thrown if tree can not be constructed.
     */
    private static PropertiesModel createModel() {
        PropertyNode root = null;

        for (Property<?> p : Property.getAllProperties()) {
            if (!p.isPublic()) {
                if (log.isTraceEnabled()) {
                    log.trace(p + " is in not public - skipping.");
                }
                continue;
            }

            if (root == null) {
                root = PropertyUtils.propertyPath(p);

                if (root == null) {
                    if (log.isTraceEnabled()) {
                        log.trace(p + " should be initializes a root but it is not.");
                    }
                }
            } else {
                if (!PropertyUtils.addProperty(root, p)) {
                    if (log.isTraceEnabled()) {
                        log.trace("Can not add " + p + " to a root " + root.getName());
                    }
                }
            }
        }

        if (root == null) {
            throw new RojacDebugException("Can not initialize properties model. See TRACE-level logs for detail");
        }

        return new PropertiesModel(root);
    }

    private JComponent setupTree() {
        JTree tree = new JTree(model);

        tree.setEditable(true);

        tree.setCellRenderer(new OptionTreeCellRenderer());
        tree.setCellEditor(new OptionCellEditor());
        // Force show tooltips in tree
        tree.setToolTipText(NodeText.Tip.get(model.getRoot()));

        return tree;
    }
}
