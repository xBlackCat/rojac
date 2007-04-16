package org.xblackcat.sunaj.service.storage.filesystem;

import java.io.File;
import java.io.FilenameFilter;
import java.util.regex.Pattern;

/**
 * Date: 16.04.2007
*
* @author ASUS
*/
class PatternFilenameFilter implements FilenameFilter {

    private final Pattern p;

    PatternFilenameFilter(String pattern) {
        p = Pattern.compile(pattern);
    }

    public boolean accept(File dir, String name) {
        return p.matcher(name).matches();
    }
}
