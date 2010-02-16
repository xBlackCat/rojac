package org.xblackcat.rojac.gui.dialogs;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xblackcat.rojac.data.MessageData;
import org.xblackcat.rojac.data.NewMessage;
import org.xblackcat.rojac.gui.component.AButtonAction;
import org.xblackcat.rojac.gui.view.message.EditMessagePane;
import org.xblackcat.rojac.gui.view.message.PreviewMessageView;
import org.xblackcat.rojac.i18n.JLOptionPane;
import org.xblackcat.rojac.i18n.Messages;
import org.xblackcat.rojac.service.ServiceFactory;
import org.xblackcat.rojac.service.executor.IExecutor;
import org.xblackcat.rojac.service.storage.INewMessageAH;
import org.xblackcat.rojac.service.storage.IStorage;
import org.xblackcat.rojac.service.storage.StorageException;
import org.xblackcat.rojac.util.MessageUtils;
import org.xblackcat.rojac.util.RojacWorker;
import org.xblackcat.rojac.util.WindowsUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * @author xBlackCat
 */

public class EditMessageDialog extends JDialog {
    private static final Log log = LogFactory.getLog(EditMessageDialog.class);

    protected final IStorage storage;
    private final PreviewMessageView panelPreview;
    private final EditMessagePane panelEdit;

    // new message credentials
    private int parentMessageId = 0;
    private int newMessageId = 0;
    private int forumId = 0;

    public EditMessageDialog(Window owner) {
        super(owner, DEFAULT_MODALITY_TYPE);

        panelPreview = new PreviewMessageView();
        panelEdit = new EditMessagePane(panelPreview);
        storage = ServiceFactory.getInstance().getStorage();

        initializeLayout();

        setSize(640, 480);

        panelEdit.forcePreview();
    }

    public void answerOn(final int messageId) {
        if (messageId == 0) {
            return;
        }

        IExecutor executor = ServiceFactory.getInstance().getExecutor();
        executor.execute(new MessageLoader(messageId));
    }

    public void createTopic(int forumId) {
        this.forumId = forumId;

        WindowsUtils.centerOnScreen(this);
        setVisible(true);
    }

    public void editMessage(int newMessageId) {
        NewMessage mes;
        try {
            mes = storage.getNewMessageAH().getNewMessageById(newMessageId);
        } catch (StorageException e) {
            log.error("Can not load a new message.", e);
            return;
        }

        forumId = mes.getForumId();
        parentMessageId = mes.getParentId();
        this.newMessageId = newMessageId;

        panelEdit.setMessage(mes.getMessage(), mes.getSubject());

        WindowsUtils.centerOnScreen(this);
        setVisible(true);
    }

    private void saveMessage() {

        String body = panelEdit.getBody();

        body = MessageUtils.addOwnTagLine(body);

        final NewMessage nm = new NewMessage(
                newMessageId,
                parentMessageId,
                forumId,
                panelEdit.getSubject(),
                body
        );

        RojacWorker<Void, Void> sw = new RojacWorker<Void, Void>() {
            @Override
            protected Void perform() throws Exception {
                try {
                    INewMessageAH nmAH = storage.getNewMessageAH();
                    if (newMessageId == 0) {
                        // Create a new own message
                        nmAH.storeNewMessage(nm);
                    } else {
                        nmAH.updateNewMessage(nm);
                    }
                } catch (StorageException e) {
                    log.error("Can not store new message object: " + nm, e);
                    throw e;
                }

                return null;
            }

            @Override
            protected void done() {
                try {
                    get();
                    dispose();
                } catch (InterruptedException e) {
                    JLOptionPane.showMessageDialog(EditMessageDialog.this, "Can not save changes");
                } catch (ExecutionException e) {
                    JLOptionPane.showMessageDialog(EditMessageDialog.this, "Can not save changes");                    
                }
            }
        };

        ServiceFactory.getInstance().getExecutor().execute(sw);
    }

    private void initializeLayout() {
        JPanel cp = new JPanel(new BorderLayout(5, 10));

        JSplitPane center = new JSplitPane(JSplitPane.VERTICAL_SPLIT, true, panelEdit, panelPreview);
        center.setOneTouchExpandable(true);
        center.setResizeWeight(0.5);
        center.setDividerLocation(1.);

        cp.add(center, BorderLayout.CENTER);

        cp.add(WindowsUtils.createButtonsBar(
                this,
                Messages.BUTTON_SAVE,
                new AButtonAction(Messages.BUTTON_SAVE) {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        saveMessage();
                    }
                },
                new AButtonAction(Messages.BUTTON_PREVIEW) {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        panelEdit.forcePreview();
                    }
                },
                new AButtonAction(Messages.BUTTON_CANCEL) {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        dispose();
                    }
                }
        ), BorderLayout.SOUTH);

        setContentPane(cp);
    }

    private class MessageLoader extends RojacWorker<Void, Message> {
        private final int messageId;

        public MessageLoader(int messageId) {
            this.messageId = messageId;
        }

        @Override
        protected Void perform() throws Exception {
            try {
                MessageData messageData = storage.getMessageAH().getMessageData(messageId);
                String messageBody = storage.getMessageAH().getMessageBodyById(messageId);
                publish(new Message(messageBody, messageData));
            } catch (StorageException e) {
                throw new RuntimeException("Can't load message #" + messageId, e);
            }

            return null;
        }

        @Override
        protected void process(List<Message> chunks) {
            for (Message message : chunks) {
                forumId = message.getMessageData().getForumId();
                parentMessageId = messageId;

                String mes = MessageUtils.correctBody(message.getBody(), message.getMessageData().getUserName());
                String subj = MessageUtils.correctSubject(message.getMessageData().getSubject());

                panelEdit.setMessage(mes, subj);

                WindowsUtils.centerOnScreen(EditMessageDialog.this);
                setVisible(true);

            }
        }

        @Override
        protected void done() {
            if (this.isCancelled()) {
                JLOptionPane.showMessageDialog(
                        EditMessageDialog.this,
                        Messages.ERROR_DIALOG_MESSAGE_NOT_FOUND_MESSAGE.get(messageId),
                        Messages.ERROR_DIALOG_MESSAGE_NOT_FOUND_TITLE.get(messageId),
                        JOptionPane.WARNING_MESSAGE
                );
            }
        }
    }

    private static final class Message {
        private final MessageData messageData;
        private final String body;

        private Message(String body, MessageData messageData) {
            this.body = body;
            this.messageData = messageData;
        }

        public String getBody() {
            return body;
        }

        public MessageData getMessageData() {
            return messageData;
        }
    }
}
