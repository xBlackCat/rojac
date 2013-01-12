package org.xblackcat.schema.data;

import org.apache.commons.codec.binary.Base64;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

/**
 * 27.10.11 12:44
 *
 * @author xBlackCat
 */
public class DataConnection extends URLConnection {
    private final String mimeType;
    private final byte[] data;

    /**
     * Constructs a URL connection to the specified URL. A connection to
     * the object referenced by the URL is not created.
     *
     * @param url the specified URL.
     */
    protected DataConnection(URL url) {
        super(url);

        String path = url.getPath();
        if (path == null || path.length() == 0) {
            throw new IllegalArgumentException("Empty data");
        }

        String[] parts = path.split(";", 2);
        mimeType = parts[0];

        parts = parts[1].split(",", 2);
        data = Base64.decodeBase64(parts[1]);
    }

    @Override
    public void connect() throws IOException {

    }

    @Override
    public int getContentLength() {
        return data.length;
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return new ByteArrayInputStream(data);
    }

    @Override
    public String getContentType() {
        return mimeType;
    }
}
