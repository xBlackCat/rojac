package org.xblackcat.sunaj.service.storage.filesystem.parsers;

import org.xblackcat.sunaj.service.data.Forum;
import org.xblackcat.sunaj.service.storage.StorageParseException;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Date: 16.04.2007
 *
 * @author ASUS
 */

public class ForumParser implements IObjectParser<Forum> {
    public Forum parseObject(File f) throws StorageParseException {
        DataInputStream dis = null;
        try {
            dis = new DataInputStream(new FileInputStream(f));

            int forumId = dis.readInt();
            int forumGroupId = dis.readInt();
            int inTop = dis.readInt();
            int rated = dis.readInt();
            int rateLimit = dis.readInt();
            String shortForumName = dis.readUTF();
            String forumName = dis.readUTF();

            return new Forum(forumId, forumGroupId, inTop, rated, rateLimit, shortForumName, forumName);
        } catch (FileNotFoundException e) {
            throw new StorageParseException("File with the recird is not found: " + f.getName(), e);
        } catch (IOException e) {
            throw new StorageParseException("Can not parse the record: " + f.getName(), e);
        } finally {
            if (dis != null) {
                try {
                    dis.close();
                } catch (IOException e) {
                    // Ignore.
                }
            }
        }
    }
}
