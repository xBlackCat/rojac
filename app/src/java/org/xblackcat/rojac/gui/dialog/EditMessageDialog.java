package org.xblackcat.rojac.gui.dialog;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xblackcat.rojac.RojacException;
import org.xblackcat.rojac.data.MessageData;
import org.xblackcat.rojac.data.NewMessage;
import org.xblackcat.rojac.data.NewMessageData;
import org.xblackcat.rojac.gui.component.AButtonAction;
import org.xblackcat.rojac.gui.view.message.EditMessagePane;
import org.xblackcat.rojac.gui.view.message.PreviewMessageView;
import org.xblackcat.rojac.i18n.JLOptionPane;
import org.xblackcat.rojac.service.datahandler.NewMessagesUpdatedPacket;
import org.xblackcat.rojac.service.storage.IMessageAH;
import org.xblackcat.rojac.service.storage.INewMessageAH;
import org.xblackcat.rojac.service.storage.Storage;
import org.xblackcat.rojac.service.storage.StorageException;
import org.xblackcat.rojac.util.MessageUtils;
import org.xblackcat.rojac.util.RojacWorker;
import org.xblackcat.rojac.util.WindowsUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static org.xblackcat.rojac.i18n.Message.*;

/**
 * @author xBlackCat
 */

public class EditMessageDialog extends JDialog {
    private static final Log log = LogFactory.getLog(EditMessageDialog.class);

    private final PreviewMessageView panelPreview;
    private final EditMessagePane panelEdit;

    // new message credentials
    private int parentMessageId = 0;
    private int newMessageId = 0;
    private int forumId = 0;

    public EditMessageDialog(Window owner) {
        super(owner, ModalityType.MODELESS);

        panelPreview = new PreviewMessageView();
        panelEdit = new EditMessagePane(panelPreview);

        initializeLayout();

        setSize(640, 480);

        panelEdit.forcePreview();
    }

    public void answerOn(int messageId) {
        if (messageId == 0) {
            return;
        }

        new ReplyLoader(messageId).execute();
    }

    public void createTopic(int forumId) {
        this.forumId = forumId;

        WindowsUtils.center(this, getOwner());
        setVisible(true);
    }

    public void editMessage(int newMessageId) {
        if (newMessageId == 0) {
            return;
        }

        new NewMessageLoader(newMessageId).execute();
    }

    private void saveMessage() {

        String body = panelEdit.getBody();

        body = MessageUtils.addOwnTagLine(body);

        final String subject = panelEdit.getSubject();

        if (StringUtils.isBlank(subject)) {
            JLOptionPane.showMessageDialog(
                    EditMessageDialog.this,
                    ErrorDialog_MessageEmptySubject_Message.get(),
                    ErrorDialog_MessageEmptySubject_Title.get(),
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        final NewMessage nm = new NewMessage(
                newMessageId,
                parentMessageId,
                forumId,
                subject,
                body
        );

        RojacWorker<Void, Void> sw = new RojacWorker<Void, Void>() {
            @Override
            protected Void perform() throws Exception {
                try {
                    INewMessageAH nmAH = Storage.get(INewMessageAH.class);
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
                } catch (InterruptedException | ExecutionException e) {
                    JLOptionPane.showMessageDialog(EditMessageDialog.this, "Can not save changes");
                }
                new NewMessagesUpdatedPacket().dispatch();
            }
        };

        sw.execute();
    }

    private void initializeLayout() {
        JPanel cp = new JPanel(new BorderLayout(5, 10));

        final JSplitPane center = new JSplitPane(JSplitPane.VERTICAL_SPLIT, true, panelEdit, panelPreview);
        center.setOneTouchExpandable(true);
        center.setResizeWeight(0.5);
        center.setDividerLocation(Integer.MAX_VALUE);

        cp.add(center, BorderLayout.CENTER);

        cp.add(WindowsUtils.createButtonsBar(
                this,
                Button_Save,
                new AButtonAction(Button_Save) {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        saveMessage();
                    }
                },
                new AButtonAction(Button_Preview) {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        panelEdit.forcePreview();
                        if (center.getDividerLocation() <= 1) {
                            center.setDividerLocation(Integer.MAX_VALUE);
                        } else {
                            center.setDividerLocation(0);
                        }
                    }
                },
                new AButtonAction(Button_Cancel) {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        dispose();
                    }
                }
        ), BorderLayout.SOUTH);

