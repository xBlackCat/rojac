package org.xblackcat.rojac.gui.view.message;

import org.xblackcat.rojac.data.MessageData;
import org.xblackcat.rojac.data.NewMessage;
import org.xblackcat.rojac.data.NewMessageData;
import org.xblackcat.rojac.gui.IAppControl;
import org.xblackcat.rojac.gui.IState;
import org.xblackcat.rojac.gui.IViewLayout;
import org.xblackcat.rojac.gui.NoViewLayout;
import org.xblackcat.rojac.gui.popup.PopupMenuBuilder;
import org.xblackcat.rojac.gui.view.AnItemView;
import org.xblackcat.rojac.gui.view.IPostEvent;
import org.xblackcat.rojac.gui.view.ViewId;
import org.xblackcat.rojac.service.datahandler.*;
import org.xblackcat.rojac.service.options.Property;
import org.xblackcat.rojac.service.storage.IMessageAH;
import org.xblackcat.rojac.service.storage.INewMessageAH;
import org.xblackcat.rojac.service.storage.Storage;
import org.xblackcat.rojac.service.storage.StorageException;
import org.xblackcat.rojac.util.MessageUtils;
import org.xblackcat.rojac.util.RojacWorker;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * @author xBlackCat
 */

public class MessageView extends AnItemView {
    private int messageId;
    private final Runnable onLoaded;

    protected MessageData messageData;
    private String messageTitle;

    private final PacketDispatcher packetDispatcher = new PacketDispatcher(
            new IPacketProcessor<ReloadDataPacket>() {
                @Override
                public void process(ReloadDataPacket p) {
                    loadItem(getId().getId());
                }
            },
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
    private final MessagePane messagePane;

    private void updateReadState(boolean read) {
        messageData = messageData.setRead(read);
        fireInfoChanged();
    }

    public MessageView(ViewId id, IAppControl appControl) {
        this(id, appControl, null, null);
    }

    public MessageView(ViewId id, IAppControl appControl, Runnable onLoaded, final IPostEvent onScrollEndEvent) {
        super(id, appControl);
        this.onLoaded = onLoaded;

        if (id != null) {
            messageTitle = "#" + id.getId();
        } else {
            messageTitle = "#";
        }

        Runnable onScrollEnd = new Runnable() {
            @Override
            public void run() {
                if (Property.VIEW_THREAD_SET_READ_ON_SCROLL.get()) {
                    if (!messageData.isRead()) {
                        MessageUtils.markMessageRead(getId(), messageData, 0);
                        messageData = messageData.setRead(true);
                    }
                }

                if (onScrollEndEvent != null) {
                    onScrollEndEvent.fired(messageId);
                }
            }
        };
        
        messagePane = new MessagePane(appControl, onScrollEnd);
        add(messagePane, BorderLayout.CENTER);
    }

    public void loadItem(final int messageId) {
        this.messageId = messageId;
        messageTitle = "#" + messageId;

        if (messageId != 0) {
            messagePane.setLoading(messageId > 0);

            new MessageLoader(messageId, onLoaded).execute();
        } else {
            messagePane.hideControls();
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

    private class MessageLoader extends RojacWorker<Void, MessageDataHolder> {
        private final int messageId;

        public MessageLoader(int messageId, Runnable onLoaded) {
            super(onLoaded);
            this.messageId = messageId;
        }

        @Override
        protected Void perform() throws Exception {
            if (messageId != 0) {
                String messageBody;
                MessageData messageData;
                try {
                    if (messageId > 0) {
                        // Regular message
                        IMessageAH messageAH = Storage.get(IMessageAH.class);
                        messageData = messageAH.getMessageData(messageId);
                        if (messageData == null) {
                            // Somehow message is not found - do not load it
                            return null;
                        }
                        messageBody = messageAH.getMessageBodyById(messageId);
                    } else {
                        // Local message
                        NewMessage newMessage = Storage.get(INewMessageAH.class).getNewMessageById(-messageId);
                        messageData = new NewMessageData(newMessage);

                        messageBody = newMessage.getMessage();
                    }
                } catch (StorageException e) {
                    throw new RuntimeException("Can't load message #" + messageId, e);
                }

                try {
                    publish(new MessageDataHolder(messageData, messageBody));
                } catch (Exception e) {
                    throw new RuntimeException("Can't parse message #" + messageId + ". Body: " + messageBody, e);
                }
            } else {
                publish(new MessageDataHolder(null, null));
            }

            return null;
        }

        @Override
        protected void process(List<MessageDataHolder> chunks) {
            for (MessageDataHolder md : chunks) {
                messageData = md.getMessage();
                messagePane.fillFrame(messageData, md.getMessageBody());
                if (messageData == null) {
                    continue;
                }

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
    }
}
