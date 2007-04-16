package org.xblackcat.sunaj.service.storage.filesystem.parsers;

import org.xblackcat.sunaj.service.storage.StorageParseException;

import java.io.File;

/**
 * Date: 16.04.2007
 *
 * @author ASUS
 */

public interface IObjectParser<T> {
    T parseObject(File f) throws StorageParseException;
}
