package org.xblackcat.rojac.gui.view.message;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;

/**
 * @author xBlackCat
 */

public class EditMessagePane extends JPanel {
    private final PreviewMessagePane preview;
    protected final JTextField fieldSubject = new JTextField();
    protected final JTextPane fieldBody = new JTextPane();

    public EditMessagePane(PreviewMessagePane preview) {
        super(new BorderLayout());
        this.preview = preview;

        initializeLayout();
    }

    private void initializeLayout() {
        add(getButtonsPane(), BorderLayout.NORTH);
        add(getBodyPane(), BorderLayout.CENTER);
    }

    private Component getButtonsPane() {
        return new JPanel();
    }

    private Component getBodyPane() {
        JPanel p = new JPanel(new BorderLayout());

        p.add(fieldSubject, BorderLayout.NORTH);
        p.add(new JScrollPane(fieldBody), BorderLayout.CENTER);

        AutoPreviewListener listener = new AutoPreviewListener();
        fieldSubject.getDocument().addDocumentListener(listener);
        fieldBody.getDocument().addDocumentListener(listener);

        return p;
    }

    public void forcePreview() {
        preview.showPreview(fieldSubject.getText(), fieldBody.getText());
    }

    public void setMessage(String message, String subject) {
        fieldBody.setText(message);
        fieldSubject.setText(subject);
    }

    public String getSubject() {
        return fieldSubject.getText();
    }

    public String getBody() {
        return fieldBody.getText();
    }

    private class AutoPreviewListener implements DocumentListener {
        @Override
        public void insertUpdate(DocumentEvent e) {
            preview();
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
            preview();
        }

        @Override
        public void changedUpdate(DocumentEvent e) {
            preview();
        }

        private void preview() {
            EventQueue.invokeLater(() -> forcePreview());
        }
    }
}
