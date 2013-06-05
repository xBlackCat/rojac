package org.xblackcat.rojac.service.storage.database.helper;

import org.xblackcat.rojac.data.IIdentifable;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * 08.04.12 21:03
 *
 * @author xBlackCat
 */
public class DBUtils {
    static void fillStatement(PreparedStatement preparedStatement, Object... parameters) throws SQLException {
        // Fill parameters if any
        if (parameters != null) {
            for (int i = 0; i < parameters.length; i++) {
                if (parameters[i] instanceof IIdentifable) {
                    preparedStatement.setInt(i + 1, ((IIdentifable) (parameters[i])).getId());
                } else if (parameters[i] instanceof Boolean) {
                    preparedStatement.setInt(i + 1, ((Boolean) (parameters[i])) ? 1 : 0);
                } else {
                    preparedStatement.setObject(i + 1, parameters[i]);
                }
            }
        }
    }
}
