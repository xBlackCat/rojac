package org.xblackcat.rojac.gui.dialogs;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xblackcat.rojac.gui.component.AButtonAction;
import org.xblackcat.rojac.i18n.Messages;
import org.xblackcat.rojac.service.options.Property;
import org.xblackcat.rojac.util.RojacUtils;
import org.xblackcat.rojac.util.WindowsUtils;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;

/**
 * Shows a dialog to configure the application.
 *
 * @author xBlackCat
 */

public class OptionsDialog extends JDialog {
    private static final Log log = LogFactory.getLog(OptionsDialog.class);
    // Hidden properties.
    private static final Collection<Property> PROPERTIES_TO_SKIP = new HashSet<Property>(Arrays.asList(new Property[]{
            Property.ROJAC_DEBUG_MODE,
            Property.ROJAC_MAIN_FRAME_SIZE,
            Property.ROJAC_MAIN_FRAME_POSITION,
            Property.ROJAC_MAIN_FRAME_STATE,
            Property.RSDN_USER_ID,
            Property.RSDN_USER_NAME,
            Property.RSDN_USER_PASSWORD,
            Property.RSDN_USER_PASSWORD_SAVE
    }));

    protected PropertiesModel model;


    public OptionsDialog(Window mainFrame) {
        super(mainFrame, DEFAULT_MODALITY_TYPE);
        try {
            model = loadModel();
        } catch (NullPointerException e) {
            log.error("Can not initialize model", e);
            model = null;
        }

        setTitle(Messages.DIALOG_LOADMESSAGE_TITLE.get());

        initializeLayout();

        setMinimumSize(new Dimension(400, 300));
        pack();
    }

    private void initializeLayout() {
        JPanel cp = new JPanel(new BorderLayout(5, 10));
        cp.setBorder(new EmptyBorder(10, 10, 10, 10));

        cp.add(new JLabel("Options dialog"), BorderLayout.NORTH);

        JComponent centerComp;
        if (model != null) {
            JTree tree = new JTree(model);
            
            centerComp = new JScrollPane(tree);
        } else {
            centerComp = new JLabel("Can not initialize model");
        }
        cp.add(centerComp, BorderLayout.CENTER);

        cp.add(WindowsUtils.createButtonsBar(
                this,
                Messages.BUTTON_OK,
                new AButtonAction(Messages.BUTTON_CHANGEPASSWORD) {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        LoginDialog ld = new LoginDialog(OptionsDialog.this);
                        ld.showLoginDialog();
                    }
                },
                new AButtonAction(Messages.BUTTON_OK) {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        applySettings();
                        setVisible(false);
                        dispose();
                    }
                },
                new AButtonAction(Messages.BUTTON_APPLY) {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        applySettings();
                    }
                },
                new AButtonAction(Messages.BUTTON_CANCEL) {
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

    }

    private PropertiesModel loadModel() {
        PropertyNode root = null;

        for (Property<?> p : Property.getAllProperties()) {
            if (PROPERTIES_TO_SKIP.contains(p)) {
                if (log.isTraceEnabled()) {
                    log.trace(p + " is in ignore list - skipping.");
                }
                continue;
            }

            if (root == null) {
                root = RojacUtils.propertyPath(p);

                if (root == null) {
                    if (log.isTraceEnabled()) {
                        log.trace(p + " should be initializes a root but it is not.");
                    }
                }
            } else {
                if (!RojacUtils.addProperty(root, p)) {
                    if (log.isTraceEnabled()) {
                        log.trace("Can not add " + p + " to a root " + root.getName());
                    }
                }
            }
        }

        if (root == null) {
            throw new NullPointerException("Can not initialize properties model. See TRACE-level logs for detail");
        }

        return new PropertiesModel(root);
    }

}
