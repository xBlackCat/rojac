package org.xblackcat.rojac.util;

import junit.framework.TestCase;
import org.xblackcat.rojac.data.Mark;
import org.xblackcat.rojac.data.NewRating;
import ru.rsdn.Janus.PostRatingInfo;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

/**
 * @author xBlackCat
 */

@SuppressWarnings({"unchecked"})
public class RojacUtilsTest extends TestCase {
    public void testArrayConvertor() {
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

    public void testBundleLocales() throws Exception {
        {
            Locale[] locales = RojacUtils.localesForBundle("/locales/bundle", false);

            Set<Locale> l = new HashSet<>(Arrays.asList(locales));

            assertEquals(3, l.size());

            assertFalse(l.remove(Locale.getDefault()));
            assertTrue(l.remove(new Locale("de")));
            assertTrue(l.remove(new Locale("en", "US")));
            assertTrue(l.remove(new Locale("en", "CA")));
            assertTrue(l.isEmpty());
        }

        {
            Locale[] locales = RojacUtils.localesForBundle("/locales/bundle", true);

            Set<Locale> l = new HashSet<>(Arrays.asList(locales));

            assertEquals(4, l.size());

            assertTrue(l.remove(Locale.getDefault()));
            assertTrue(l.remove(new Locale("de")));
            assertTrue(l.remove(new Locale("en", "US")));
            assertTrue(l.remove(new Locale("en", "CA")));
            assertTrue(l.isEmpty());

        }
    }
}
