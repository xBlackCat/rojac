package org.xblackcat.sunaj.service.storage.database;

import org.xblackcat.sunaj.data.NewMessage;
import org.xblackcat.sunaj.service.storage.INewMessageAH;
import org.xblackcat.sunaj.service.storage.StorageException;
import org.xblackcat.sunaj.service.storage.database.convert.Converters;

import java.util.Collection;

/**
 * Date: 10 трав 2007
 *
 * @author ASUS
 */

final class DBNewMessageAH implements INewMessageAH {
    private final IQueryExecutor helper;

    DBNewMessageAH(IQueryExecutor helper) {
        this.helper = helper;
    }

    public void storeNewMessage(NewMessage nm) throws StorageException {
        helper.update(DataQuery.STORE_OBJECT_NEW_MESSAGE,
                nm.getLocalMessageId(),
                nm.getParentId(),
                nm.getForumId(),
                nm.getSubject(),
                nm.getMessage());
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
