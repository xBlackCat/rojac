package org.xblackcat.rojac.gui.view.message;

import net.java.balloontip.BalloonTip;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xblackcat.rojac.data.Mark;
import org.xblackcat.rojac.data.MessageData;
import org.xblackcat.rojac.data.NewMessage;
import org.xblackcat.rojac.data.RatingCache;
import org.xblackcat.rojac.gui.IAppControl;
import org.xblackcat.rojac.gui.component.AButtonAction;
import org.xblackcat.rojac.gui.component.ShortCut;
import org.xblackcat.rojac.i18n.JLOptionPane;
import org.xblackcat.rojac.i18n.LocaleControl;
import org.xblackcat.rojac.i18n.Message;
import org.xblackcat.rojac.service.ServiceFactory;
import org.xblackcat.rojac.service.converter.IMessageParser;
import org.xblackcat.rojac.service.storage.INewRatingAH;
import org.xblackcat.rojac.service.storage.Storage;
import org.xblackcat.rojac.util.MessageUtils;
import org.xblackcat.rojac.util.RojacWorker;
import org.xblackcat.rojac.util.ShortCutUtils;
import org.xblackcat.rojac.util.WindowsUtils;
import org.xblackcat.utils.ResourceUtils;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.text.html.HTMLEditorKit;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.text.DateFormat;
import java.util.Date;
import java.util.Iterator;

import static org.xblackcat.rojac.service.options.Property.RSDN_USER_NAME;

/**
 * 01.12.11 17:13
 *
 * @author xBlackCat
 */
public class MessagePane extends JPanel {
    private static final Log log = LogFactory.getLog(MessagePane.class);

    private final IMessageParser rsdnToHtml = ServiceFactory.getInstance().getMessageConverter();

    private final Runnable onScrollEnd;

    protected final JTextPane messageTextPane = new JTextPane();

    private JButton marksButton;
    private JLabel userInfoLabel = new JLabel();
    private JLabel messageDateLabel = new JLabel();
    private JLabel userLabel = new JLabel();
    private JLabel dateLabel = new JLabel();
    private JButton answer;
    private JComboBox<Mark> marks;

    protected final JPanel controls = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
    protected final JComponent titleBar = createTitleBar();
    private final IAppControl appControl;

    private MessageData messageData;

    public MessagePane(IAppControl appControl, Runnable onScrollEnd) {
        super(new BorderLayout());
        this.appControl = appControl;

        initialize();

        answer.setToolTipText(Message.Button_Reply_ToolTip.get());
        userLabel.setText(Message.Panel_Message_Label_User.get());
        dateLabel.setText(Message.Panel_Message_Label_Date.get());

        titleBar.setVisible(false);
        messageTextPane.setEnabled(false);

        this.onScrollEnd = onScrollEnd;
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

    private void initialize() {
        messageTextPane.setEditorKit(new HTMLEditorKit());
        messageTextPane.setEditable(false);
        messageTextPane.addHyperlinkListener(new HyperlinkHandler(MessagePane.this.appControl, messageTextPane));

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
                    if (onScrollEnd != null) {
                        onScrollEnd.run();
                    }
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

    protected void fillFrame(NewMessage mes) {
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

    public void fillFrame(MessageData mes, String messageBody) {
        messageData = mes;

        if (mes == null) {
            // No data
            messageTextPane.setEnabled(false);
            messageTextPane.setText(null);
            titleBar.setVisible(false);
            marksButton.setVisible(false);
            marksButton.setText("No marks");
            marksButton.setIcon(null);
            return;
        }

        messageTextPane.setEnabled(true);
        String parsedText = rsdnToHtml.convert(messageBody);
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

        fillMarksButton(mes.getRating());
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

    public void setLoading(boolean realMessage) {
        messageTextPane.setEnabled(true);
        titleBar.setVisible(realMessage);
    }

    public void hideControls() {
        messageTextPane.setText(null);
        messageTextPane.setEnabled(false);
        titleBar.setVisible(false);
    }

    private class ReplyAction extends AButtonAction {
        public ReplyAction() {
            super(ShortCut.ReplyOnMessage);
        }

        public void actionPerformed(ActionEvent e) {
            appControl.editMessage(messageData.getForumId(), messageData.getMessageId());
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
            final RatingPane rd = new RatingPane(messageData.getMessageId());

            rd.loadRating(new Runnable() {
                @Override
                public void run() {
                    LeftRightRoundedBalloonStyle tipStyle = new LeftRightRoundedBalloonStyle(5, 5, rd.getBackground(), Color.black);

                    LeftCenterPositioner positioner = new LeftCenterPositioner(15, 15);
                    JButton closeButton = WindowsUtils.balloonTipCloseButton(null);
                    final BalloonTip tip = new BalloonTip(marksButton, rd, tipStyle, positioner, closeButton);
                    tip.refreshLocation();
                    tip.setVisible(true);

                    rd.trackFocus(new Runnable() {
                        @Override
                        public void run() {
                            tip.closeBalloon();
                        }
                    });
                }
            });
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

    private class MarksUpdater extends RojacWorker<Void, RatingCache> {
        private final Mark mark;

        public MarksUpdater(Mark mark) {
            this.mark = mark;
        }

        @Override
        protected Void perform() throws Exception {
            Storage.get(INewRatingAH.class).storeNewRating(messageData.getMessageId(), mark);

            publish(MessageUtils.updateRatingCache(messageData.getMessageId()));

            return null;
        }

        @Override
        protected void process(java.util.List<RatingCache> chunks) {
            Iterator<RatingCache> iterator = chunks.iterator();
            if (iterator.hasNext()) {
                fillMarksButton(iterator.next());
            }
        }

        @Override
        protected void done() {
            if (isCancelled()) {
                JLOptionPane.showMessageDialog(
                        MessagePane.this,
                        Message.ErrorDialog_SetMark_Message.get(mark),
                        Message.ErrorDialog_SetMark_Title.get(),
                        JOptionPane.DEFAULT_OPTION
                );
                if (log.isWarnEnabled()) {
                    log.warn("Can't store mark " + mark + " for message [id=" + messageData.getMessageId() + "].");
                }
            }
        }
    }
}
