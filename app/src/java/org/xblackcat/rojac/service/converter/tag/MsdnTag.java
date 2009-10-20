package org.xblackcat.rojac.service.converter.tag;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xblackcat.rojac.service.converter.ITagData;
import org.xblackcat.rojac.service.converter.ITagInfo;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * @author xBlackCat
 */

public class MsdnTag extends SimpleTag {
    private static final Log log = LogFactory.getLog(MsdnTag.class);
    private final String openTextTagEnd;

    public MsdnTag() {
        super("msdn", "<a target='_blank' class='m' href='http://search.microsoft.com/search/results.aspx?View=msdn&amp;c=4&amp;qu=", "</a>");
        this.openTextTagEnd = "'>";
    }

    protected ITagInfo getTagInfo(final String text, final String lower, final int startPos) {
        return new MsdnTagInfo(startPos, lower, text);
    }

    protected class MsdnTagData extends SimpleTagData {
        public MsdnTagData(int startPos, int endPos, String text) {
            super(startPos, endPos, text);
        }

        public String getHead() {
            try {
                return openTextTag + URLEncoder.encode(getBody(), "UTF-8") + openTextTagEnd;
            } catch (UnsupportedEncodingException e) {
                if (log.isWarnEnabled()) {
                    log.warn("Something wrong with UTF-8 in text: " + getBody(), e);
                }
                return openTextTag + getBody() + openTextTagEnd;
            }
        }
    }

    protected class MsdnTagInfo extends SimpleTagInfo {
        public MsdnTagInfo(int startPos, String lower, String text) {
            super(startPos, lower, text);
        }

        public ITagData process() {
            final int endPos = lower.indexOf(closeTag, startPos);

            if (endPos == -1) {
                return null;
            }

            return new MsdnTagData(startPos, endPos, text);
        }
    }
}