package org.xblackcat.rojac.util;
/**
 * Date: 7 ñ³÷ 2008
 * @author xBlackCat
 */


import junit.framework.TestCase;

public class LinkHandlerTest extends TestCase {
    public void testGetMessageId() {
        // Tests RSDN links
        assertEquals(1863587, LinkHandler.getMessageId("http://rsdn.ru/Forum/message/1863587.1.aspx").intValue());
        assertEquals(1044129, LinkHandler.getMessageId("http://gzip.rsdn.ru/Forum/message/1044129.1.aspx").intValue());
        assertEquals(1044129, LinkHandler.getMessageId("http://www.rsdn.ru/Forum/message/1044129.1.aspx").intValue());
        assertEquals(1083065, LinkHandler.getMessageId("http://rsdn.ru/forum/Message.aspx?mid=1083065&only=1").intValue());
        assertEquals(1083065, LinkHandler.getMessageId("http://www.rsdn.ru/forum/Message.aspx?mid=1083065&only=1").intValue());
        assertEquals(1083065, LinkHandler.getMessageId("http://gzip.rsdn.ru/forum/Message.aspx?mid=1083065&only=1").intValue());

        // Tests broken and non-RSDN links
        assertNull(LinkHandler.getMessageId("http://gzip.rsdn.ru/Forum/message/1044129.aspx"));
        assertNull(LinkHandler.getMessageId("http://rsdn.ru/message/1863587.1.aspx"));
        assertNull(LinkHandler.getMessageId("http://dot.net.ru/message/1863587.1.aspx"));
        assertNull(LinkHandler.getMessageId("http://www.google.com/"));
    }

    public void testGetThreadId() {
        // Tests RSDN links
        assertEquals(1863587, LinkHandler.getThreadId("http://rsdn.ru/Forum/message/1863587.flat.aspx").intValue());
        assertEquals(1044129, LinkHandler.getThreadId("http://gzip.rsdn.ru/Forum/message/1044129.flat.aspx").intValue());
        assertEquals(1044129, LinkHandler.getThreadId("http://www.rsdn.ru/Forum/message/1044129.flat.aspx").intValue());
        assertEquals(1083065, LinkHandler.getThreadId("http://rsdn.ru/forum/Message.aspx?mid=1083065&all=1").intValue());
        assertEquals(1083065, LinkHandler.getThreadId("http://www.rsdn.ru/forum/Message.aspx?mid=1083065&all=1").intValue());
        assertEquals(1083065, LinkHandler.getThreadId("http://gzip.rsdn.ru/forum/Message.aspx?mid=1083065&all=1").intValue());
        assertEquals(1083065, LinkHandler.getThreadId("http://rsdn.ru/forum/Message.aspx?mid=1083065").intValue());
        assertEquals(1083065, LinkHandler.getThreadId("http://www.rsdn.ru/forum/Message.aspx?mid=1083065").intValue());
        assertEquals(1083065, LinkHandler.getThreadId("http://gzip.rsdn.ru/forum/Message.aspx?mid=1083065").intValue());

        // Tests broken and non-RSDN links
        assertNull(LinkHandler.getThreadId("http://rsdn.ru/message/1863587.1.aspx"));
        assertNull(LinkHandler.getThreadId("http://dot.net.ru/message/1863587.1.aspx"));
        assertNull(LinkHandler.getThreadId("http://www.google.com/"));
        assertNull(LinkHandler.getThreadId("http://www.rsdn.ru/Forum/message/1044129.1.aspx"));
        assertNull(LinkHandler.getThreadId("http://rsdn.ru/forum/Message.aspx?mid=1083065&only=1"));
    }
}