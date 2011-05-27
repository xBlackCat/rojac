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
    public static final IResultHandler<IPacket> PACKET_HANDLER = new ASwingThreadedHandler<IPacket>() {
        @Override
        public void execute(IPacket packet) {
            ServiceFactory.getInstance().getDataDispatcher().processPacket(packet);
        }
    };

    public static final Request<IPacket> EXTRA_MESSAGES = new Request<IPacket>(LoadExtraMessagesRequest.class);
    public static final Request<IPacket> GET_FORUMS_LIST = new Request<IPacket>(GetForumListRequest.class);
    public static final Request<Integer> GET_USER_ID = new Request<Integer>(TestRequest.class);
    public static final Request<IPacket> SYNCHRONIZE = new Request<IPacket>(PostChangesRequest.class, GetNewPostsRequest.class);
    public static final Request<IPacket> SYNCHRONIZE_WITH_USERS = new Request<IPacket>(PostChangesRequest.class, GetUsersRequest.class, GetNewPostsRequest.class);

    private final Class<? extends IRequest<T>>[] requests;

    private Request(Class<? extends IRequest<T>>... requests) {
        this.requests = requests;
    }

    /**
     * Process request(s) without user registration check and custom result handler.
     *
     * @param handler custom handler to process request(s) results.
     */
    public void process(IResultHandler<T>... handler) {
        process(null, handler);
    }

    /**
     * Process request(s) with user registration check and default result handler.
     *
     * @param frame parent frame for Login dialog if it should be displayed.
     */
    public void process(Window frame) {
        process(frame, new IResultHandler[]{PACKET_HANDLER});
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
     * @param frame    parent frame for Login dialog if it should be displayed.
     * @param handlers custom handler to process request(s) results.
     */
    public void process(Window frame, final IResultHandler<T>... handlers) {
        assert RojacUtils.checkThread(true);

        if (frame != null) {
            while (!UserHelper.isUserRegistered()) {
                LoginDialog ld = new LoginDialog(frame);
                WindowsUtils.center(ld, frame);
                if (ld.showLoginDialog()) {
                    return;
                }
            }
        }

        new RequestProcessor<T>(new IResultHandler<T>() {
            @Override
            public void process(T data) {
                for (IResultHandler<T> h : handlers) {
                    h.process(data);
                }
            }
        }, requests).execute();
    }
}
