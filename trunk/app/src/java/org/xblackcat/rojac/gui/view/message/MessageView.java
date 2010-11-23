package org.xblackcat.rojac.gui.view.message;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xblackcat.rojac.data.Mark;
import org.xblackcat.rojac.data.MessageData;
import org.xblackcat.rojac.data.NewMessage;
import org.xblackcat.rojac.data.RatingCache;
import org.xblackcat.rojac.gui.IInternationazable;
import org.xblackcat.rojac.gui.IRootPane;
import org.xblackcat.rojac.gui.ViewId;
import org.xblackcat.rojac.gui.component.AButtonAction;
import org.xblackcat.rojac.gui.component.ShortCut;
import org.xblackcat.rojac.gui.popup.PopupMenuBuilder;
import org.xblackcat.rojac.i18n.JLOptionPane;
import org.xblackcat.rojac.i18n.Messages;
import org.xblackcat.rojac.service.ServiceFactory;
import org.xblackcat.rojac.service.converter.IMessageParser;
import org.xblackcat.rojac.service.datahandler.IMessageUpdatePacket;
import org.xblackcat.rojac.service.datahandler.IPacket;
import org.xblackcat.rojac.service.datahandler.IPacketProcessor;
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


    public MessageView(ViewId id, IRootPane mainFrame) {
        super(id, mainFrame);

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

        marks = new JComboBox(marksModel);
        marks.setFocusable(false);
        marks.setToolTipText(selectorAction.getMessage().get());
        marks.setRenderer(markRender);
        marks.addActionListener(marksAction);
        marks.addKeyListener(marksAction);

        WindowsUtils.registerAction(this, selectorAction);

        answer = WindowsUtils.registerImageButton(this, "reply", new ReplyAction());

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
        if (mark != null &&
                JOptionPane.YES_OPTION ==
                        JLOptionPane.showConfirmDialog(
                                this,
                                Messages.Dialog_SetMark_Message.get(mark),
                                Messages.Dialog_SetMark_Title.get(),
                                JOptionPane.YES_NO_OPTION
                        )) {
            new MarksUpdater(mark).execute();
        }
    }

    public void loadItem(final int messageId) {
        this.messageId = messageId;
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

    @Override
    @SuppressWarnings({"unchecked"})
    protected IPacketProcessor<IPacket>[] getProcessors() {
        return new IPacketProcessor[]{
                new IPacketProcessor<IMessageUpdatePacket>() {
                    @Override
                    public void process(IMessageUpdatePacket p) {
                        if (p.isMessageAffected(messageId)) {
                            loadItem(messageId);
                        }
                    }
                }

        };
    }

    public void loadLabels() {
        answer.setToolTipText(Messages.Button_Reply_ToolTip.get());
        userLabel.setText(Messages.Panel_Message_Label_User.get());
        dateLabel.setText(Messages.Panel_Message_Label_Date.get());
    }

    @Override
    public void makeVisible(int messageId) {
        if (messageId != this.messageId) {
            loadItem(messageId);
        }
    }

    @Override
    public int getVisibleId() {
        return messageId;
    }

    @Override
    public boolean containsItem(int messageId) {
        return messageId == this.messageId;
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

                String parsedText = rsdnToHtml.convert(messageBody);

                publish(new MessageDataHolder(messageData, parsedText));
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
                fillMarksButton(messageData.getMessage().getRating());
                messageTitle = "#" + messageId + " " + messageData.getMessage().getSubject();
                fireItemUpdated(forumId, messageId);
            }
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
                        Messages.ErrorDialog_SetMark_Message.get(mark),
                        Messages.ErrorDialog_SetMark_Message.get(),
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
            super(Messages.Button_Reply_ToolTip, ShortCut.ReplyOnMessage);
        }

        public void actionPerformed(ActionEvent e) {
            mainFrame.editMessage(forumId, messageId);
        }
    }

    private class ShowMarksAction extends AButtonAction {
        public ShowMarksAction() {
            super(Messages.Description_Mark_Select, ShortCut.ShowMessageMarks);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            RatingDialog rd = new RatingDialog(SwingUtilities.windowForComponent(MessageView.this), messageId);
            WindowsUtils.center(rd, marksButton);
            rd.setVisible(true);
        }
    }

    private class ShowMarkSelectorAction extends AButtonAction {
        public ShowMarkSelectorAction() {
            super(Messages.Description_Mark_Select, ShortCut.SetMarkOnMessage);
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
