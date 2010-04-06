package org.xblackcat.rojac.service.janus.commands;

import org.xblackcat.rojac.gui.dialogs.LoginDialog;
import org.xblackcat.rojac.service.RojacHelper;
import org.xblackcat.rojac.util.RojacWorker;
import org.xblackcat.rojac.util.WindowsUtils;

import java.awt.*;

/**
 * Utility class to help with constructing request to Janus service in one line.
 *
 * @author xBlackCat
 */

public enum Request {
    EXTRA_MESSAGES(new LoadExtraMessagesRequest()),
    GET_NEW_POSTS(new GetNewPostsRequest()),
    POST_CHANGES(new PostChangesRequest()),
    GET_USERS(new GetUsersRequest()),
    GET_FORUMS_LIST(new GetForumListRequest()),
    GET_USER_ID(new TestRequest()),
    SYNCHRONIZE(new PostChangesRequest(), new GetNewPostsRequest(), new LoadExtraMessagesRequest()),
    SYNCHRONIZE_WITH_USERS(new PostChangesRequest(), new GetUsersRequest(), new GetNewPostsRequest(), new LoadExtraMessagesRequest())
    ;

    private final IRequest[] requests;

    private Request(IRequest...requests) {
        this.requests = requests;
    }

    public void process(IResultHandler handler) {
        RojacWorker sw = new RequestProcessor(handler, requests);

        sw.execute();
    }

    public void process(Window frame, IResultHandler handler) {
        while (!RojacHelper.isUserRegistered()) {
            LoginDialog ld = new LoginDialog(frame);
            WindowsUtils.center(ld, frame);
            if (ld.showLoginDialog()) {
                return;
            }
        }

        this.process(handler);
    }
}
