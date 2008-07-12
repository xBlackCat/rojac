package org.xblackcat.sunaj.service.options;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;

/**
 * Date: 12 лип 2008
 *
 * @author xBlackCat
 */

public class PasswordConverter implements IConverter<Password> {

    public Password convert(String s) {
        if (StringUtils.isEmpty(s)) {
            return null;
        }
        return new Password(new String(Base64.decodeBase64(s.getBytes())).toCharArray());
    }

    public String toString(Password o) {
        if (o == null) {
            return "";
        }
        return new String(Base64.encodeBase64(new String(o.getPassword()).getBytes()));
    }
}
