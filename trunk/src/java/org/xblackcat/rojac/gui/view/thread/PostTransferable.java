package org.xblackcat.rojac.gui.view.thread;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xblackcat.rojac.gui.view.model.Post;
import org.xblackcat.rojac.util.LinkUtils;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 02.02.12 17:19
 *
 * @author xBlackCat
 */
class PostTransferable extends org.jdesktop.swingx.plaf.basic.core.BasicTransferable {
    private static final Log log = LogFactory.getLog(PostTransferable.class);

    private final Post[] posts;
    private final DataFlavor[] flavors;

    public PostTransferable(Post[] posts) {
        super(convertToPlain(posts), convertToHtml(posts));
        this.posts = posts;

        this.flavors = constructFlavours(
                "application/x-java-url; class=java.net.URL",
                "application/x-java-file-list; class=java.util.List",
                "text/uri-list; class=java.lang.String"
        );
    }

    private static DataFlavor[] constructFlavours(String... types) {
        Collection<DataFlavor> flavors = new ArrayList<>(types.length);

        for (String type : types) {
            try {
                flavors.add(new DataFlavor(type));
            } catch (ClassNotFoundException e) {
                log.warn("Can't initialize data flavor", e);
            }
        }

        if (flavors.isEmpty()) {
            return null;
        }

        return flavors.toArray(new DataFlavor[flavors.size()]);
    }

    private static String convertToHtml(Post[] posts) {
        StringBuilder str = new StringBuilder("<html>\n<body>\n<ul>\n");

        for (Post p : posts) {
            str.append("<li>\n");
            str.append(LinkUtils.buildMessageLink(p.getMessageId()));
            str.append("\n<\\li>\n");
        }

        return str.substring(1);
    }

    private static String convertToPlain(Post[] posts) {
        StringBuilder str = new StringBuilder();

        for (Post p : posts) {
            str.append("\n");
            str.append(LinkUtils.buildMessageLink(p.getMessageId()));
        }

        return str.toString();
    }

    @Override
    protected DataFlavor[] getRicherFlavors() {
        return flavors;
    }

    @Override
    protected Object getRicherData(DataFlavor flavor) throws UnsupportedFlavorException {
        try {
            if (flavor.getRepresentationClass() == URL.class) {
                return new URL(LinkUtils.buildMessageLink(posts[0].getMessageId()));
            } else if ("application/x-java-file-list; class=java.util.List".equals(flavor.getMimeType())) {
                return createFiles(posts);
            }
        } catch (MalformedURLException e) {
            // Ignore - pass as URI lists
        }

        return getPlainData();
    }

    private static List<File> createFiles(Post[] posts) {
        List<File> files = new ArrayList<>();

        // Get temporary folder
        try {
            File tempFolder = new File(System.getProperty("java.io.tmpdir"));

            if (tempFolder.isDirectory() && tempFolder.canWrite()) {
                for (Post p : posts) {
                    files.add(browserLinkFile(tempFolder, p));
                }
            }
        } catch (Exception e) {
            log.error("Can't create temporary files", e);
            return null;
        }

        return files;
    }

    private static File browserLinkFile(File tempFolder, Post p) throws IOException {
        File postFile = new File(tempFolder, prepareFileName(p));

        if (!postFile.exists()) {
            // Fill file content
            try (PrintWriter w = new PrintWriter(new BufferedWriter(new FileWriter(postFile)))) {
                w.println("[InternetShortcut]");
                w.println("URL=" + LinkUtils.buildMessageLink(p.getMessageId()));
            }
        }

        postFile.deleteOnExit();
        return postFile;
    }

    private static String prepareFileName(Post p) {
        return "#" + p.getMessageId() + " " + p.getMessageData().getSubject().replaceAll("[/\\?|:]+", "_") + ".url";
    }


}
