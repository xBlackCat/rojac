package org.xblackcat.rojac.service.storage.database;

import org.xblackcat.rojac.data.NewModerate;
import org.xblackcat.rojac.service.storage.INewModerateAH;
import org.xblackcat.rojac.service.storage.StorageException;
import org.xblackcat.rojac.service.storage.database.convert.Converters;

/**
 * @author ASUS
 */

final class DBNewModerateAH extends AnAH implements INewModerateAH {
    DBNewModerateAH(IQueryHolder helper) {
        super(helper);
    }

    public void storeNewModerate(NewModerate nm) throws StorageException {
        helper.update(DataQuery.STORE_OBJECT_NEW_MODERATE,
                nm.getId(),
                nm.getMessageId(),
                nm.getAction().getId(),
                nm.getForumId(),
                nm.getDescription(),
                nm.isAsModerator()
        );
    }

    public boolean removeNewModerate(int id) throws StorageException {
        return helper.update(DataQuery.REMOVE_OBJECT_NEW_MODERATE, id) > 0;
    }

    public NewModerate getNewModerateById(int id) throws StorageException {
        return helper.executeSingle(Converters.TO_NEW_MODERATE, DataQuery.GET_OBJECT_NEW_MODERATE, id);
    }

    public org.xblackcat.rojac.service.storage.IResult<NewModerate> getAllNewModerates() throws StorageException {
        return helper.execute(Converters.TO_NEW_MODERATE, DataQuery.GET_OBJECTS_NEW_MODERATES);
    }
}