package org.xblackcat.rojac.service.storage.database;

import org.xblackcat.rojac.service.storage.*;
import org.xblackcat.rojac.service.storage.database.connection.DatabaseSettings;
import org.xblackcat.rojac.service.storage.database.connection.IConnectionFactory;
import org.xblackcat.rojac.service.storage.database.connection.SimplePooledConnectionFactory;
import org.xblackcat.rojac.service.storage.database.helper.IQueryHelper;
import org.xblackcat.rojac.service.storage.database.helper.QueryHelper;

import java.util.HashMap;
import java.util.Map;

/**
 * @author ASUS
 */

public class DBStorage implements IStorage {
    private final IQueryHelper helper;
    private final IQueryHolder queryExecutor;

    private final Map<Class<? extends AH>, AH> accessHelpers = new HashMap<>();

    public DBStorage(DatabaseSettings settings) throws StorageException {
        IConnectionFactory connectionFactory = new SimplePooledConnectionFactory(settings);

        helper = new QueryHelper(connectionFactory);

        queryExecutor = new QueryHolder(helper);

        // Initialize the object AHs
        accessHelpers.put(IForumAH.class, new DBForumAH(queryExecutor));
        accessHelpers.put(IRatingAH.class, new DBRatingAH(queryExecutor));
        accessHelpers.put(IUserAH.class, new DBUserAH(queryExecutor));
        accessHelpers.put(IForumGroupAH.class, new DBForumGroupAH(queryExecutor));
        accessHelpers.put(IVersionAH.class, new DBVersionAH(queryExecutor));
        accessHelpers.put(INewRatingAH.class, new DBNewRatingAH(queryExecutor));
        accessHelpers.put(IModerateAH.class, new DBModerateAH(queryExecutor));
        accessHelpers.put(INewMessageAH.class, new DBNewMessageAH(queryExecutor));
        accessHelpers.put(IMessageAH.class, new DBMessageAH(queryExecutor));
        accessHelpers.put(IMiscAH.class, new DBMiscAH(queryExecutor));
        accessHelpers.put(INewModerateAH.class, new DBNewModerateAH(queryExecutor));
        accessHelpers.put(IFavoriteAH.class, new DBFavoriteAH(queryExecutor));
        accessHelpers.put(IStatisticAH.class, new DBStatisticAH(queryExecutor));
    }

    @Override
    public <T extends AH> T get(Class<T> base) {
        @SuppressWarnings({"unchecked"})
        T accessHelper = (T) accessHelpers.get(base);

        assert accessHelper != null : "Access helper is not registered: " + base.getSimpleName();

        return accessHelper;
    }

    @Override
    public void shutdown() throws StorageException {
        helper.shutdown();
    }

}
