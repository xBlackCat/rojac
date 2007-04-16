package org.xblackcat.sunaj.service.storage.filesystem.writers;

import org.xblackcat.sunaj.service.data.Forum;
import org.xblackcat.sunaj.service.storage.StorageWriteException;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Date: 16.04.2007
 *
 * @author ASUS
 */

public class ForumWriter implements IObjectWriter<Forum> {
    public String writeObject(File root, Forum o) throws StorageWriteException {
        String fileName = new StringBuilder().append(o.getForumId()).append('_').append(o.getForumGroupId()).toString();

        File recordFile = new File(root, fileName);

        DataOutputStream dos = null;
        try {
            dos = new DataOutputStream(new FileOutputStream(recordFile));

            dos.writeInt(o.getForumId());
            dos.writeInt(o.getForumGroupId());
            dos.writeInt(o.getInTop());
            dos.writeInt(o.getRated());
            dos.writeInt(o.getRateLimit());
            dos.writeUTF(o.getShortForumName());
            dos.writeUTF(o.getForumName());

            return fileName;
        } catch (IOException e) {
            throw new StorageWriteException("Can not store the record " + o, e);
        } finally {
            if (dos != null) {
                try {
                    dos.close();
                } catch (IOException e) {
                    // Ignore.
                }
            }
        }
    }
}
