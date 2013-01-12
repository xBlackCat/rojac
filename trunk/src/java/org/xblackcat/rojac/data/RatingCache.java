package org.xblackcat.rojac.data;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.EnumMap;

/**
 * @author xBlackCat
 */

public class RatingCache {
    private static final Log log = LogFactory.getLog(RatingCache.class);

    private final EnumMap<Mark, Integer> rating = new EnumMap<>(Mark.class);

    public RatingCache(Iterable<MarkStat> marks) {
        for (MarkStat m : marks) {
            rating.put(m.getMark(), m.getAmount());
        }
    }

    public RatingCache(String rating) {
        if (StringUtils.isEmpty(rating)) {
            return;
        }

        String[] parts = rating.split(";");
        for (String p : parts) {
            String[] m = p.split(":");

            try {
                this.rating.put(
                        Mark.getMark(Integer.parseInt(m[0].trim())),
                        Integer.parseInt(m[1].trim())
                );
            } catch (IllegalArgumentException e) {
                // Ignore invalid data in rating cache
                log.warn("Got invalid part '" + p + "' in cache string '" + rating + "'");
            }
        }
    }

    public String asString() {
        if (rating.isEmpty()) {
            return "";
        }

        StringBuilder res = new StringBuilder();
        for (Mark m : rating.keySet()) {
            res.append(';');
            res.append(m.getValue());
            res.append(':');
            res.append(rating.get(m));
        }

        return res.substring(1);
    }

    public int getRating(Mark mark) {
        Integer amount = rating.get(mark);
        return amount == null ? 0 : amount;
    }

    public boolean isEmpty() {
        return rating.isEmpty();
    }
}
