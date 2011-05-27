package org.xblackcat.bbcode;

import java.io.IOException;
import java.io.Reader;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * @author xBlackCat
 */
public class BBDomParser {
    public BBTag parse(Reader r) throws IOException {
        List<Part> parts = splitByParts(r);

        return new BBTag();
    }

    List<Part> splitByParts(Reader r) throws IOException {
        List<Part> result = new LinkedList<Part>();
        StringBuilder buf = new StringBuilder();

        boolean braceOpen = false;
        boolean quoteOpen = false;

        int v;
        while ((v = r.read()) != -1) {
            char c = (char) v;

            if (c == '[') {
                if (!braceOpen) {
                    if (buf.length() > 0) {
                        result.add(new Part(buf.toString()));
                        buf.setLength(0);
                    }
                    braceOpen = true;
                }
                buf.append(c);
            } else if (c == '"' && braceOpen) {
                quoteOpen = !quoteOpen;
                buf.append(c);
            } else if (c == ']' && braceOpen && !quoteOpen) {
                buf.append(c);
                if (buf.length() > 0) {
                    result.add(new Part(buf.toString()));
                    buf.setLength(0);
                }
                braceOpen = false;
            } else {
                buf.append(c);
            }
        }

        if (buf.length() > 0) {
            result.add(new Part(buf.toString()));
            buf.setLength(0);
        }

        return result;
    }

    List<Part> splitByPartsI(Reader r) {
        List<Part> result = new LinkedList<Part>();

        Iterator<Part> i = new SplitIterator(r);

        while (i.hasNext()) {
            result.add(i.next());
        }

        return result;
    }

    private class SplitIterator implements Iterator<Part> {
        private StringBuilder buf = new StringBuilder();

        private boolean braceOpen = false;
        private boolean quoteOpen = false;

        private final Reader source;

        private Part nextPart;

        public SplitIterator(Reader source) {
            this.source = source;
        }


        @Override
        public boolean hasNext() {
            try {
                nextPart = readNextPart();
            } catch (IOException e) {
                return false;
            }

            return nextPart != null;
        }

        @Override
        public Part next() {
            return nextPart;
        }

        @Override
        public void remove() {
        }

        private Part readNextPart() throws IOException {
            Part result = null;

            int v;
            while ((v = source.read()) != -1) {
                char c = (char) v;

                if (c == '[') {
                    if (!braceOpen) {
                        if (buf.length() > 0) {
                            result = new Part(buf.toString());
                            buf.setLength(0);
                        }
                        braceOpen = true;
                    }
                    buf.append(c);
                } else if (c == '"' && braceOpen) {
                    quoteOpen = !quoteOpen;
                    buf.append(c);
                } else if (c == ']' && braceOpen && !quoteOpen) {
                    buf.append(c);
                    if (buf.length() > 0) {
                        result = new Part(buf.toString());
                        buf.setLength(0);
                    }
                    braceOpen = false;
                } else {
                    buf.append(c);
                }

                if (result != null) {
                    return result;
                }
            }

            if (buf.length() > 0) {
                result = new Part(buf.toString());
                buf.setLength(0);
            }

            return result;
        }
    }
}
