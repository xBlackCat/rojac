package org.xblackcat.schema.data;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;

/**
 * 27.10.11 12:45
 *
 * @author xBlackCat
 */
public class DataStreamHandler extends URLStreamHandler {
    @Override
    protected URLConnection openConnection(URL u) throws IOException {
        return new DataConnection(u);
    }
}
