package org.xblackcat.rojac.gui.dialogs;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xblackcat.rojac.gui.MainFrame;
import org.xblackcat.rojac.i18n.Messages;
import org.xblackcat.rojac.service.options.Property;
import org.xblackcat.rojac.util.RojacUtils;
import org.xblackcat.rojac.util.WindowsUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
    }));

    private final MainFrame mainFrame;
    protected PropertiesModel model;


    public OptionsDialog(MainFrame mainFrame) {
        super(mainFrame, true);
        this.mainFrame = mainFrame;
        try {
            model = loadModel();
        } catch (NullPointerException e) {
            log.error("Can not initialize model", e);
            model = null;
        }

        setTitle(Messages.DIALOG_LOADMESSAGE_TITLE.get());

        initializeLayout();

        pack();
        setSize(400, 300);
    }

    private void initializeLayout() {
        JPanel cp = new JPanel(new BorderLayout());

        cp.add(new JLabel("Options dialog"), BorderLayout.NORTH);

        JComponent centerComp;
        if (model != null) {
            JTree tree = new JTree(model);
            
            centerComp = new JScrollPane(tree);
        } else {
            centerComp = new JLabel("Can not initialize model");
        }
        cp.add(centerComp, BorderLayout.CENTER);

        JPanel buttons = new JPanel(new GridLayout(1, 0, 10, 5));

        buttons.add(WindowsUtils.setupButton(Messages.BUTTON_CANCEL, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                applySettings();
            }
        }, Messages.BUTTON_APPLY));
        JButton okButton = WindowsUtils.setupButton(Messages.BUTTON_OK, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                applySettings();
                setVisible(false);
            }
        }, Messages.BUTTON_OK);
        buttons.add(okButton);
        buttons.add(WindowsUtils.setupButton(Messages.BUTTON_CANCEL, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
            }
        }, Messages.BUTTON_CANCEL));
        getRootPane().setDefaultButton(okButton);

        cp.add(WindowsUtils.coverComponent(buttons, FlowLayout.CENTER), BorderLayout.SOUTH);

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
