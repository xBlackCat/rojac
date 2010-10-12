package org.xblackcat.rojac.gui.view.message;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xblackcat.rojac.data.Mark;
import org.xblackcat.rojac.data.MessageData;
import org.xblackcat.rojac.data.NewMessage;
import org.xblackcat.rojac.gui.IInternationazable;
import org.xblackcat.rojac.gui.IRootPane;
import org.xblackcat.rojac.gui.component.AButtonAction;
import org.xblackcat.rojac.gui.popup.PopupMenuBuilder;
import org.xblackcat.rojac.i18n.JLOptionPane;
import org.xblackcat.rojac.i18n.Messages;
import org.xblackcat.rojac.service.ServiceFactory;
import org.xblackcat.rojac.service.converter.IMessageParser;
import org.xblackcat.rojac.service.datahandler.ProcessPacket;
import org.xblackcat.rojac.service.janus.commands.AffectedMessage;
import org.xblackcat.rojac.service.storage.StorageException;
import org.xblackcat.rojac.util.LinkUtils;
import org.xblackcat.rojac.util.MessageUtils;
import org.xblackcat.rojac.util.RojacWorker;
import org.xblackcat.rojac.util.WindowsUtils;
import org.xblackcat.utils.ResourceUtils;

import javax.swing.*;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.text.html.HTMLEditorKit;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.text.DateFormat;
import java.util.Date;
import java.util.List;

import static org.xblackcat.rojac.service.options.Property.RSDN_USER_NAME;

/**
 * @author xBlackCat
 */

public class MessageView extends AItemView implements IInternationazable {
    private static final Log log = LogFactory.getLog(MessageView.class);
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
    private JComboBox marks;
    private String messageTitle = "#";

    protected final JPanel controls = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
    protected final JComponent titleBar = createTitleBar();


    public MessageView(IRootPane mainFrame) {
        super(mainFrame);

        initialize();

        loadLabels();
    }

    private void initialize() {
        messageTextPane.setEditorKit(new HTMLEditorKit());
        messageTextPane.setEditable(false);
        messageTextPane.addHyperlinkListener(new HyperlinkHandler());

        add(titleBar, BorderLayout.NORTH);
        add(new JScrollPane(messageTextPane, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED), BorderLayout.CENTER);
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
        labelPane.add(messageInfoPane, BorderLayout.EAST);
        messageInfoPane.add(dateLabel);
        messageInfoPane.add(messageDateLabel);

        marksButton = WindowsUtils.setupImageButton(null, new ShowMarksAction());
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

        AButtonAction marksAction = new AButtonAction(Messages.DESCRIPTION_MARK_SELECT) {
            public void actionPerformed(ActionEvent e) {
                marks.setPopupVisible(false);
                chooseMark(marksModel.getSelectedItem());
                marksModel.reset();
            }
        };

        marks = new JComboBox(marksModel);
        marks.setFocusable(false);
        marks.setToolTipText(marksAction.getMessage().get());
        marks.setRenderer(markRender);
        marks.addActionListener(marksAction);

        answer = WindowsUtils.setupImageButton("reply", new ReplyAction());

        controls.add(answer);
        controls.add(marks);
        controls.add(marksButton);
        titlePane.add(controls, BorderLayout.SOUTH);

        return titlePane;
    }

    /**
     * Assigns specified mark to the message
     *
     * @param mark new mark
     */
    private void chooseMark(final Mark mark) {
        if (JOptionPane.YES_OPTION ==
                JLOptionPane.showConfirmDialog(
                        this,
                        Messages.DIALOG_SET_MARK_MESSAGE.get(mark),
                        Messages.DIALOG_SET_MARK_TITLE.get(),
                        JOptionPane.YES_NO_OPTION
                )) {
            new MarksUpdater(mark).execute();
        }
    }

    public void loadItem(final AffectedMessage message) {
        messageId = message.getMessageId();
        messageTitle = "#" + messageId;

        new MessageLoader(messageId).execute();
    }

