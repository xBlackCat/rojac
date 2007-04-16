package org.xblackcat.sunaj.service.storage.filesystem;

import junit.framework.TestCase;
import org.xblackcat.sunaj.service.data.Forum;
import org.xblackcat.sunaj.service.storage.StorageException;

import java.io.File;

/**
 * Date: 16.04.2007
 *
 * @author ASUS
 */

public class FileSystemForumDAOTest extends TestCase {
    public void testPrimaryKeyExtraction() {
        FileSystemForumDAO dao = new FileSystemForumDAO(null);

        assertEquals(403, dao.extractPrimaryKey("403_2"));
        assertEquals(0, dao.extractPrimaryKey("0_3"));

        assertEquals(-1, dao.extractPrimaryKey("403"));
        assertEquals(-1, dao.extractPrimaryKey("403we_324"));
        assertEquals(-1, dao.extractPrimaryKey("fsad_fds"));
    }

    public void testDAO() throws StorageException {
        FileSystemForumDAO dao = new FileSystemForumDAO(new File("P:\\temp\\sunaj"));

        if (!dao.checkStructure()) {
            dao.initialize();
        }

        dao.storeForum(new Forum(1, 1, 0, 0, 0, "Test1", "Test 1 forum"));
        dao.storeForum(new Forum(2, 1, 0, 0, 0, "Test2", "Test 2 forum"));
        dao.storeForum(new Forum(3, 2, 0, 0, 0, "Test3", "Test 3 forum"));

        Forum f1 = dao.getForumById(1);
        Forum f2 = dao.getForumById(2);
        Forum f3 = dao.getForumById(3);

        int[] inGroup1 = dao.getForumIdsInGroup(1);
        int[] inGroup2 = dao.getForumIdsInGroup(2);

        int[] allForumIds = dao.getAllForumIds();

        // TODO: add assertions

        assertEquals(true, dao.removeForum(1));
        assertEquals(true, dao.removeForum(2));
        assertEquals(true, dao.removeForum(3));
        
        assertEquals(false, dao.removeForum(1));
        assertEquals(false, dao.removeForum(2));
        assertEquals(false, dao.removeForum(3));
    }
}
