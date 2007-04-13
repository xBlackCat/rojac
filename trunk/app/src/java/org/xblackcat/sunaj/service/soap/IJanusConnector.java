package org.xblackcat.sunaj.service.soap;

import org.xblackcat.sunaj.service.soap.data.ForumsList;
import org.xblackcat.sunaj.service.soap.data.UsersList;

/**
 * Date: 10 квіт 2007
 *
 * @author Alexey
 */

public interface IJanusConnector {
	/**
	 * Tests a connection with Janus WS.
	 *
	 * @throws JanusServiceException throws if connection can not be established.
	 */
	void testConnection() throws JanusServiceException;

	/**
	 * Retrieves a forums list from Janus WS.
	 *
	 * @return RSDN forums list.
	 *
	 * @throws org.xblackcat.sunaj.service.soap.JanusServiceException throws if any errors occurs.
	 */
	ForumsList getForumsList() throws JanusServiceException;

	UsersList getNewUsers(byte[] verRow) throws JanusServiceException;
}
