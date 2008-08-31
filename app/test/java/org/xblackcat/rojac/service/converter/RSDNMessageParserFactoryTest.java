package org.xblackcat.rojac.service.converter;
/**
 * Date: 20 лют 2008
 * @author xBlackCat
 */


import junit.framework.Assert;
import junit.framework.TestCase;

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