package org.xblackcat.rojac.gui.view.message;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xblackcat.rojac.data.Mark;
import org.xblackcat.rojac.data.MessageData;
import org.xblackcat.rojac.data.NewMessage;
import org.xblackcat.rojac.gui.IInternationazable;
import org.xblackcat.rojac.gui.IRootPane;
import org.xblackcat.rojac.gui.popup.PopupMenuBuilder;
import org.xblackcat.rojac.i18n.JLOptionPane;
import org.xblackcat.rojac.i18n.Messages;
import org.xblackcat.rojac.service.ServiceFactory;
import org.xblackcat.rojac.service.converter.IMessageParser;
import org.xblackcat.rojac.service.janus.commands.AffectedIds;
import org.xblackcat.rojac.service.storage.StorageException;
import org.xblackcat.rojac.util.LinkUtils;
import org.xblackcat.rojac.util.RojacWorker;
import org.xblackcat.rojac.util.WindowsUtils;
import org.xblackcat.utils.ResourceUtils;

import javax.swing.*;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.text.html.HTMLEditorKit;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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

    private final JTextPane messageTextPane = new JTextPane();

    private final JLabel labelTopic = new JLabel();
    private final JButton marksButton = new JButton();
    private final JLabel userInfoLabel = new JLabel();
    private final JLabel messageDateLabel = new JLabel();

    private int messageId;
    private int forumId;

    private JLabel userLabel;
    private JLabel dateLabel;
    protected JButton answer;
    protected JComboBox marks;

    public MessageView(IRootPane mainFrame) {
        super(mainFrame);

        initialize();

        loadLabels();
    }

    private void initialize() {
        messageTextPane.setEditorKit(new HTMLEditorKit());
        messageTextPane.setEditable(false);
        messageTextPane.addHyperlinkListener(new HyperlinkHandler());

        add(createTitleBar(), BorderLayout.NORTH);
        add(new JScrollPane(messageTextPane, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED), BorderLayout.CENTER);
    }

    private Component createTitleBar() {
        JPanel p = new JPanel(new BorderLayout(5, 5));
        JPanel labelPane = new JPanel(new BorderLayout());
        p.add(labelPane, BorderLayout.NORTH);

        labelPane.add(labelTopic, BorderLayout.CENTER);
        labelPane.add(marksButton, BorderLayout.EAST);

        marksButton.setDefaultCapable(false);
        marksButton.setFocusable(false);
        marksButton.setFocusPainted(false);
        marksButton.setMargin(WindowsUtils.EMPTY_MARGIN);
        marksButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                RatingDialog rd = new RatingDialog(SwingUtilities.windowForComponent(MessageView.this), messageId);
                WindowsUtils.center(rd, marksButton);
                rd.setVisible(true);
            }
        });
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

        marks = new JComboBox(marksModel);
        marks.setFocusable(false);
        marks.setToolTipText(Messages.DESCRIPTION_MARK_SELECT.get());
        marks.setRenderer(markRender);
        marks.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                marks.setPopupVisible(false);
                chooseMark(marksModel.getSelectedItem());
                marksModel.reset();
            }
        });

        answer = WindowsUtils.setupImageButton("reply", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                mainFrame.editMessage(forumId, messageId);
            }
        }, Messages.BUTTON_REPLY_TOOLTIP);

        JPanel controls = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
        controls.add(answer);
        controls.add(marks);
        p.add(controls, BorderLayout.EAST);

        userLabel = new JLabel();
        dateLabel = new JLabel();

        JPanel labels = new JPanel(new GridLayout(0, 1));
        p.add(labels, BorderLayout.WEST);
        labels.add(userLabel);
        labels.add(dateLabel);

        JPanel infoPane = new JPanel(new GridLayout(0, 1));
        p.add(infoPane, BorderLayout.CENTER);

        infoPane.add(userInfoLabel);
        infoPane.add(messageDateLabel);

        return p;
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
            executor.execute(new MarksUpdater(mark));
        }
    }

    public void loadItem(final int messageId) {
        this.messageId = messageId;

        executor.execute(new MessageLoader(messageId));
    }

    protected void fillFrame(NewMessage mes) {
        forumId = mes.getForumId();

        String message = mes.getMessage();
        String converted = rsdnToHtml.convert(message);
        messageTextPane.setText(converted);
        messageTextPane.setCaretPosition(0);
        labelTopic.setText(mes.getSubject());
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
        labelTopic.setText(mes.getSubject());
        userInfoLabel.setText(mes.getUserName());
        DateFormat df = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.MEDIUM, Messages.getLocale());
        messageDateLabel.setText(df.format(new Date(mes.getMessageDate())));
        answer.setEnabled(true);
        marks.setEnabled(true);
    }

    public void updateData(AffectedIds ids) {
        if (ids.containsMessage(this.messageId)) {
            loadItem(messageId);
        }
    }

    private void fillMarksButton(Mark[] ratings) {
        boolean empty = ArrayUtils.isEmpty(ratings);

        marksButton.setVisible(!empty);
        if (empty) {
            marksButton.setText("No marks");
            return;
        }

        int smiles = 0;
        int agrees = 0;
        int disagrees = 0;
        int plusOnes = 0;
        int rate = 0;
        int rateAmount = 0;

        for (Mark r : ratings) {
            switch (r) {
                case Agree:
                    ++agrees;
                    break;
                case Disagree:
                    ++disagrees;
                    break;
                case PlusOne:
                    ++plusOnes;
                    break;
                case Remove:
                    // Do nothig
                    break;
                case Smile:
                    ++smiles;
                    break;
                case x3:
                    ++rate;
                case x2:
                    ++rate;
                case x1:
                    ++rate;
                    ++rateAmount;
                    break;
            }
        }

        StringBuilder text = new StringBuilder("<html><body>");

        if (rateAmount > 0) {
            text.append("<b>");
            text.append(rate);
            text.append("(");
            text.append(rateAmount);
            text.append(")</b> ");
        }

        text.append(addInfo(Mark.PlusOne, plusOnes));
        text.append(addInfo(Mark.Agree, agrees));
        text.append(addInfo(Mark.Disagree, disagrees));
        text.append(addInfo(Mark.Smile, smiles));

        marksButton.setText(text.toString());
        revalidate();
    }

    private String addInfo(Mark m, int amount) {
        if (amount > 0) {
            String res = "&nbsp;<img src='" + m.getUrl().toString() + "'>";
            if (amount > 1) {
                return res + "<i>x" + amount + "</i>";
            } else {
                return res;
            }
        }
        return "";
    }

    public void loadLabels() {
        answer.setToolTipText(Messages.BUTTON_REPLY_TOOLTIP.get());
        userLabel.setText(Messages.MESSAGE_PANE_USER_LABEL.get());
        dateLabel.setText(Messages.MESSAGE_PANE_DATE_LABEL.get());
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
}
