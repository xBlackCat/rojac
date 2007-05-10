package org.xblackcat.sunaj.service.storage.database;

import org.xblackcat.sunaj.service.data.NewMessage;
import org.xblackcat.sunaj.service.storage.INewMessageDAO;
import org.xblackcat.sunaj.service.storage.StorageException;
import org.xblackcat.sunaj.service.storage.database.convert.ToNewMessageConvertor;

/**
 * Date: 10 трав 2007
 *
 * @author ASUS
 */

class DBNewMessageDAO implements INewMessageDAO {
    private final IQueryExecutor helper;

    DBNewMessageDAO(IQueryExecutor helper) {
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
        return helper.executeSingle(new ToNewMessageConvertor(), DataQuery.GET_OBJECT_NEW_MESSAGE, id);
    }

    public int[] getAllNewMessageIds() throws StorageException {
        return helper.getIds(DataQuery.GET_IDS_NEW_MESSAGE);
    }
}
