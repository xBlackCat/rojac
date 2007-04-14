package org.xblackcat.sunaj;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xblackcat.sunaj.service.janus.IJanusService;
import org.xblackcat.sunaj.service.janus.JanusService;
import org.xblackcat.sunaj.service.janus.JanusServiceException;
import org.xblackcat.sunaj.service.janus.data.TopicMessages;

/**
 * Date: 26 бер 2007
 *
 * @author Alexey
 */

public class SunajLauncher {
    private static final Log log = LogFactory.getLog(SunajLauncher.class);

    private SunajLauncher() {
    }

    public static void main(String[] args) throws Exception {
        IJanusService con = JanusService.getInstance("xBlackCat", "tryt0guess");

        if (log.isInfoEnabled()) {
            log.info("\nInitialized\n================================================================================");
        }

        con.testConnection();

        testGetTopicByMessagesIds(con);
    }

    private static void testGetTopicByMessagesIds(IJanusService con) throws JanusServiceException {
        TopicMessages t = con.getTopicByMessage(new int[]{2447774, 2447998});

        if (log.isInfoEnabled()) {
            log.info("Messages: " + ArrayUtils.toString(t.getMessages()));
            log.info("Moderates: " + ArrayUtils.toString(t.getModerates()));
            log.info("Raitings: " + ArrayUtils.toString(t.getRatings()));
        }
    }
}
