package org.xblackcat.rojac.service.storage.database;

import org.xblackcat.rojac.data.NewMessage;
import org.xblackcat.rojac.service.storage.INewMessageAH;
import org.xblackcat.rojac.service.storage.IResult;
import org.xblackcat.rojac.service.storage.StorageException;
import org.xblackcat.rojac.service.storage.database.convert.Converters;

/**
 * @author ASUS
 */

final class DBNewMessageAH extends AnAH implements INewMessageAH {
    DBNewMessageAH(IQueryHolder helper) {
        super(helper);
    }

    public void storeNewMessage(NewMessage nm) throws StorageException {
        long nextId = helper.executeSingle(
                Converters.TO_NUMBER,
                DataQuery.GET_NEXT_ID_NEW_MESSAGE
        ).longValue();
        helper.update(
                DataQuery.STORE_OBJECT_NEW_MESSAGE,
                nextId,
                nm.getParentId(),
                nm.getForumId(),
                nm.getSubject(),
                nm.getMessage(),
                nm.isDraft()
        );
    }

    @Override
    public void updateNewMessage(NewMessage nm) throws StorageException {
        helper.update(
                DataQuery.UPDATE_OBJECT_NEW_MESSAGE,
                nm.getSubject(),
                nm.getMessage(),
                nm.isDraft(),
                nm.getLocalMessageId()
        );
    }

    public boolean removeNewMessage(int id) throws StorageException {
        return helper.update(DataQuery.REMOVE_OBJECT_NEW_MESSAGE, id) > 0;
    }

    @Override
    public void purgeNewMessage() throws StorageException {
        helper.update(DataQuery.REMOVE_OBJECTS_NEW_MESSAGE);
    }

    public NewMessage getNewMessageById(int id) throws StorageException {
        return helper.executeSingle(Converters.TO_NEW_MESSAGE, DataQuery.GET_OBJECT_NEW_MESSAGE, id);
    }

    @Override
    public IResult<NewMessage> getNewMessagesToSend() throws StorageException {
        return helper.execute(Converters.TO_NEW_MESSAGE, DataQuery.GET_OBJECTS_NEW_MESSAGE_TO_SEND);
    }

    @Override
    public void setDraftFlag(boolean draft, int messageId) throws StorageException {
        helper.update(
                DataQuery.UPDATE_OBJECT_NEW_MESSAGE_DRAFT_FLAG,
                draft,
                messageId
        );
    }

    public IResult<NewMessage> getAllNewMessages() throws StorageException {
        return helper.execute(Converters.TO_NEW_MESSAGE, DataQuery.GET_OBJECTS_NEW_MESSAGE);
    }
}
