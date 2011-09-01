package org.xblackcat.rojac.gui.view.message;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xblackcat.rojac.data.*;
import org.xblackcat.rojac.gui.IAppControl;
import org.xblackcat.rojac.gui.IState;
import org.xblackcat.rojac.gui.IViewLayout;
import org.xblackcat.rojac.gui.NoViewLayout;
import org.xblackcat.rojac.gui.component.AButtonAction;
import org.xblackcat.rojac.gui.component.ShortCut;
import org.xblackcat.rojac.gui.popup.PopupMenuBuilder;
import org.xblackcat.rojac.gui.view.AnItemView;
import org.xblackcat.rojac.gui.view.ViewId;
import org.xblackcat.rojac.i18n.JLOptionPane;
import org.xblackcat.rojac.i18n.LocaleControl;
import org.xblackcat.rojac.i18n.Message;
import org.xblackcat.rojac.service.ServiceFactory;
import org.xblackcat.rojac.service.converter.IMessageParser;
import org.xblackcat.rojac.service.datahandler.*;
import org.xblackcat.rojac.service.options.Property;
import org.xblackcat.rojac.service.storage.StorageException;
import org.xblackcat.rojac.util.*;
import org.xblackcat.utils.ResourceUtils;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.text.html.HTMLEditorKit;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.text.DateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import static org.xblackcat.rojac.service.options.Property.RSDN_USER_NAME;

/**
 * @author xBlackCat
 */

public class MessageView extends AnItemView {
    private static final Log log = LogFactory.getLog(MessageView.class);
    public static final String MESSAGE_VIEWED_FLAG = "MessageViewed";
    public static final String MESSAGE_LOADED = "MessageLoaded";
    private final IMessageParser rsdnToHtml = ServiceFactory.getInstance().getMessageConverter();

    private int messageId;
    private int forumId;

    private final JTextPane messageTextPane = new JTextPane();

    private JButton marksButton;
    private JLabel userInfoLabel = new JLabel();
    private JLabel messageDateLabel = new JLabel();
    private JLabel userLabel = new JLabel();
    private JLabel dateLabel = new JLabel();
    private JButton answer;
    private JComboBox<Mark> marks;
    private String messageTitle;

    protected final JPanel controls = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
    protected final JComponent titleBar = createTitleBar();
    protected MessageData messageData;

    private final PacketDispatcher packetDispatcher = new PacketDispatcher(
            new IPacketProcessor<IMessageUpdatePacket>() {
                @Override
                public void process(IMessageUpdatePacket p) {
                    if (p.isMessageAffected(messageId)) {
                        if (p instanceof SetReadExPacket) {
                            updateReadState(((SetReadExPacket) p).isRead());
                        } else {
                            loadItem(messageId);
                        }
                    }
                }
            },
            new IPacketProcessor<SetPostReadPacket>() {
                @Override
                public void process(SetPostReadPacket p) {
                    if (p.getPost().getMessageId() == messageId) {
                        updateReadState(p.isRead());
                    }
                }
            },
            new IPacketProcessor<SetSubThreadReadPacket>() {
                @Override
                public void process(SetSubThreadReadPacket p) {
                    // ???
                    if (messageData != null &&
                            p.getPostId() == messageData.getThreadRootId()) {
                        updateReadState(p.isRead());
                    }
                }
            }
    );

    private void updateReadState(boolean read) {
        messageData = messageData.setRead(read);
        fireInfoChanged();
    }


    public MessageView(ViewId id, IAppControl appControl) {
        super(id, appControl);

        if (id != null) {
            messageTitle = "#" + id.getId();
        } else {
            messageTitle = "#";
        }

        initialize();

        answer.setToolTipText(Message.Button_Reply_ToolTip.get());
        userLabel.setText(Message.Panel_Message_Label_User.get());
        dateLabel.setText(Message.Panel_Message_Label_Date.get());

    }

