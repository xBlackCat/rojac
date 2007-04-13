package org.xblackcat.sunaj;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.lang.ArrayUtils;
import org.xblackcat.sunaj.service.soap.IJanusConnector;
import org.xblackcat.sunaj.service.soap.JanusConnector;
import org.xblackcat.sunaj.service.soap.data.UsersList;

/**
 * Date: 26 бер 2007
 *
 * @author Alexey
 */

public class SunajLauncher {
	private static final Log log = LogFactory.getLog(SunajLauncher.class);

	private SunajLauncher() {
	}

	public static void main(String[] args) throws Exception {
		IJanusConnector con = JanusConnector.getInstance("xBlackCat", "tryt0guess");

		if (log.isInfoEnabled()) {
			log.info("\nInitialized\n================================================================================");
		}

		con.testConnection();

		byte[] verRow = ArrayUtils.EMPTY_BYTE_ARRAY;
		for (int i = 0; i < 5; i++) {
			UsersList users = con.getNewUsers(verRow);
			if (log.isInfoEnabled()) {
				log.info("Version: " + ArrayUtils.toString(users.getVersion()));
				log.info("New users: " + ArrayUtils.toString(users.getUsers()));
			}
			verRow = users.getVersion();
		}
	}

}
