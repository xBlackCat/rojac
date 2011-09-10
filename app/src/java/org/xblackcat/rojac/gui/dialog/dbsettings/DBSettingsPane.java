package org.xblackcat.rojac.gui.dialog.dbsettings;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdesktop.swingx.combobox.ListComboBoxModel;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.xblackcat.rojac.RojacDebugException;
import org.xblackcat.rojac.service.options.Property;
import org.xblackcat.rojac.service.storage.StorageInitializationException;
import org.xblackcat.rojac.service.storage.database.connection.DatabaseSettings;
import org.xblackcat.rojac.util.DatabaseUtils;

import javax.swing.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

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

    public DBSettingsPane() {
        GroupLayout groupLayout = new GroupLayout(this);
        setLayout(groupLayout);
        groupLayout.setAutoCreateContainerGaps(true);
        groupLayout.setAutoCreateGaps(true);

        String currentEngine;
        try {
            currentEngine = loadEngines();
        } catch (IOException e) {
            throw new RojacDebugException("Can not load list of engines", e);
        }

        initialize(currentEngine, groupLayout);
    }

    private void initialize(String currentEngine, GroupLayout groupLayout) {
        final ArrayList<String> list = new ArrayList<>(this.engines.keySet());
        Collections.sort(list);
        engineSelector = new JComboBox<>(new ListComboBoxModel<>(list));

        fieldUrl = new JTextField();
        fieldUserName = new JTextField();
        fieldPassword = new JPasswordField();
        fieldShudownUrl = new JTextField();
        fieldDriverName = new JTextField();

        fieldDriverName.setEditable(false);

        JLabel labelEngine = new JLabel("Engine");
        JLabel labelUrl = new JLabel("URL");
        JLabel labelUserName = new JLabel("User name");
        JLabel labelPassword = new JLabel("Password");
        JLabel labelShutdownUrl = new JLabel("Shutdown URL");
        JLabel labelDriverName = new JLabel("JDBC Driver");

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
    }

    public DatabaseSettings getCurrentSettings() {
        String selectedItem = (String) engineSelector.getSelectedItem();

        return updateSettings(selectedItem);
    }

    private String loadEngines() throws IOException {
        engines = new HashMap<>();

        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();

        final Resource[] resources = resolver.getResources("dbconfig/*/database.properties");
        for (Resource resource : resources) {
            String engine = resource.getFile().getParentFile().getName();
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
}
