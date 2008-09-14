package org.xblackcat.rojac.service.synchronizer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xblackcat.rojac.gui.frame.progress.ITask;
import org.xblackcat.rojac.service.ServiceFactory;
import org.xblackcat.rojac.service.janus.IJanusService;
import org.xblackcat.rojac.service.janus.JanusService;
import org.xblackcat.rojac.service.janus.JanusServiceException;
import org.xblackcat.rojac.service.options.IOptionsService;
import org.xblackcat.rojac.service.options.Password;
import org.xblackcat.rojac.service.options.Property;
import org.xblackcat.rojac.service.storage.IStorage;

/**
 * Date: 14 вер 2008
 *
 * @author xBlackCat
 */

public abstract class ARsdnCommand implements ITask, ICommand {
    private static final Log log = LogFactory.getLog(ARsdnCommand.class);

    protected IJanusService janusService;
    protected IStorage storage;

    public void prepareTask() throws JanusServiceException {
        ServiceFactory sf = ServiceFactory.getInstance();
        storage = sf.getStorage();

        IOptionsService os = sf.getOptionsService();

        String username = os.getProperty(Property.RSDN_USER_NAME);
        Password password = os.getProperty(Property.RSDN_USER_PASSWORD);

        JanusService c = new JanusService(username, password.toString());
        c.init();
        c.testConnection();
        janusService = c;
    }
}
