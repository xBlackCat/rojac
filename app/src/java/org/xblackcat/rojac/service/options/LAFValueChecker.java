package org.xblackcat.rojac.service.options;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xblackcat.utils.ResourceUtils;

import javax.swing.*;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author xBlackCat
 */

final class LAFValueChecker implements IValueChecker<LookAndFeel> {
    private static final Log log = LogFactory.getLog(LAFValueChecker.class);

    private static final String LAF_LIST = "/laf.properties";

    private static final Pattern KEY_PATTERN = Pattern.compile("rojac\\.laf\\.(\\w+)\\.(class|description)");
    private static final Comparator<LookAndFeel> LOOK_AND_FEEL_COMPARATOR = new Comparator<LookAndFeel>() {
        @Override
        public int compare(LookAndFeel o1, LookAndFeel o2) {
            return o1.getName().compareTo(o2.getName());
        }
    };

    private final Map<LNFContainer, String> availableLAFs;

    LAFValueChecker() {
        Properties lafList;
        try {
            lafList = ResourceUtils.loadProperties(LAF_LIST);
        } catch (IOException e) {
            throw new RuntimeException("Can not load list of available L&Fs", e);
        }

        for (String key : lafList.stringPropertyNames()) {
            Matcher m = KEY_PATTERN.matcher(key);
            if (m.matches()) {
                String value = lafList.getProperty(key);
                String name = m.group(1);

                if ("class".equals(m.group(2))) {
                    if (log.isTraceEnabled()) {
                        log.trace("Install L&F class " + name + " [" + value + "]");
                    }

                    UIManager.installLookAndFeel(name, value);
                } else {
                    log.warn("Invalid key: " + key);
                }
            }
        }

        Map<LNFContainer, String> lafs = new HashMap<>();
        for (UIManager.LookAndFeelInfo laf : UIManager.getInstalledLookAndFeels()) {
            if (log.isTraceEnabled()) {
                log.trace("Load " + laf.toString());
            }

            LookAndFeel lafClass;
            try {
                lafClass = (LookAndFeel) ResourceUtils.loadObjectOrEnum(laf.getClassName());
            } catch (ClassNotFoundException e1) {
                log.warn("Can not find class of LAF", e1);
                continue;
            } catch (IllegalAccessException | InstantiationException e1) {
                log.warn("Can not initialize L&F class", e1);
                continue;
            }

            if (!lafClass.isSupportedLookAndFeel()) {
                log.warn("L&F " + laf.getName() + " (" + laf.getClassName() + ") is not supported.");
            }

            lafs.put(new LNFContainer(lafClass), lafClass.getDescription());
        }

        this.availableLAFs = Collections.unmodifiableMap(lafs);

    }

    @Override
    public List<LookAndFeel> getPossibleValues() {
        Collection<LNFContainer> lafs = availableLAFs.keySet();

        List<LookAndFeel> lafClasses = new ArrayList<>(lafs.size());

        for (LNFContainer c : lafs) {
            lafClasses.add(c.getLnf());
        }

        Collections.sort(lafClasses, LOOK_AND_FEEL_COMPARATOR);

        return lafClasses;
    }

    @Override
    public String getValueDescription(LookAndFeel v) throws IllegalArgumentException {
        if (!isValueCorrect(v)) {
            throw new IllegalArgumentException("L&F " + v.getName() + " is not supported.");
        }
        return availableLAFs.get(new LNFContainer(v));
    }

    @Override
    public boolean isValueCorrect(LookAndFeel v) {
        return availableLAFs.containsKey(new LNFContainer(v));
    }

    @Override
    public Icon getValueIcon(LookAndFeel v) throws IllegalArgumentException {
        return null;
    }

    private final class LNFContainer {
        private final LookAndFeel lnf;

        LNFContainer(LookAndFeel lnf) {
            this.lnf = lnf;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }

            LNFContainer that = (LNFContainer) o;

            return lnf.getName().equals(that.lnf.getName());
        }

        @Override
        public int hashCode() {
            return lnf.getName().hashCode();
        }

        public LookAndFeel getLnf() {
            return lnf;
        }
    }
}
