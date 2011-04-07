package org.xblackcat.rojac.gui.dialog.options;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xblackcat.rojac.RojacException;
import org.xblackcat.rojac.gui.component.AButtonAction;
import org.xblackcat.rojac.gui.dialog.LoginDialog;
import org.xblackcat.rojac.i18n.Messages;
import org.xblackcat.rojac.service.options.Property;
import org.xblackcat.rojac.util.PropertyUtils;
import org.xblackcat.rojac.util.SynchronizationUtils;
import org.xblackcat.rojac.util.UIUtils;
import org.xblackcat.rojac.util.WindowsUtils;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Locale;

import static org.xblackcat.rojac.service.options.Property.*;

/**
 * Shows a dialog to configure the application.
 *
 * @author xBlackCat
 */

public class OptionsDialog extends JDialog {
    private static final Log log = LogFactory.getLog(OptionsDialog.class);
    protected PropertiesModel model;

    public OptionsDialog(Window mainFrame) throws RojacException {
        super(mainFrame, DEFAULT_MODALITY_TYPE);
        model = createModel();

        setTitle(Messages.Dialog_Options_Title.get());

        initializeLayout();

        setMinimumSize(new Dimension(400, 300));
        pack();
    }

    private void initializeLayout() {
        JPanel cp = new JPanel(new BorderLayout(5, 10));
        cp.setBorder(new EmptyBorder(10, 10, 10, 10));

        cp.add(new JLabel(Messages.Dialog_Options_Description.get()), BorderLayout.NORTH);

        JComponent centerComp;
        if (model != null) {
            JComponent tree = setupTree();

            centerComp = new JScrollPane(tree);
        } else {
            centerComp = new JLabel("Can not initialize model");
        }
        cp.add(centerComp, BorderLayout.CENTER);

        cp.add(WindowsUtils.createButtonsBar(
                this,
                Messages.Button_Ok,
                new AButtonAction(Messages.Button_ChangePassword) {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        LoginDialog ld = new LoginDialog(OptionsDialog.this);
                        ld.showLoginDialog();
                    }
                },
                new AButtonAction(Messages.Button_Ok) {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        applySettings();
                        setVisible(false);
                        dispose();
                    }
                },
                new AButtonAction(Messages.Button_Apply) {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        applySettings();
                    }
                },
                new AButtonAction(Messages.Button_Cancel) {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        setVisible(false);
                        dispose();
                    }
                }

        ), BorderLayout.SOUTH);

        setContentPane(cp);

    }

    private void applySettings() {
        LookAndFeel oldLAF = ROJAC_GUI_LOOK_AND_FEEL.get();
        Locale oldLocale = ROJAC_GUI_LOCALE.get();
        int schedulePeriod = SYNCHRONIZER_SCHEDULE_PERIOD.getDefault();

        model.applySettings();

        // Load changed properties.
        LookAndFeel laf = ROJAC_GUI_LOOK_AND_FEEL.get();
        if (!laf.getName().equals(oldLAF.getName())) {
            try {
                UIUtils.setLookAndFeel(laf);
            } catch (UnsupportedLookAndFeelException e) {
                log.warn("Can not initialize " + laf.getName() + " L&F.", e);
            }
        }

        Locale locale = ROJAC_GUI_LOCALE.get();
        if (!locale.equals(oldLocale)) {
            Messages.setLocale(locale);
        }

        if (schedulePeriod != SYNCHRONIZER_SCHEDULE_PERIOD.get()) {
            SynchronizationUtils.setScheduleSynchronizer(this.getOwner());
        }
    }

    /**
     * Builds options dialog model from the list of properties.
     *
     * @return constructed and filled model of properties for property tree.
     *
     * @throws RojacException is thrown if tree can not be constructed.
     */
    private PropertiesModel createModel() throws RojacException {
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
            throw new RojacException("Can not initialize properties model. See TRACE-level logs for detail");
        }

        return new PropertiesModel(root);
    }

    private JComponent setupTree() {
        JTree tree = new JTree(model);

        tree.setEditable(true);

        tree.setCellRenderer(new OptionTreeCellRenderer());
        tree.setCellEditor(new OptionCellEditor());

        return tree;
    }
}
