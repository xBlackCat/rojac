package org.xblackcat.rojac.service.options.converter;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.xblackcat.rojac.service.options.Password;

/**
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
