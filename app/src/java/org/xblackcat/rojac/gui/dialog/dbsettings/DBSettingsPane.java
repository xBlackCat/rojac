package org.xblackcat.rojac.gui.dialog.dbsettings;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdesktop.swingx.combobox.ListComboBoxModel;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.xblackcat.rojac.RojacDebugException;
import org.xblackcat.rojac.gui.component.AButtonAction;
import org.xblackcat.rojac.i18n.JLOptionPane;
import org.xblackcat.rojac.i18n.Message;
import org.xblackcat.rojac.service.options.Property;
import org.xblackcat.rojac.service.storage.StorageException;
import org.xblackcat.rojac.service.storage.StorageInitializationException;
import org.xblackcat.rojac.service.storage.database.connection.DatabaseSettings;
import org.xblackcat.rojac.service.storage.database.connection.IConnectionFactory;
import org.xblackcat.rojac.service.storage.database.connection.SimpleConnectionFactory;
import org.xblackcat.rojac.service.storage.database.convert.Converters;
import org.xblackcat.rojac.service.storage.database.helper.IQueryHelper;
import org.xblackcat.rojac.service.storage.database.helper.QueryHelper;
import org.xblackcat.rojac.util.DatabaseUtils;
import org.xblackcat.rojac.util.RojacWorker;
import org.xblackcat.rojac.util.WindowsUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.IOException;
import java.util.*;
import java.util.List;

import static org.xblackcat.rojac.i18n.Message.*;

/**
 * @author xBlackCat
 */
public class DBSettingsPane extends JPanel {
    private static final Log log = LogFactory.getLog(DBSettingsPane.class);

    private Map<String, DatabaseSettings> engines;
    private JComboBox<String> engineSelector;
    private JTextField fieldUrl;
    private JTextField fieldUserName;
    private JPasswordField fieldPassword;
    private JTextField fieldShudownUrl;
    private JTextField fieldDriverName;
    private final JButton checkButton;

    public DBSettingsPane() {
        super(new BorderLayout(5, 5));

        String currentEngine;
        try {
            currentEngine = loadEngines();
        } catch (IOException e) {
            throw new RojacDebugException("Can not load list of engines", e);
        }

        add(getFieldsList(currentEngine), BorderLayout.CENTER);
        checkButton = WindowsUtils.setupButton(new CheckDBSettings());
        add(WindowsUtils.coverComponent(checkButton, FlowLayout.RIGHT, getBackground()), BorderLayout.SOUTH);
    }

    private JComponent getFieldsList(String currentEngine) {
        JPanel fieldsPane = new JPanel();
        GroupLayout groupLayout = new GroupLayout(fieldsPane);
        fieldsPane.setLayout(groupLayout);
        groupLayout.setAutoCreateContainerGaps(true);
        groupLayout.setAutoCreateGaps(true);

        final ArrayList<String> list = new ArrayList<>(this.engines.keySet());
        Collections.sort(list);
        engineSelector = new JComboBox<>(new ListComboBoxModel<>(list));

        fieldUrl = new JTextField();
        fieldUserName = new JTextField();
        fieldPassword = new JPasswordField();
        fieldShudownUrl = new JTextField();
        fieldDriverName = new JTextField();

        fieldDriverName.setEditable(false);

        JLabel labelEngine = new JLabel(Dialog_DbSettings_Label_Engine.get());
        JLabel labelUrl = new JLabel(Dialog_DbSettings_Label_Url.get());
        JLabel labelUserName = new JLabel(Dialog_DbSettings_Label_UserName.get());
        JLabel labelPassword = new JLabel(Dialog_DbSettings_Label_Passord.get());
        JLabel labelShutdownUrl = new JLabel(Dialog_DbSettings_Label_ShutdownUrl.get());
        JLabel labelDriverName = new JLabel(Dialog_DbSettings_Label_DriverName.get());

        engineSelector.setSelectedItem(null);
        engineSelector.addItemListener(new FieldSetter());
        engineSelector.setSelectedItem(currentEngine);

        GroupLayout.SequentialGroup hGroup = groupLayout.createSequentialGroup();

        GroupLayout.SequentialGroup vGroup = groupLayout.createSequentialGroup();

        groupLayout.setVerticalGroup(vGroup);
        groupLayout.setHorizontalGroup(hGroup);

        hGroup
                .addGroup(
                        groupLayout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                                .addComponent(labelEngine)
                                .addComponent(labelUrl)
                                .addComponent(labelUserName)
                                .addComponent(labelPassword)
                                .addComponent(labelShutdownUrl)
                                .addComponent(labelDriverName)
                )
                .addGroup(
                        groupLayout.createParallelGroup(GroupLayout.Alignment.LEADING, true)
                                .addComponent(engineSelector)
                                .addComponent(fieldUrl)
                                .addComponent(fieldUserName)
                                .addComponent(fieldPassword)
                                .addComponent(fieldShudownUrl)
                                .addComponent(fieldDriverName)
                );

        vGroup
                .addGroup(
                        groupLayout.createParallelGroup(GroupLayout.Alignment.CENTER, false)
                                .addComponent(labelEngine)
                                .addComponent(engineSelector)
                )
                .addGroup(
                        groupLayout.createParallelGroup(GroupLayout.Alignment.CENTER, false)
                                .addComponent(labelUrl)
                                .addComponent(fieldUrl)
                )
                .addGroup(
                        groupLayout.createParallelGroup(GroupLayout.Alignment.CENTER, false)
                                .addComponent(labelUserName)
                                .addComponent(fieldUserName)
                )
                .addGroup(
                        groupLayout.createParallelGroup(GroupLayout.Alignment.CENTER, false)
                                .addComponent(labelPassword)
                                .addComponent(fieldPassword)
                )
                .addGroup(
                        groupLayout.createParallelGroup(GroupLayout.Alignment.CENTER, false)
                                .addComponent(labelShutdownUrl)
                                .addComponent(fieldShudownUrl)
                )
                .addGroup(
                        groupLayout.createParallelGroup(GroupLayout.Alignment.CENTER, false)
                                .addComponent(labelDriverName)
                                .addComponent(fieldDriverName)
                );

        return fieldsPane;
    }

