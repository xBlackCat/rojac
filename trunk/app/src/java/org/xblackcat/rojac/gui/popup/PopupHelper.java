package org.xblackcat.rojac.gui.popup;

import org.apache.commons.lang.ArrayUtils;

/**
 * Date: 19 ρεπο 2009
 *
 * @author xBlackCat
 */

public final class PopupHelper {
    private PopupHelper() {
    }

    public static LinkParameters getLinkParameters(Object[] params) {
        if (ArrayUtils.isEmpty(params) || params.length != 3) {
            throw new IllegalArgumentException("Invalid amount of parameters for building LinkPopUp menu");
        }

        return new LinkParameters((String) params[0], (Integer) params[1], (String) params[2]);
    }

    public static class LinkParameters {
        private final String url;
        private final Integer messageId;
        private final String text;

        public LinkParameters(String url, Integer messageId, String text) {
            this.url = url;
            this.messageId = messageId;
            this.text = text;
        }

        public String getUrl() {
            return url;
        }

        public Integer getMessageId() {
            return messageId;
        }

        public String getText() {
            return text;
        }
    }
}
