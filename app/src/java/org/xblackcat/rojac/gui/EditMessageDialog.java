package org.xblackcat.rojac.gui;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xblackcat.rojac.data.Forum;
import org.xblackcat.rojac.data.Message;
import org.xblackcat.rojac.data.NewMessage;
import org.xblackcat.rojac.gui.frame.message.EditMessagePane;
import org.xblackcat.rojac.gui.frame.message.PreviewMessagePane;
import org.xblackcat.rojac.gui.frame.progress.ITask;
import org.xblackcat.rojac.i18n.JLOptionPane;
import org.xblackcat.rojac.service.ServiceFactory;
import org.xblackcat.rojac.service.storage.INewMessageAH;
import org.xblackcat.rojac.service.storage.IStorage;
import org.xblackcat.rojac.service.storage.StorageException;
import org.xblackcat.rojac.util.MessageUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * Date: 11 זמגע 2008
 *
 * @author xBlackCat
 */

class EditMessageDialog extends JDialog {
    private static final Log log = LogFactory.getLog(EditMessageDialog.class);

    protected final IStorage storage;
    private final PreviewMessagePane panelPreview;
    private final EditMessagePane panelEdit;

    // new message credentials
    private int parentMessageId = 0;
    private int newMessageId = 0;
    private int forumId = 0;

    public EditMessageDialog(Frame owner) {
        super(owner, false);

        panelPreview = new PreviewMessagePane(new DumbRootPane());
        panelEdit = new EditMessagePane(panelPreview);
        storage = ServiceFactory.getInstance().getStorage();

        initializeLayout();

        panelEdit.forcePreview();
    }

    public void answerOn(int messageId) {
        Message message;
        try {
            message = storage.getMessageAH().getMessageById(messageId);
        } catch (StorageException e) {
            log.error("Can not load a message.", e);
            return;
        }

        forumId = message.getForumId();
        parentMessageId = messageId;

        String mes = MessageUtils.correctBody(message.getMessage(), message.getUserNick());
        String subj = MessageUtils.correctSubject(message.getSubject());

        panelEdit.setMessage(mes, subj);

        setVisible(true);
    }

    public void createTopic(int forumId) {
        this.forumId = forumId;

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

        setVisible(true);
    }

    private boolean saveMessage() {
        INewMessageAH nmAH = storage.getNewMessageAH();

        String body = panelEdit.getBody();

        if (newMessageId == 0) {
            body = MessageUtils.addOwnTagLine(body);
        }

        NewMessage nm = new NewMessage(
                newMessageId,
                parentMessageId,
                forumId,
                panelEdit.getSubject(),
                body
        );

        try {
            if (newMessageId == 0) {
                // Create a new own message
                nmAH.storeNewMessage(nm);
            } else {
                nmAH.updateNewMessage(nm);
            }
        } catch (StorageException e) {
            log.error("Can not store new message object: " + nm, e);
            return false;
        }

        return true;
    }

    private void initializeLayout() {
        JPanel cp = new JPanel(new BorderLayout());

        JSplitPane center = new JSplitPane(JSplitPane.VERTICAL_SPLIT, true, panelEdit, panelPreview);
        center.setOneTouchExpandable(true);
        center.setResizeWeight(0.5);
        center.setDividerLocation(1.);

        cp.add(center, BorderLayout.CENTER);

        JPanel buttonsPane = new JPanel(new FlowLayout());

        buttonsPane.add(new JButton(new AbstractAction("Save") {
            @Override
            public void actionPerformed(ActionEvent e) {
                boolean res = saveMessage();

                if (res) {
                    dispose();
                } else {
                    JLOptionPane.showMessageDialog(EditMessageDialog.this, "Can not save changes");
                }
            }
        }));
        buttonsPane.add(new JButton(new AbstractAction("Preview") {
            @Override
            public void actionPerformed(ActionEvent e) {
                panelEdit.forcePreview();
            }
        }));
        buttonsPane.add(new JButton(new AbstractAction("Cancel"){
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        }));

        cp.add(buttonsPane, BorderLayout.SOUTH);

        setContentPane(cp);
    }

    private static class DumbRootPane implements IRootPane {
        @Override
        public void openForumTab(Forum f) {
        }

        @Override
        public void showProgressDialog(ITask task) {
        }

        @Override
        public void editMessage(Integer forumId, Integer messageId) {
        }
    }
}
