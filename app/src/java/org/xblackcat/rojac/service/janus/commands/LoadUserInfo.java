package org.xblackcat.rojac.service.janus.commands;

import org.xblackcat.rojac.RojacException;
import org.xblackcat.rojac.data.User;
import org.xblackcat.rojac.i18n.Message;
import org.xblackcat.rojac.service.datahandler.IPacket;
import org.xblackcat.rojac.service.janus.IJanusService;
import org.xblackcat.rojac.service.janus.data.UsersList;
import org.xblackcat.rojac.service.storage.IUserAH;
import org.xblackcat.rojac.service.storage.Storage;

import java.util.Arrays;

/**
 * 16.08.12 16:43
 *
 * @author xBlackCat
 */
public class LoadUserInfo extends ARequest<IPacket> {
    @Override
    public void process(
            IResultHandler<IPacket> handler,
            ILogTracker tracker,
            IJanusService janusService
    ) throws RojacException {
        int[] portionIds = new int[]{};

        tracker.addLodMessage(Message.Synchronize_Command_Name_Users, Arrays.toString(portionIds));

        UsersList usersByIds = janusService.getUsersByIds(portionIds);

        tracker.addLodMessage(Message.Synchronize_Message_StoreFullUserInfo);
        User[] users = usersByIds.getUsers();

        if (users.length > 0) {
            User user = users[0];
            Storage.get(IUserAH.class).storeUser(user);
            tracker.updateProgress(1, 1);
        }
    }
}