    protected void fillFrame(NewMessage mes) {
        forumId = mes.getForumId();

        String message = mes.getMessage();
        String converted = rsdnToHtml.convert(message);
        messageTextPane.setText(converted);
        messageTextPane.setCaretPosition(0);
        userInfoLabel.setText(RSDN_USER_NAME.get());
        DateFormat df = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.MEDIUM, Messages.getLocale());
        messageDateLabel.setText(df.format(new Date()));
        answer.setEnabled(false);
        marks.setEnabled(false);
    }

    protected void fillFrame(MessageData mes, String parsedText) {
        forumId = mes.getForumId();

        messageTextPane.setText(parsedText);
        messageTextPane.setCaretPosition(0);
        userInfoLabel.setText(mes.getUserName());
        DateFormat df = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.MEDIUM, Messages.getLocale());
        messageDateLabel.setText(df.format(new Date(mes.getMessageDate())));
        answer.setEnabled(true);
        marks.setEnabled(true);
    }

    @Override
    public String getTabTitle() {
        return messageTitle;
    }

    public void processPacket(ProcessPacket ids) {
        if (this.messageId != 0 && ids.containsMessage(this.messageId)) {
            loadItem(new AffectedMessage(AffectedMessage.DEFAULT_FORUM, messageId));
        }
    }

    public void loadLabels() {
        answer.setToolTipText(Messages.BUTTON_REPLY_TOOLTIP.get());
        userLabel.setText(Messages.MESSAGE_PANE_USER_LABEL.get());
        dateLabel.setText(Messages.MESSAGE_PANE_DATE_LABEL.get());
    }

    @Override
    public void makeVisible(int messageId) {
        if (messageId != this.messageId) {
            loadItem(new AffectedMessage(AffectedMessage.DEFAULT_FORUM, messageId));
        }
    }

    @Override
    public boolean containsItem(int messageId) {
        return messageId == this.messageId;
    }

    private void fillMarksButton(Mark[] ratings) {
        boolean empty = ArrayUtils.isEmpty(ratings);

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

    private class HyperlinkHandler implements HyperlinkListener {
        public void hyperlinkUpdate(HyperlinkEvent e) {
            if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
                JPopupMenu menu = PopupMenuBuilder.getLinkMenu(
                        e.getURL(),
                        e.getDescription(),
                        LinkUtils.getUrlText(e.getSourceElement()),
                        mainFrame
                );

                Point l = MouseInfo.getPointerInfo().getLocation();
                SwingUtilities.convertPointFromScreen(l, messageTextPane);
                menu.show(messageTextPane, l.x, l.y);
            } else if (e.getEventType() == HyperlinkEvent.EventType.ENTERED) {

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
            String messageBody = "";
            try {
                MessageData messageData = storage.getMessageAH().getMessageData(messageId);
                messageBody = storage.getMessageAH().getMessageBodyById(messageId);
                Mark[] ratings = storage.getRatingAH().getRatingMarksByMessageId(messageId);
                Mark[] ownRatings = storage.getNewRatingAH().getNewRatingMarksByMessageId(messageId);

                String parsedText = rsdnToHtml.convert(messageBody);

                publish(new MessageDataHolder(messageData, parsedText, (Mark[]) ArrayUtils.addAll(ratings, ownRatings)));
            } catch (StorageException e) {
                throw new RuntimeException("Can't load message #" + messageId, e);
            } catch (Exception e) {
                throw new RuntimeException("Can't parse message #" + messageId + ". Body: " + messageBody, e);
            }

            return null;
        }

        @Override
        protected void process(List<MessageDataHolder> chunks) {
            for (MessageDataHolder messageData : chunks) {
                fillFrame(messageData.getMessage(), messageData.getMessageBody());
                fillMarksButton(messageData.getMarks());
                messageTitle = "#" + messageId + " " + messageData.getMessage().getSubject();
                fireItemUpdated(new AffectedMessage(messageId, forumId));
            }
        }
    }

    private static class MessageDataHolder {
        private final MessageData message;
        private final String messageBody;
        private final Mark[] marks;

        private MessageDataHolder(MessageData message, String messageBody, Mark[] marks) {
            this.message = message;
            this.messageBody = messageBody;
            this.marks = marks;
        }

        public MessageData getMessage() {
            return message;
        }

        public Mark[] getMarks() {
            return marks;
        }

        public String getMessageBody() {
            return messageBody;
        }
    }

    private class MarksUpdater extends RojacWorker<Void, Mark> {
        private final Mark mark;

        public MarksUpdater(Mark mark) {
            this.mark = mark;
        }

        @Override
        protected Void perform() throws Exception {
            storage.getNewRatingAH().storeNewRating(messageId, mark);
            Mark[] ratings = storage.getRatingAH().getRatingMarksByMessageId(messageId);

            Mark[] ownRatings = storage.getNewRatingAH().getNewRatingMarksByMessageId(messageId);

            publish((Mark[]) ArrayUtils.addAll(ratings, ownRatings));

            return null;
        }

        @Override
        protected void process(List<Mark> chunks) {
            fillMarksButton(chunks.toArray(new Mark[chunks.size()]));
        }

        @Override
        protected void done() {
            if (isCancelled()) {
                JLOptionPane.showMessageDialog(
                        MessageView.this,
                        Messages.ERROR_DIALOG_SET_MARK_MESSAGE.get(mark),
                        Messages.ERROR_DIALOG_SET_MARK_MESSAGE.get(),
                        JOptionPane.DEFAULT_OPTION
                );
                if (log.isWarnEnabled()) {
                    log.warn("Cann't store mark " + mark + " for message [id=" + messageId + "].");
                }
            }
        }
    }

    private class ReplyAction extends AButtonAction {
        public ReplyAction() {
            super(Messages.BUTTON_REPLY_TOOLTIP);
        }

        public void actionPerformed(ActionEvent e) {
            mainFrame.editMessage(forumId, messageId);
        }
    }

    private class ShowMarksAction extends AButtonAction {
        public ShowMarksAction() {
            super(Messages.BUTTON_MARKS_TOOLTIP);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            RatingDialog rd = new RatingDialog(SwingUtilities.windowForComponent(MessageView.this), messageId);
            WindowsUtils.center(rd, marksButton);
            rd.setVisible(true);
        }
    }
}
