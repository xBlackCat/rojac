package org.xblackcat.rojac.service.commands;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xblackcat.rojac.RojacException;
import org.xblackcat.rojac.data.Version;
import org.xblackcat.rojac.data.VersionInfo;
import org.xblackcat.rojac.data.VersionType;
import org.xblackcat.rojac.gui.dialogs.progress.IProgressTracker;
import org.xblackcat.rojac.gui.dialogs.progress.ITask;
import org.xblackcat.rojac.service.RojacHelper;
import org.xblackcat.rojac.service.ServiceFactory;
import org.xblackcat.rojac.service.janus.IJanusService;
import org.xblackcat.rojac.service.janus.JanusService;
import org.xblackcat.rojac.service.options.Property;
import org.xblackcat.rojac.service.storage.IStorage;
import org.xblackcat.rojac.service.storage.IVersionAH;
import org.xblackcat.rojac.service.storage.StorageException;

/**
 * @author xBlackCat
 */

public abstract class ARsdnCommand implements ITask, ICommand {
    protected final Log log = LogFactory.getLog(getClass());
    private boolean executed = false;

    protected IJanusService janusService;
    protected final IStorage storage;
    private final IResultHandler resultHandler;

    protected ARsdnCommand(IResultHandler resultHandler) {
        this.resultHandler = resultHandler;

        ServiceFactory sf = ServiceFactory.getInstance();
        storage = sf.getStorage();
    }

    public void doTask(IProgressTracker trac) throws RojacException {
        synchronized (this) {
            if (!executed) {
                executed = true;
            } else {
                throw new RsdnProcessorException("Can not execute command twice: " + this.getClass().getSimpleName());
            }
        }

        JanusService c = new JanusService(Property.RSDN_USER_NAME.get(), RojacHelper.getUserPassword());
        c.init(Property.SYNCHRONIZER_USE_GZIP.get());
        c.testConnection();
        janusService = c;

        AffectedPosts result = process(trac);

        if (resultHandler != null) {
            resultHandler.process(result);
        }
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

    public abstract AffectedPosts process(IProgressTracker trac) throws RojacException;

    public void setJanusService(IJanusService janusService) {
        this.janusService = janusService;
    }
}