package org.xblackcat.rojac.service.synchronizer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xblackcat.rojac.data.Version;
import org.xblackcat.rojac.data.VersionInfo;
import org.xblackcat.rojac.data.VersionType;
import org.xblackcat.rojac.gui.frame.progress.ITask;
import org.xblackcat.rojac.service.ServiceFactory;
import org.xblackcat.rojac.service.janus.IJanusService;
import org.xblackcat.rojac.service.janus.JanusService;
import org.xblackcat.rojac.service.janus.JanusServiceException;
import org.xblackcat.rojac.service.options.IOptionsService;
import org.xblackcat.rojac.service.options.Password;
import org.xblackcat.rojac.service.options.Property;
import org.xblackcat.rojac.service.storage.IStorage;
import org.xblackcat.rojac.service.storage.IVersionAH;
import org.xblackcat.rojac.service.storage.StorageException;

/**
 * Date: 14 вер 2008
 *
 * @author xBlackCat
 */

public abstract class ARsdnCommand implements ITask, ICommand {
    private static final Log log = LogFactory.getLog(ARsdnCommand.class);

    protected IJanusService janusService;
    protected final IStorage storage;
    protected final IOptionsService optionsService;

    protected ARsdnCommand() {
        ServiceFactory sf = ServiceFactory.getInstance();
        storage = sf.getStorage();
        optionsService = sf.getOptionsService();
    }

    public void prepareTask() throws JanusServiceException {
        String username = optionsService.getProperty(Property.RSDN_USER_NAME);
        Password password = optionsService.getProperty(Property.RSDN_USER_PASSWORD);

        JanusService c = new JanusService(username, password.toString());
        c.init(optionsService.getProperty(Property.SERVICE_JANUS_USE_GZIP));
        c.testConnection();
        janusService = c;
    }

    protected Version getVersion(VersionType type) throws StorageException {
        IVersionAH vAH = storage.getVersionAH();
        VersionInfo versionInfo = vAH.getVersionInfo(type);
        return versionInfo == null ? new Version() : versionInfo.getVersion();
    }

    protected void setVersion(VersionType type, Version v) throws StorageException {
        IVersionAH vAH = storage.getVersionAH();
        vAH.updateVersionInfo(new VersionInfo(v, type));
    }
}