    public DatabaseSettings getCurrentSettings() {
        String selectedItem = (String) engineSelector.getSelectedItem();

        return selectedItem == null ? null : updateSettings(selectedItem);
    }

    private String loadEngines() throws IOException {
        engines = new HashMap<>();

        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();

        final Resource[] resources = resolver.getResources("dbconfig/*/database.properties");
        for (Resource resource : resources) {
            String path = resource.getURL().getPath();
            int endIndex = path.length() - 20; // "/database.properties".length() == 20
            String engine = path.substring(path.lastIndexOf('/', endIndex - 1) + 1, endIndex);
            try {
                DatabaseSettings settings = DatabaseUtils.readDefaults(engine);

                assert engine.equals(settings.getEngine());

                engines.put(engine, settings);
            } catch (StorageInitializationException e) {
                log.warn("Found invalid configuration");
            }

        }

        DatabaseSettings settings = Property.ROJAC_DATABASE_CONNECTION_SETTINGS.get();
        if (settings != null) {
            final String engine = settings.getEngine();
            engines.put(engine, settings);
            return engine;
        }

        return null;
    }

    private DatabaseSettings updateSettings(String engine) {
        DatabaseSettings current = engines.get(engine);
        if (current == null) {
            return null;
        }

        DatabaseSettings settings = new DatabaseSettings(
                engine,
                fieldUrl.getText(),
                fieldShudownUrl.getText(),
                fieldUserName.getText(),
                String.valueOf(fieldPassword.getPassword()),
                current.getJdbcDriverClass()
        );

        engines.put(engine, settings);

        return settings;
    }

    public void requestFocusInField() {
        engineSelector.requestFocus();
    }

    private class FieldSetter implements ItemListener {
        @Override
        public void itemStateChanged(ItemEvent e) {
            boolean exists = e.getItem() != null;
            if (e.getStateChange() == ItemEvent.DESELECTED) {
                if (exists) {
                    String engine = e.getItem().toString();

                    updateSettings(engine);
                }
            } else if (e.getStateChange() == ItemEvent.SELECTED) {
                fieldUrl.setEnabled(exists);
                fieldShudownUrl.setEnabled(exists);
                fieldUserName.setEnabled(exists);
                fieldPassword.setEnabled(exists);
                fieldDriverName.setEnabled(exists);

                if (exists) {
                    DatabaseSettings settings = engines.get(e.getItem().toString());

                    fieldUrl.setText(settings.getUrl());
                    fieldShudownUrl.setText(settings.getShutdownUrl());
                    fieldUserName.setText(settings.getUserName());
                    fieldPassword.setText(settings.getPassword());
                    fieldDriverName.setText(settings.getJdbcDriverClass().getName());
                }
            }
        }

    }

    private class CheckDBSettings extends AButtonAction {
        public CheckDBSettings() {
            super(Message.Button_Check);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            final DatabaseSettings curSet = getCurrentSettings();

            if (curSet == null) {
                return;
            }

            checkButton.setEnabled(false);

            Runnable buttonEnabler = new Runnable() {
                @Override
                public void run() {
                    checkButton.setEnabled(true);
                }
            };
            new RojacWorker<Void, Exception>(buttonEnabler) {
                @Override
                protected Void perform() throws Exception {
                    try {
                        IConnectionFactory factory = new SimpleConnectionFactory(curSet);
                        IQueryHelper queryHelper = new QueryHelper(factory);

                        Number answer = queryHelper.executeSingle(Converters.TO_NUMBER, "SELECT 21+21");

                        if (answer != null && answer.intValue() == 42) {
                            publish((Exception) null);
                        } else {
                            publish(new IllegalStateException("Database math is wrong!"));
                        }

                        queryHelper.shutdown();

                    } catch (StorageException e1) {
                        publish(e1);
                    }


                    return null;
                }

                @Override
                protected void process(List<Exception> chunks) {
                    for (Exception check : chunks) {
                        if (check == null) {
                            JLOptionPane.showMessageDialog(
                                    DBSettingsPane.this,
                                    Message.Dialog_DbCheck_Success.get(),
                                    Message.Dialog_DbCheck_Title.get(),
                                    JOptionPane.INFORMATION_MESSAGE
                            );
                        } else {
                            JLOptionPane.showMessageDialog(
                                    DBSettingsPane.this,
                                    Message.Dialog_DbCheck_Fail.get(ExceptionUtils.getStackTrace(check)),
                                    Message.Dialog_DbCheck_Title.get(),
                                    JOptionPane.ERROR_MESSAGE
                            );
                        }
                    }
                }
            }.execute();
        }
    }
}
