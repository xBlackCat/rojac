package org.xblackcat.rojac.service.janus.commands;

import org.xblackcat.rojac.gui.dialog.LoginDialog;
import org.xblackcat.rojac.service.ServiceFactory;
import org.xblackcat.rojac.service.UserHelper;
import org.xblackcat.rojac.service.datahandler.IPacket;
import org.xblackcat.rojac.util.RojacUtils;
import org.xblackcat.rojac.util.WindowsUtils;

import java.awt.*;

/**
 * Utility class to help with constructing request to Janus service in one line.
 *
 * @author xBlackCat
 */

@SuppressWarnings({"unchecked"})
public class Request<T> {
    private static final IResultHandler<IPacket> PACKET_HANDLER = new ASwingThreadedHandler<IPacket>() {
        @Override
        public void execute(IPacket packet) {
            ServiceFactory.getInstance().getDataDispatcher().processPacket(packet);
        }
    };
    /**
     * Common handler to prevent invoking requests without associated handler.
     */
    private static final IResultHandler NO_HANDLER = new IResultHandler() {
        @Override
        public void process(Object data) {
            throw new RuntimeException("No result handler defined for the request!");
        }
    };

    public static final Request<IPacket> EXTRA_MESSAGES = new Request<IPacket>(PACKET_HANDLER, new LoadExtraMessagesRequest());
    public static final Request<IPacket> GET_NEW_POSTS = new Request<IPacket>(PACKET_HANDLER, new GetNewPostsRequest());
    public static final Request<IPacket> POST_CHANGES = new Request<IPacket>(PACKET_HANDLER, new PostChangesRequest());
    public static final Request<IPacket> GET_USERS = new Request<IPacket>(PACKET_HANDLER, new GetUsersRequest());
    public static final Request<IPacket> GET_FORUMS_LIST = new Request<IPacket>(PACKET_HANDLER, new GetForumListRequest());
    public static final Request<Integer> GET_USER_ID = new Request<Integer>(NO_HANDLER, new TestRequest());
    public static final Request<IPacket> SYNCHRONIZE = new Request<IPacket>(PACKET_HANDLER, new PostChangesRequest(), new GetNewPostsRequest(), new LoadExtraMessagesRequest());
    public static final Request<IPacket> SYNCHRONIZE_WITH_USERS = new Request<IPacket>(PACKET_HANDLER, new PostChangesRequest(), new GetUsersRequest(), new GetNewPostsRequest(), new LoadExtraMessagesRequest());

    private final IResultHandler<T> defaultHandler;
    private final IRequest<T>[] requests;

    private Request(IResultHandler<T> defaultHandler, IRequest<T>... requests) {
        this.defaultHandler = defaultHandler;
        this.requests = requests;
    }

    /**
     * Process request(s) without user registration check and custom result handler.
     *
     * @param handler custom handler to process request(s) results.
     */
    public void process(IResultHandler<T> handler) {
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
        process((Window) null);
    }

    /**
     * Process request(s) with user registration check and custom result handler.
     *
     * @param frame   parent frame for Login dialog if it should be displayed.
     * @param handler custom handler to process request(s) results.
     */
    public void process(Window frame, IResultHandler<T> handler) {
        assert RojacUtils.checkThread(true, Request.class);

        if (frame != null) {
            while (!UserHelper.isUserRegistered()) {
                LoginDialog ld = new LoginDialog(frame);
                WindowsUtils.center(ld, frame);
                if (ld.showLoginDialog()) {
                    return;
                }
            }
        }

        new RequestProcessor<T>(handler, requests).execute();
    }

}