    private void initialize() {
        messageTextPane.setEditorKit(new HTMLEditorKit());
        messageTextPane.setEditable(false);
        messageTextPane.addHyperlinkListener(new HyperlinkHandler());

        add(titleBar, BorderLayout.NORTH);
        final JScrollPane scrollPane = new JScrollPane(messageTextPane, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        add(scrollPane, BorderLayout.CENTER);

        ShortCutUtils.registerShortCuts(messageTextPane);

        // Handle keyboard events to emulate tree navigation in TreeTable
        getInputMap(WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0), "spaceScroll");

        getActionMap().put("spaceScroll", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (messageData == null) {
                    return;
                }

                JScrollBar scrollBar = scrollPane.getVerticalScrollBar();

                int oldValue = scrollBar.getValue();
                if (!scrollBar.isVisible() || oldValue + scrollBar.getHeight() >= scrollBar.getMaximum()) {
                    if (Property.VIEW_THREAD_SET_READ_ON_SCROLL.get()) {
                        if (!messageData.isRead()) {
                            MessageUtils.markMessageRead(getId(), messageData, 0);
                            messageData = messageData.setRead(true);
                        }
                    }

                    MessageView.this.firePropertyChange(MESSAGE_VIEWED_FLAG, 0, messageId);
                    return;
                }

                int blockSize = (int) (scrollBar.getHeight() * .8);

                int newValue = oldValue + blockSize;

                if (newValue < oldValue) {
                    newValue = scrollBar.getMaximum();
                }

                scrollBar.setValue(newValue);
            }
        });
    }

    private JComponent createTitleBar() {
        JPanel titlePane = new JPanel(new BorderLayout(5, 5));

        JPanel labelPane = new JPanel(new BorderLayout());
        titlePane.add(labelPane, BorderLayout.CENTER);

        JPanel userInfoPane = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        labelPane.add(userInfoPane, BorderLayout.WEST);
        userInfoPane.add(userLabel);
        userInfoPane.add(userInfoLabel);

        JPanel messageInfoPane = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        titlePane.add(messageInfoPane, BorderLayout.EAST);
        messageInfoPane.add(dateLabel);
        messageInfoPane.add(messageDateLabel);

        marksButton = WindowsUtils.registerImageButton(this, null, new ShowMarksAction());
        marksButton.setDefaultCapable(false);
        marksButton.setFocusable(false);
        marksButton.setFocusPainted(false);
        marksButton.setBorderPainted(false);
        marksButton.setHorizontalAlignment(SwingConstants.CENTER);
        marksButton.setVerticalAlignment(SwingConstants.BOTTOM);
        marksButton.setBackground(Color.white);
        marksButton.setOpaque(false);
        marksButton.setRolloverEnabled(true);
        marksButton.setVisible(false);
        labelPane.add(marksButton, BorderLayout.EAST);

        final IconsModel marksModel = new IconsModel(
                Mark.PlusOne,
                Mark.Agree,
                Mark.Disagree,
                Mark.Smile,
                Mark.x1,
                Mark.x2,
                Mark.x3
        );

        final MarkRender markRender = new MarkRender(ResourceUtils.loadIcon("images/marks/select.gif"));

        SelectMarkAction marksAction = new SelectMarkAction(marksModel);
        ShowMarkSelectorAction selectorAction = new ShowMarkSelectorAction();

        marks = new JComboBox<>(marksModel);
        marks.setFocusable(false);
        marks.setToolTipText(selectorAction.getMessage().get());
        marks.setRenderer(markRender);
        marks.addActionListener(marksAction);
        marks.addKeyListener(marksAction);

        WindowsUtils.registerAction(this, selectorAction);

        answer = WindowsUtils.registerImageButton(this, "reply", new ReplyAction());

        controls.add(answer);
        controls.add(marks);
        controls.setBorder(new EmptyBorder(2, 0, 2, 0));
        titlePane.add(controls, BorderLayout.WEST);

        return titlePane;
    }

    /**
     * Assigns specified mark to the message
     *
     * @param mark new mark
     */
    private void chooseMark(final Mark mark) {
        if (mark != null &&
                JOptionPane.YES_OPTION ==
                        JLOptionPane.showConfirmDialog(
                                this,
                                Message.Dialog_SetMark_Message.get(mark),
                                Message.Dialog_SetMark_Title.get(),
                                JOptionPane.YES_NO_OPTION
                        )) {
            new MarksUpdater(mark).execute();
        }
    }

    public void loadItem(final int messageId) {
        this.messageId = messageId;
        messageTitle = "#" + messageId;

        if (messageId != 0) {
            messageTextPane.setEnabled(false);
            titleBar.setVisible(false);

            new MessageLoader(messageId).execute();
        } else {
            messageTextPane.setEnabled(true);
            titleBar.setVisible(messageId > 0);
        }
    }

    protected void fillFrame(NewMessage mes) {
        forumId = mes.getForumId();

        String message = mes.getMessage();
        String converted = rsdnToHtml.convert(message);
        messageTextPane.setText(converted);
        messageTextPane.setCaretPosition(0);
        userInfoLabel.setText(RSDN_USER_NAME.get());
        DateFormat df = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.MEDIUM, LocaleControl.getInstance().getLocale());
        messageDateLabel.setText(df.format(new Date()));
        answer.setEnabled(false);
        marks.setEnabled(false);
    }

    protected void fillFrame(MessageData mes, String parsedText) {
        forumId = mes.getForumId();

        messageTextPane.setText(parsedText);
        messageTextPane.setCaretPosition(0);
        if (mes.getMessageId() > 0) {
            titleBar.setVisible(true);

            // Normal message
            final String userName = mes.getUserName();
            if (StringUtils.isNotEmpty(userName)) {
                userInfoLabel.setText(userName);
            } else {
                userInfoLabel.setText(Message.UserName_Anonymous.get());
            }
            DateFormat df = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.MEDIUM, LocaleControl.getInstance().getLocale());
            messageDateLabel.setText(df.format(new Date(mes.getMessageDate())));
            answer.setEnabled(true);
            marks.setEnabled(true);
        } else {
            titleBar.setVisible(false);
        }
    }

    @Override
    public String getTabTitle() {
        return messageTitle;
    }

    @Override
    public void makeVisible(int messageId) {
        if (messageId != this.messageId) {
            loadItem(messageId);
        }
    }

    @Override
    public IState getObjectState() {
        return null;
    }

    @Override
    public void setObjectState(IState state) {
        // No state for the view.
    }

    @Override
    public boolean containsItem(int messageId) {
        return messageId == this.messageId;
    }

    @Override
    public IViewLayout storeLayout() {
        return new NoViewLayout();
    }

    @Override
    public void setupLayout(IViewLayout o) {
    }

    private void fillMarksButton(RatingCache ratings) {
        boolean empty = ratings.isEmpty();

        marksButton.setVisible(!empty);
        if (empty) {
            // Just in case
            marksButton.setText("No marks");
            marksButton.setIcon(null);
            return;
        }

        marksButton.setIcon(MessageUtils.buildRateImage(ratings, marksButton.getFont(), marksButton.getForeground()));
        marksButton.setText(null);
        revalidate();
    }

    @Override
    public JPopupMenu getTabTitleMenu() {
        return PopupMenuBuilder.getMessageViewTabMenu(messageData, appControl);
    }

    @Override
    public Icon getTabTitleIcon() {
        return messageData == null ? null : MessageUtils.getPostIcon(messageData);
    }

    @Override
    public final void processPacket(IPacket packet) {
        packetDispatcher.dispatch(packet);
    }

    private class HyperlinkHandler implements HyperlinkListener {
        public void hyperlinkUpdate(HyperlinkEvent e) {
            if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
                JPopupMenu menu = PopupMenuBuilder.getLinkMenu(
                        e.getURL(),
                        e.getDescription(),
                        LinkUtils.getUrlText(e.getSourceElement()),
                        appControl
                );

                Point l = MouseInfo.getPointerInfo().getLocation();
                SwingUtilities.convertPointFromScreen(l, messageTextPane);
                menu.show(messageTextPane, l.x, l.y);
            } else if (e.getEventType() == HyperlinkEvent.EventType.ENTERED) {
                // TODO: show popup with message info
            }
        }
    }

    private class MessageLoader extends RojacWorker<Void, MessageDataHolder> {
        private final int messageId;

        public MessageLoader(int messageId) {
            this.messageId = messageId;
        }

        @Override
        protected Void perform() throws Exception {
            if (messageId == 0) {
                // No message
                messageTextPane.setEnabled(true);
                titleBar.setVisible(messageId > 0);
            } else {
                String messageBody;
                MessageData messageData;
                try {
                    if (messageId > 0) {
                        // Regulag message
                        messageData = storage.getMessageAH().getMessageData(messageId);
                        if (messageData == null) {
                            // Somehow message is not found - do not load it
                            return null;
                        }
                        messageBody = storage.getMessageAH().getMessageBodyById(messageId);
                    } else {
                        // Local message
                        NewMessage newMessage = storage.getNewMessageAH().getNewMessageById(-messageId);
                        messageData = new NewMessageData(newMessage);

                        messageBody = newMessage.getMessage();
                    }
                } catch (StorageException e) {
                    throw new RuntimeException("Can't load message #" + messageId, e);
                }

                try {
                    String parsedText = rsdnToHtml.convert(messageBody);

                    publish(new MessageDataHolder(messageData, parsedText));
                } catch (Exception e) {
                    throw new RuntimeException("Can't parse message #" + messageId + ". Body: " + messageBody, e);
                }
            }

            return null;
        }

        @Override
        protected void process(List<MessageDataHolder> chunks) {
            for (MessageDataHolder md : chunks) {
                messageData = md.getMessage();
                fillFrame(messageData, md.getMessageBody());
                fillMarksButton(messageData.getRating());
                messageTitle = messageData.getSubject();
                fireInfoChanged();

                if (!messageData.isRead()) {
                    Long delay = Property.VIEW_THREAD_AUTOSET_READ.get();
                    if (delay != null && delay >= 0) {
                        MessageUtils.markMessageRead(getId(), messageData, delay);
                    }
                }
            }
        }

        @Override
        protected void done() {
            MessageView.this.firePropertyChange(MESSAGE_LOADED, null, null);
        }
    }

    private static class MessageDataHolder {
        private final MessageData message;
        private final String messageBody;

        private MessageDataHolder(MessageData message, String messageBody) {
            this.message = message;
            this.messageBody = messageBody;
        }

        public MessageData getMessage() {
            return message;
        }

        public String getMessageBody() {
            return messageBody;
        }
    }

    private class MarksUpdater extends RojacWorker<Void, RatingCache> {
        private final Mark mark;

        public MarksUpdater(Mark mark) {
            this.mark = mark;
        }

        @Override
        protected Void perform() throws Exception {
            storage.getNewRatingAH().storeNewRating(messageId, mark);

            publish(MessageUtils.updateRatingCache(messageId));

            return null;
        }

        @Override
        protected void process(List<RatingCache> chunks) {
            Iterator<RatingCache> iterator = chunks.iterator();
            if (iterator.hasNext()) {
                fillMarksButton(iterator.next());
            }
        }

        @Override
        protected void done() {
            if (isCancelled()) {
                JLOptionPane.showMessageDialog(
                        MessageView.this,
                        Message.ErrorDialog_SetMark_Message.get(mark),
                        Message.ErrorDialog_SetMark_Title.get(),
                        JOptionPane.DEFAULT_OPTION
                );
                if (log.isWarnEnabled()) {
                    log.warn("Can't store mark " + mark + " for message [id=" + messageId + "].");
                }
            }
        }
    }

    private class ReplyAction extends AButtonAction {
        public ReplyAction() {
            super(ShortCut.ReplyOnMessage);
        }

        public void actionPerformed(ActionEvent e) {
            appControl.editMessage(forumId, messageId);
        }
    }

    private class ShowMarksAction extends AButtonAction {
        public ShowMarksAction() {
            super(ShortCut.ShowMessageMarks);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (!marksButton.isVisible()) {
                // No button - no marks.
                return;
            }
            RatingDialog rd = new RatingDialog(SwingUtilities.windowForComponent(MessageView.this), messageId);
            WindowsUtils.center(rd, marksButton);
            rd.setVisible(true);
        }
    }

    private class ShowMarkSelectorAction extends AButtonAction {
        public ShowMarkSelectorAction() {
            super(ShortCut.SetMarkOnMessage);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            marks.setFocusable(true);
            marks.setPopupVisible(true);
            marks.requestFocusInWindow();
            marks.requestFocus();
        }
    }

    private class SelectMarkAction extends AbstractAction implements KeyListener {
        private final IconsModel marksModel;

        public SelectMarkAction(IconsModel marksModel) {
            this.marksModel = marksModel;
        }

        public void actionPerformed(ActionEvent e) {
            if ((AWTEvent.MOUSE_EVENT_MASK & e.getModifiers()) != 0) {
                selectMark();
            }
        }

        private void selectMark() {
            marks.setPopupVisible(false);
            chooseMark(marksModel.getSelectedItem());
            marksModel.reset();
            marks.getParent().requestFocus();
        }

        private void checkKey(KeyEvent e) {
            if (marksModel.getSelectedItem() != null &&
                    (e.getKeyCode() == KeyEvent.VK_ENTER ||
                            e.getKeyCode() == KeyEvent.VK_SPACE)) {
                selectMark();
            } else if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                marksModel.reset();
                selectMark();
            }
        }

        @Override
        public void keyPressed(KeyEvent e) {
            checkKey(e);
        }

        @Override
        public void keyTyped(KeyEvent e) {
            checkKey(e);
        }

        @Override
        public void keyReleased(KeyEvent e) {
            checkKey(e);
        }
    }
}
