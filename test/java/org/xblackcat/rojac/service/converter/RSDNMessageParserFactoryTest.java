package org.xblackcat.rojac.service.converter;

import junit.framework.TestCase;
import org.junit.Assert;

/**
 * @author xBlackCat
 */


public class RSDNMessageParserFactoryTest extends TestCase {
    public void testLoadingTags() throws Exception {
        RSDNMessageParserFactory factory = new RSDNMessageParserFactory(
                "/message/tags/taglist.loading.properties",
                "/message/tags/tagrules.loading.properties",
                null
        );

        Assert.assertEquals(4, ((RSDNMessageParser) factory.getParser()).getAvailableSubTags().get(null).length);
    }
}