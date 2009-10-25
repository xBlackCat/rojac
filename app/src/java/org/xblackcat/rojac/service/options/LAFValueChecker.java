package org.xblackcat.rojac.service.options;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xblackcat.utils.ResourceUtils;

import javax.swing.*;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author xBlackCat
 */

public class LAFValueChecker implements IValueChecker<LookAndFeel> {
    private static final Log log = LogFactory.getLog(LAFValueChecker.class);

    private static final String LAF_LIST = "/laf.config";

    private static final Pattern KEY_PATTERN = Pattern.compile("rojac\\.laf\\.(\\w+)\\.(class|description)");

    private final Map<LookAndFeel, String> availableLAFs;

    public LAFValueChecker() {
        Properties lafList;
        try {
            lafList = ResourceUtils.loadProperties(LAF_LIST);
        } catch (IOException e) {
            throw new RuntimeException("Can not load list of available L&Fs", e);
        }

        Map<String, String> classes = new HashMap<String, String>();
        Map<String, String> descriptions = new HashMap<String, String>();
        for (String key : lafList.stringPropertyNames()) {
            Matcher m = KEY_PATTERN.matcher(key);
            if (m.matches()) {
                String value = lafList.getProperty(key);
                String name = m.group(1);

                if ("class".equals(m.group(2))) {
                    if (log.isTraceEnabled()) {
                        log.trace("Found L&F class " + name + " [" + value + "]");
                    }
                    classes.put(name, value);
                } else if ("description".equals(m.group(2))) {
                    if (log.isTraceEnabled()) {
                        log.trace("Found L&F description " + name + " [" + value + "]");
                    }
                    descriptions.put(name, value);
                } else {
                    log.warn("Invalid key: " + key);
                }
            }
        }

        Map<LookAndFeel, String> lafs = new HashMap<LookAndFeel, String>();
        for (Map.Entry<String, String> e : classes.entrySet()) {
            LookAndFeel lafClass;
            try {
                lafClass = (LookAndFeel) ResourceUtils.loadObjectOrEnum(e.getValue());
            } catch (ClassNotFoundException e1) {
                log.warn("Can not find class of LAF", e1);
                continue;
            } catch (IllegalAccessException e1) {
                log.warn("Can not initialize L&F class", e1);
                continue;
            } catch (InstantiationException e1) {
                log.warn("Can not initialize L&F class", e1);
                continue;
            }

            if (!lafClass.isSupportedLookAndFeel()) {
                log.warn("L&F " + e.getKey() + " (" + e.getValue() + ") is not supported.");
            }

            String description = MapUtils.getString(descriptions, e.getKey(), lafClass.getName() + ": " + lafClass.getDescription());

            lafs.put(lafClass, description);
        }

        this.availableLAFs = Collections.unmodifiableMap(lafs);
    }

    @Override
    public LookAndFeel[] getPossibleValues() {
        Collection<LookAndFeel> lafClasses = availableLAFs.keySet();

        return lafClasses.toArray(new LookAndFeel[lafClasses.size()]);
    }

    @Override
    public String getValueDescription(LookAndFeel v) throws IllegalArgumentException {
        if (!isValueCorrect(v)) {
            throw new IllegalArgumentException("L&F " + v.getName() + " is not supported.");
        }
        return availableLAFs.get(v);
    }

    @Override
    public boolean isValueCorrect(LookAndFeel v) {
        return availableLAFs.containsKey(v);
    }
}
