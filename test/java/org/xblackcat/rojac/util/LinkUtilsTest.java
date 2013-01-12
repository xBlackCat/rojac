package org.xblackcat.rojac.util;
/**
 *
 * @author xBlackCat
 */


import junit.framework.TestCase;

public class LinkUtilsTest extends TestCase {
    public void testGetMessageId() {
        // Tests RSDN links
        assertEquals(1863587, LinkUtils.getMessageId("http://rsdn.ru/Forum/message/1863587.1.aspx").intValue());
        assertEquals(1044129, LinkUtils.getMessageId("http://gzip.rsdn.ru/Forum/message/1044129.1.aspx").intValue());
        assertEquals(1044129, LinkUtils.getMessageId("http://www.rsdn.ru/Forum/message/1044129.1.aspx").intValue());
        assertEquals(1083065, LinkUtils.getMessageId("http://rsdn.ru/forum/Message.aspx?mid=1083065&only=1").intValue());
        assertEquals(1083065, LinkUtils.getMessageId("http://www.rsdn.ru/forum/Message.aspx?mid=1083065&only=1").intValue());
        assertEquals(1083065, LinkUtils.getMessageId("http://gzip.rsdn.ru/forum/Message.aspx?mid=1083065&only=1").intValue());

        // Tests broken and non-RSDN links
        assertNull(LinkUtils.getMessageId("http://rsdn.ru/message/1863587.1.aspx"));
        assertNull(LinkUtils.getMessageId("http://dot.net.ru/message/1863587.1.aspx"));
        assertNull(LinkUtils.getMessageId("http://www.google.com/"));
    }

    public void testGetThreadId() {
        // Tests RSDN links
        assertEquals(1863587, LinkUtils.getThreadId("http://rsdn.ru/Forum/message/1863587.flat.aspx").intValue());
        assertEquals(1044129, LinkUtils.getThreadId("http://gzip.rsdn.ru/Forum/message/1044129.flat.aspx").intValue());
        assertEquals(1044129, LinkUtils.getThreadId("http://www.rsdn.ru/Forum/message/1044129.flat.aspx").intValue());
        assertEquals(1083065, LinkUtils.getThreadId("http://rsdn.ru/forum/Message.aspx?mid=1083065&all=1").intValue());
        assertEquals(1083065, LinkUtils.getThreadId("http://www.rsdn.ru/forum/Message.aspx?mid=1083065&all=1").intValue());
        assertEquals(1083065, LinkUtils.getThreadId("http://gzip.rsdn.ru/forum/Message.aspx?mid=1083065&all=1").intValue());
        assertEquals(1083065, LinkUtils.getThreadId("http://rsdn.ru/forum/Message.aspx?mid=1083065").intValue());
        assertEquals(1083065, LinkUtils.getThreadId("http://www.rsdn.ru/forum/Message.aspx?mid=1083065").intValue());
        assertEquals(1083065, LinkUtils.getThreadId("http://gzip.rsdn.ru/forum/Message.aspx?mid=1083065").intValue());

        // Tests broken and non-RSDN links
        assertNull(LinkUtils.getThreadId("http://rsdn.ru/message/1863587.1.aspx"));
        assertNull(LinkUtils.getThreadId("http://dot.net.ru/message/1863587.1.aspx"));
        assertNull(LinkUtils.getThreadId("http://www.google.com/"));
        assertNull(LinkUtils.getThreadId("http://www.rsdn.ru/Forum/message/1044129.1.aspx"));
        assertNull(LinkUtils.getThreadId("http://rsdn.ru/forum/Message.aspx?mid=1083065&only=1"));
    }

    public void testIdFromAny() {
        // Tests RSDN links
        assertEquals(1863587, LinkUtils.getMessageIdFromUrl("http://rsdn.ru/Forum/message/1863587.1.aspx").intValue());
        assertEquals(1044129, LinkUtils.getMessageIdFromUrl("http://gzip.rsdn.ru/Forum/message/1044129.1.aspx").intValue());
        assertEquals(1044129, LinkUtils.getMessageIdFromUrl("http://www.rsdn.ru/Forum/message/1044129.1.aspx").intValue());
        assertEquals(1083065, LinkUtils.getMessageIdFromUrl("http://rsdn.ru/forum/Message.aspx?mid=1083065&only=1").intValue());
        assertEquals(1083065, LinkUtils.getMessageIdFromUrl("http://www.rsdn.ru/forum/Message.aspx?mid=1083065&only=1").intValue());
        assertEquals(1083065, LinkUtils.getMessageIdFromUrl("http://gzip.rsdn.ru/forum/Message.aspx?mid=1083065&only=1").intValue());

        // Tests broken and non-RSDN links
        assertNull(LinkUtils.getMessageIdFromUrl("http://rsdn.ru/message/1863587.1.aspx"));
        assertNull(LinkUtils.getMessageIdFromUrl("http://dot.net.ru/message/1863587.1.aspx"));
        assertNull(LinkUtils.getMessageIdFromUrl("http://www.google.com/"));
        assertEquals(1863587, LinkUtils.getMessageIdFromUrl("http://rsdn.ru/Forum/message/1863587.flat.aspx").intValue());
        assertEquals(1044129, LinkUtils.getMessageIdFromUrl("http://gzip.rsdn.ru/Forum/message/1044129.flat.aspx").intValue());
        assertEquals(1044129, LinkUtils.getMessageIdFromUrl("http://www.rsdn.ru/Forum/message/1044129.flat.aspx").intValue());
        assertEquals(1083065, LinkUtils.getMessageIdFromUrl("http://rsdn.ru/forum/Message.aspx?mid=1083065&all=1").intValue());
        assertEquals(1083065, LinkUtils.getMessageIdFromUrl("http://www.rsdn.ru/forum/Message.aspx?mid=1083065&all=1").intValue());
        assertEquals(1083065, LinkUtils.getMessageIdFromUrl("http://gzip.rsdn.ru/forum/Message.aspx?mid=1083065&all=1").intValue());
        assertEquals(1083065, LinkUtils.getMessageIdFromUrl("http://rsdn.ru/forum/Message.aspx?mid=1083065").intValue());
        assertEquals(1083065, LinkUtils.getMessageIdFromUrl("http://www.rsdn.ru/forum/Message.aspx?mid=1083065").intValue());
        assertEquals(1083065, LinkUtils.getMessageIdFromUrl("http://gzip.rsdn.ru/forum/Message.aspx?mid=1083065").intValue());
        assertEquals(1044129, LinkUtils.getMessageIdFromUrl("http://gzip.rsdn.ru/Forum/message/1044129.aspx").intValue());

        // Tests broken and non-RSDN links
        assertNull(LinkUtils.getMessageIdFromUrl("http://rsdn.ru/message/1863587.1.aspx"));
        assertNull(LinkUtils.getMessageIdFromUrl("http://dot.net.ru/message/1863587.1.aspx"));
        assertNull(LinkUtils.getMessageIdFromUrl("http://www.google.com/"));
//        assertNull(LinkUtils.getMessageIdFromUrl("http://www.rsdn.ru/Forum/message/1044129.1.aspx"));
//        assertNull(LinkUtils.getMessageIdFromUrl("http://rsdn.ru/forum/Message.aspx?mid=1083065&only=1"));
    }
}
