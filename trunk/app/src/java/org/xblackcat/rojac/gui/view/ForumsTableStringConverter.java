package org.xblackcat.rojac.gui.view;

import org.xblackcat.rojac.data.Forum;
import org.xblackcat.rojac.gui.model.ForumData;

import javax.swing.table.TableStringConverter;
import javax.swing.table.TableModel;

/**
 * Date: 8 ���� 2008
*
* @author xBlackCat
*/
class ForumsTableStringConverter extends TableStringConverter {
    @Override
        public String toString(TableModel model, int row, int column) {
        Forum forum = ((ForumData) model.getValueAt(row, column)).getForum();
        return forum == null ? "" : forum.getForumName();
    }
}
