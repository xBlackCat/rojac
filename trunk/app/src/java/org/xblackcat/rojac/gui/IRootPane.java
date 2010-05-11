package org.xblackcat.rojac.gui;

/**
 * @author xBlackCat
 */

public interface IRootPane {
    void openForumTab(int forumId);

    /**
     * Show edit dialog. Possible combinations are: <ul> <li>messageId is <code>null</code> and forumId specifies a
     * forum - create a new thread in the specified forum. <li>messageId specifies a message and forumId specifies a
     * forum - create an answer on specified message. <li>messageId specifies a message and forumId is <code>null</code>
     * - edit specified unposted message. </ul>
     *
     * @param forumId   forum id or <code>null</code>.
     * @param messageId message id or <code>null</code>.
     */
    void editMessage(Integer forumId, Integer messageId);

    /**
     * Opens a message by id
     *
     * @param messageId
     */
    void openMessage(int messageId, OpenMessageMethod method);
}
