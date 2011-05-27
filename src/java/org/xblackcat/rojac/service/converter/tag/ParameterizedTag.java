package org.xblackcat.rojac.service.converter.tag;

import org.apache.commons.lang.StringUtils;
import org.xblackcat.rojac.service.converter.ITagData;
import org.xblackcat.rojac.service.converter.ITagInfo;

/**
 * @author xBlackCat
 */

public class ParameterizedTag extends SimpleTag {
    private final String openTagEnd;
    private final String openTextTagEnd;

    protected ParameterizedTag(String openTag, String openTagEnd, String closeTag, String openTextTag, String openTextTagEnd, String closeTextTag) {
        super(openTag, closeTag, openTextTag, closeTextTag);
        this.openTagEnd = openTagEnd.toLowerCase();
        this.openTextTagEnd = openTextTagEnd;
    }

    protected ITagInfo getTagInfo(final String text, final String lower, final int startPos) {
        return new ParameterizedTagInfo(startPos, lower, text);
    }

    protected class ParameterizedTagData extends SimpleTagData {
        private final int openEndPos;

        public ParameterizedTagData(int startPos, int endPos, String text, int openEndPos) {
            super(startPos, endPos, text);
            this.openEndPos = openEndPos;
        }

        public String getHead() {
            String parameter = text.substring(startPos + openTag.length(), openEndPos);
            if (StringUtils.isBlank(parameter)) {
                parameter = getBody();
            }
            return openTextTag + parameter + openTextTagEnd;
        }

        public String getBody() {
            return text.substring(openEndPos + openTagEnd.length(), endPos);
        }
    }

    protected class ParameterizedTagInfo extends SimpleTagInfo {
        public ParameterizedTagInfo(int startPos, String lower, String text) {
            super(startPos, lower, text);
        }

        public ITagData process() {
            final int openEndPos = lower.indexOf(openTagEnd, startPos);

            if (openEndPos == -1) {
                return null;
            }
            final int endPos = lower.indexOf(closeTag, openEndPos);

            if (endPos == -1) {
                return null;
            }

            return new ParameterizedTagData(startPos, endPos, text, openEndPos);
        }
    }
}