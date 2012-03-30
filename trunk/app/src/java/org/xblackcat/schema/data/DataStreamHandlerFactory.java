package org.xblackcat.schema.data;

import java.net.URLStreamHandler;
import java.net.URLStreamHandlerFactory;

/**
 * 27.10.11 12:46
 *
 * @author xBlackCat
 */
public class DataStreamHandlerFactory implements URLStreamHandlerFactory {
    @Override
    public URLStreamHandler createURLStreamHandler(String protocol) {
        if ("data".equals(protocol)) {
            return new DataStreamHandler();
        } else {
            return null;
        }
    }
}
