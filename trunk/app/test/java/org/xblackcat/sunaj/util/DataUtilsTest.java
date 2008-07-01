package org.xblackcat.sunaj.util;

import junit.framework.TestCase;
import org.xblackcat.sunaj.data.Mark;
import org.xblackcat.sunaj.data.NewRating;
import ru.rsdn.Janus.PostRatingInfo;

/**
 * Date: 27 черв 2008
 *
 * @author xBlackCat
 */

public class DataUtilsTest extends TestCase {
    public void testArrayConvertor() {
        NewRating[] r = new NewRating[] {
                new NewRating(0, 0, Mark.Agree.getValue()),
                new NewRating(1, 1, Mark.Agree.getValue()),
                new NewRating(2, 2, Mark.Agree.getValue()),
        };

        PostRatingInfo[] oo = DataUtils.getRSDNObject(r);

        assertEquals(oo.length, r.length);
        for (PostRatingInfo o : oo) {
            assertEquals(o.getClass(), PostRatingInfo.class);
        }
    }
}
