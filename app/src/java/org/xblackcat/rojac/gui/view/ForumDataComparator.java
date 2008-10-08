package org.xblackcat.rojac.gui.view;

import org.xblackcat.rojac.gui.model.ForumData;

import java.util.Comparator;

/**
 * Date: 8 זמגע 2008
*
* @author xBlackCat
*/
class ForumDataComparator implements Comparator<ForumData> {
    public int compare(ForumData o1, ForumData o2) {
        if (o1 == null || o1.getForum() == null) {
            return o2 == null || o2.getForum() == null ? 0 : -1;
        } else if (o2 == null || o2.getForum() == null) {
            return 1;
        } else {
            return o1.getForum().getForumName().compareToIgnoreCase(o2.getForum().getForumName());
        }
    }
}
