package org.xblackcat.rojac.util;

import junit.framework.TestCase;
import org.xblackcat.rojac.data.Mark;
import org.xblackcat.rojac.data.NewRating;
import ru.rsdn.janus.PostRatingInfo;

import java.util.*;

/**
 * @author xBlackCat
 */

@SuppressWarnings({"unchecked"})
public class RojacUtilsTest extends TestCase {
    public void testArrayConverter() {
        NewRating[] r = new NewRating[]{
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

    public void testCollectionConverter() {
        List<NewRating> r = Arrays.asList(
                new NewRating(0, 0, Mark.Agree.getValue()),
                new NewRating(1, 1, Mark.Agree.getValue()),
                new NewRating(2, 2, Mark.Agree.getValue())
        );

        List<PostRatingInfo> oo = RojacUtils.getRSDNObject(r, PostRatingInfo.class);

        assertEquals(oo.size(), r.size());
        for (PostRatingInfo o : oo) {
            assertEquals(o.getClass(), PostRatingInfo.class);
        }
    }

    public void testBundleLocales() throws Exception {
        {
            Locale[] locales = RojacUtils.localesForBundle("/locales/bundle", false);

            Set<Locale> l = new HashSet<>(Arrays.asList(locales));

            assertEquals(3, l.size());

            assertFalse(l.remove(Locale.getDefault()));
            assertTrue(l.remove(new Locale("ii")));
            assertTrue(l.remove(new Locale("un", "EE")));
            assertTrue(l.remove(new Locale("un", "CC")));
            assertTrue(l.isEmpty());
        }

        {
            Locale[] locales = RojacUtils.localesForBundle("/locales/bundle", true);

            Set<Locale> l = new HashSet<>(Arrays.asList(locales));

            assertEquals(4, l.size());

            assertTrue(l.remove(Locale.getDefault()));
            assertTrue(l.remove(new Locale("ii")));
            assertTrue(l.remove(new Locale("un", "EE")));
            assertTrue(l.remove(new Locale("un", "CC")));
            assertTrue(l.isEmpty());

        }
    }
}
