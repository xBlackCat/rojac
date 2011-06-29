package org.xblackcat.rojac.i18n;

import junit.framework.TestCase;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xblackcat.rojac.service.options.Property;
import org.xblackcat.rojac.util.RojacUtils;

import java.io.IOException;
import java.util.*;

/**
 * @author xBlackCat Date: 23.06.11
 */
public class NodeTextTest extends TestCase {
    private static final Log log = LogFactory.getLog(MessageTest.class);

    private ANode[] nodeList;

    @Override
    public void setUp() throws Exception {
        Set<String> nodes = new HashSet<String>();

        for (Property p : Property.getAllProperties()) {
            if (p.isPublic()) {
                String[] parts = p.getName().split("\\.");
                StringBuilder name = new StringBuilder();

                for (String part : parts) {
                    name.append('.');
                    name.append(part);

                    nodes.add(name.substring(1));
                }
            }
        }

        String[] list = nodes.toArray(new String[nodes.size()]);
        Arrays.sort(list);

        nodeList = new ANode[list.length];

        for (int i = 0, listLength = list.length; i < listLength; i++) {
            nodeList[i] = new FakeNode(list[i]);
        }
    }

    public void testNamesForExistence() throws IOException {
        internalTestForExistence(NodeText.Name);
    }

    public void testTipsForExistence() throws IOException {
        internalTestForExistence(NodeText.Tip);
    }

    private void internalTestForExistence(NodeText type) throws IOException {
        // Test default locale first.
        if (log.isInfoEnabled()) {
            log.info("Test resources for default locale.");
        }
        assertTrue("Some translations is missed.", localeTest(null, type));

        // List of locales
        Collection<Locale> locales = Arrays.asList(RojacUtils.localesForBundle("i18n/" + type.getBundleName(), false));

        if (log.isInfoEnabled()) {
            log.info("Found " + locales.size() + " locales to test: " + locales);
        }

        boolean noErrors = true;
        for (Locale l : locales) {
            if (log.isInfoEnabled()) {
                log.info("Test resources for locale " + l);
            }
            try {
                noErrors &= localeTest(l, type);
            } catch (IllegalArgumentException e) {
                log.error("Can not set locale " + l, e);
                noErrors = false;
            }
        }

        assertTrue("Some translations is missed.", noErrors);
    }

    private boolean localeTest(Locale l, NodeText type) {
        boolean noErrors = true;
        LocaleControl.getInstance().setLocale(l, true);

        for (ANode node : nodeList) {
            if (log.isDebugEnabled()) {
                log.debug("Check resource " + node.key()  + " [" + type + "]");
            }
            try {
                if (type.get(node) == null) {
                    log.error("There is no translation for " + node.key() + " [" + type + "]");
                    noErrors = false;
                }
            } catch (MissingResourceException e) {
                log.error("There is no translation for " + node.key() + " [" + type + "]");
                noErrors = false;
            }
        }

        return noErrors;
    }

    private static class FakeNode extends ANode {
        private final String key;

        public FakeNode(String key) {
            this.key = key;
        }

        @Override
        protected String key() {
            return key;
        }
    }
}
