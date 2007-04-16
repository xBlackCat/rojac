package org.xblackcat.sunaj.service.storage.filesystem;

import org.xblackcat.sunaj.service.data.Forum;
import org.xblackcat.sunaj.service.storage.IForumDAO;
import org.xblackcat.sunaj.service.storage.StorageException;
import org.xblackcat.sunaj.service.storage.filesystem.parsers.ForumParser;
import org.xblackcat.sunaj.service.storage.filesystem.writers.ForumWriter;

import java.io.File;

/**
 * Class for holding forum information.
 * Forum records has following file name format: <code>&lt;forum id&gt;_&lt;forum group id&gt;</code>
 * <p/>
 * Date: 16.04.2007
 *
 * @author ASUS
 */

class FileSystemForumDAO extends AbstractFileSystemDAO implements IForumDAO {
    FileSystemForumDAO(File root) {
        super(root, "forum");
    }

    public void storeForum(Forum f) throws StorageException {
        saveRecord(f, new ForumWriter());
    }

    public boolean removeForum(int id) throws StorageException {
        return removeRecord(id);
    }

    public Forum getForumById(int forumId) throws StorageException {
        return getRecord(forumId, new ForumParser());
    }

    public int[] getForumIdsInGroup(int forumGroupId) throws StorageException {
        return getPrimaryKeys("\\d+_" + forumGroupId); // Search all the records with given group id.
    }

    public int[] getAllForumIds() throws StorageException {
        return getAllIds(); // Search all the records in the storage.
    }

    protected String getPrimaryKeySearchPattern(int recordId) {
        return recordId + "_\\d+";
    }

    protected int extractPrimaryKey(String recordFileName) {
        int i = recordFileName.indexOf('_');
        try {
            return Integer.parseInt(recordFileName.substring(0, i));
        } catch (RuntimeException e) {
            return -1; // Got error
        }
    }
}