        setContentPane(cp);
    }

    private class NewMessageLoader extends MessageLoader {
        private NewMessageLoader(int messageId) {
            super(messageId, messageId);
        }

        @Override
        protected MessageInfo loadMessage() throws RojacException {
            try {
                NewMessage newMessage = Storage.get(INewMessageAH.class).getNewMessageById(messageId);
                MessageData messageData = new NewMessageData(newMessage);
                String messageBody = MessageUtils.removeTagline(newMessage.getMessage());
                parentMessageId = newMessage.getParentId();
                return new MessageInfo(messageData, messageBody, messageData.getSubject());
            } catch (StorageException e) {
                log.error("Can't load message #" + messageId, e);
                throw new RojacException("Can't load message #" + messageId, e);
            }
        }
    }

    private class ReplyLoader extends MessageLoader {
        public ReplyLoader(int messageId) {
            super(messageId, 0);
            parentMessageId = messageId;
        }

        @Override
        protected MessageInfo loadMessage() throws RojacException {
            MessageInfo messageInfo;
            try {
                IMessageAH messageAH = Storage.get(IMessageAH.class);
                MessageData messageData = messageAH.getMessageData(messageId);
                String messageBody = messageAH.getMessageBodyById(messageId);

                messageInfo = new MessageInfo(
                        messageData,
                        MessageUtils.correctBody(messageBody, messageData.getUserName()),
                        MessageUtils.correctSubject(messageData.getSubject())
                );
            } catch (StorageException e) {
                log.error("Can't load message #" + messageId, e);
                throw new RojacException("Can't load message #" + messageId, e);
            }
            return messageInfo;
        }

    }

    private abstract class MessageLoader extends RojacWorker<Void, MessageInfo> {
        protected final int messageId;
        protected int parentMessageId;
        protected final int newMessageId;

        public MessageLoader(int messageId, int newMessageId) {
            this.messageId = messageId;
            this.newMessageId = newMessageId;
        }

        @Override
        protected Void perform() throws Exception {
            publish(loadMessage());

            return null;
        }

        protected abstract MessageInfo loadMessage() throws RojacException;

        @Override
        protected void process(List<MessageInfo> chunks) {
            for (MessageInfo messageInfo : chunks) {
                forumId = messageInfo.getMessageData().getForumId();
                EditMessageDialog.this.parentMessageId = parentMessageId;
                EditMessageDialog.this.newMessageId = newMessageId;

                panelEdit.setMessage(messageInfo.getBody(), messageInfo.getSubj());

                WindowsUtils.center(EditMessageDialog.this, getOwner());
                setVisible(true);

            }
        }

        @Override
        protected void done() {
            if (this.isCancelled()) {
                JLOptionPane.showMessageDialog(
                        EditMessageDialog.this,
                        ErrorDialog_MessageNotFound_Message.get(messageId),
                        ErrorDialog_MessageNotFound_Title.get(messageId),
                        JOptionPane.WARNING_MESSAGE
                );
            }
        }
    }


    private static final class MessageInfo {
        private final MessageData messageData;
        private final String body;
        private final String subj;

        private MessageInfo(MessageData messageData, String body, String subj) {
            this.body = body;
            this.messageData = messageData;
            this.subj = subj;
        }

        public String getBody() {
            return body;
        }

        public String getSubj() {
            return subj;
        }

        public MessageData getMessageData() {
            return messageData;
        }
    }
}
