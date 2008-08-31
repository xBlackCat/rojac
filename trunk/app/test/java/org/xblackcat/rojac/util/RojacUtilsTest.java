package org.xblackcat.rojac.util;

import junit.framework.TestCase;
import org.xblackcat.rojac.data.Mark;
import org.xblackcat.rojac.data.NewRating;
import ru.rsdn.Janus.PostRatingInfo;

/**
 * Date: 27 черв 2008
 *
 * @author xBlackCat
 */

public class RojacUtilsTest extends TestCase {
    public void testArrayConvertor() {
        NewRating[] r = new NewRating[] {
                new NewRating(0, 0, Mark.Agree.getValue()),
                new NewRating(1, 1, Mark.Agree.getValue()),
                new NewRating(2, 2, Mark.Agree.getValue()),
        };

        PostRatingInfo[] oo = RojacUtils.getRSDNObject(r);

        assertEquals(oo.length, r.length);
        for (PostRatingInfo o : oo) {
            assertEquals(o.getClass(), PostRatingInfo.class);
        }
    }
}
