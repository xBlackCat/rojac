package org.xblackcat.rojac.service.janus.commands;

import org.xblackcat.rojac.gui.dialogs.LoginDialog;
import org.xblackcat.rojac.service.ServiceFactory;
import org.xblackcat.rojac.service.UserHelper;
import org.xblackcat.rojac.service.datahandler.ProcessPacket;
import org.xblackcat.rojac.util.RojacUtils;
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
    SYNCHRONIZE_WITH_USERS(new PostChangesRequest(), new GetUsersRequest(), new GetNewPostsRequest(), new LoadExtraMessagesRequest());

    private static final IResultHandler<?> defaultHandler = new ASwingThreadedHandler<ProcessPacket>() {
        @Override
        public void execute(ProcessPacket packet) {
            ServiceFactory.getInstance().getDataDispatcher().processPacket(packet);
        }
    };

    private final IRequest<?>[] requests;

    private Request(IRequest<?>... requests) {
        this.requests = requests;
    }

    /**
     * Process request(s) without user registration check and custom result handler.
     *
     * @param handler custom handler to process request(s) results.
     */
    public void process(IResultHandler<?> handler) {
        process(null, handler);
    }

    /**
     * Process request(s) with user registration check and default result handler.
     *
     * @param frame parent frame for Login dialog if it should be displayed.
     */
    public void process(Window frame) {
        process(frame, defaultHandler);
    }

    /**
     * Process request(s) without user registration check and default result handler.
     */
    public void process() {
        process(null, defaultHandler);
    }

    /**
     * Process request(s) with user registration check and custom result handler.
     *
     * @param frame parent frame for Login dialog if it should be displayed.
     * @param handler custom handler to process request(s) results.
     */
    public void process(Window frame, IResultHandler<?> handler) {
        RojacUtils.checkThread(true, Request.class);

        if (frame != null) {
            while (!UserHelper.isUserRegistered()) {
                LoginDialog ld = new LoginDialog(frame);
                WindowsUtils.center(ld, frame);
                if (ld.showLoginDialog()) {
                    return;
                }
            }
        }

        new RequestProcessor(handler, requests).execute();
    }

}
