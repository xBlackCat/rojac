package org.xblackcat.rojac.service.storage.database;

import org.xblackcat.rojac.data.NewMessage;
import org.xblackcat.rojac.service.storage.INewMessageAH;
import org.xblackcat.rojac.service.storage.StorageException;
import org.xblackcat.rojac.service.storage.database.convert.Converters;

import java.util.Collection;

/**
 * @author ASUS
 */

final class DBNewMessageAH implements INewMessageAH {
    private final IQueryExecutor helper;

    DBNewMessageAH(IQueryExecutor helper) {
        this.helper = helper;
    }

    public void storeNewMessage(NewMessage nm) throws StorageException {
        helper.update(
                DataQuery.STORE_OBJECT_NEW_MESSAGE,
                nm.getParentId(),
                nm.getForumId(),
                nm.getSubject(),
                nm.getMessage()
        );
    }

    @Override
    public void updateNewMessage(NewMessage nm) throws StorageException {
        helper.update(
                DataQuery.UPDATE_OBJECT_NEW_MESSAGE,
                nm.getSubject(),
                nm.getMessage(),
                nm.getLocalMessageId()
        );
    }

    public boolean removeNewMessage(int id) throws StorageException {
        return helper.update(DataQuery.REMOVE_OBJECT_NEW_MESSAGE, id) > 0;
    }

    public NewMessage getNewMessageById(int id) throws StorageException {
        return helper.executeSingle(Converters.TO_NEW_MESSAGE_CONVERTER, DataQuery.GET_OBJECT_NEW_MESSAGE, id);
    }

    public NewMessage[] getAllNewMessages() throws StorageException {
        Collection<NewMessage> newMessages = helper.execute(Converters.TO_NEW_MESSAGE_CONVERTER, DataQuery.GET_OBJECTS_NEW_MESSAGE);
        return newMessages.toArray(new NewMessage[newMessages.size()]);
    }
}
