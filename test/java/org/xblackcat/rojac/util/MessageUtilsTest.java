package org.xblackcat.rojac.util;

import junit.framework.TestCase;

/**
 * @author xBlackCat
 */

public class MessageUtilsTest extends TestCase {
    public void testSubjectChanger() {
        {
            String was = "Test subject";
            String expectred = "Re: Test subject";

            assertEquals(expectred, MessageUtils.correctSubject(was));
        }
        {
            String was = "Re: Test subject";
            String expectred = "Re[2]: Test subject";

            assertEquals(expectred, MessageUtils.correctSubject(was));
        }
        {
            String was = "Re:   Test subject";
            String expectred = "Re[2]: Test subject";

            assertEquals(expectred, MessageUtils.correctSubject(was));
        }
        {
            String was = "Re[2]: Test subject";
            String expectred = "Re[3]: Test subject";

            assertEquals(expectred, MessageUtils.correctSubject(was));
        }
        {
            String was = "Re[6542652]: Test subject";
            String expectred = "Re[6542653]: Test subject";

            assertEquals(expectred, MessageUtils.correctSubject(was));
        }
        {
            String was = "Re [ 6 ]: Test subject";
            String expectred = "Re: Re [ 6 ]: Test subject";

            assertEquals(expectred, MessageUtils.correctSubject(was));
        }
    }
}
