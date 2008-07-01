package org.xblackcat.sunaj.service.storage.database;

import org.xblackcat.sunaj.data.NewModerate;
import org.xblackcat.sunaj.service.storage.INewModerateAH;
import org.xblackcat.sunaj.service.storage.StorageException;
import org.xblackcat.sunaj.service.storage.database.convert.Converters;

import java.util.Collection;

/**
 * Date: 10 трав 2007
 *
 * @author ASUS
 */

final class DBNewModerateAH implements INewModerateAH {
    private final IQueryExecutor helper;

    DBNewModerateAH(IQueryExecutor helper) {
        this.helper = helper;
    }

    public void storeNewModerate(NewModerate nm) throws StorageException {
        helper.update(DataQuery.STORE_OBJECT_NEW_MODERATE,
                nm.getId(),
                nm.getMessageId(),
                nm.getAction().getCode(),
                nm.getForumId(),
                nm.getDescription(),
                nm.isAsModerator()
        );
    }

    public boolean removeNewModerate(int id) throws StorageException {
        return helper.update(DataQuery.REMOVE_OBJECT_NEW_MODERATE, id) > 0;
    }

    public NewModerate getNewModerateById(int id) throws StorageException {
        return helper.executeSingle(Converters.TO_NEW_MODERATE_CONVERTER, DataQuery.GET_OBJECT_NEW_MODERATE, id);
    }

    public NewModerate[] getAllNewModerates() throws StorageException {
        Collection<NewModerate> newModerates = helper.execute(Converters.TO_NEW_MODERATE_CONVERTER, DataQuery.GET_OBJECTS_NEW_MODERATES);
        return newModerates.toArray(new NewModerate[newModerates.size()]);
    }
}