package org.xblackcat.rojac.service.converter.tag;

import org.xblackcat.rojac.data.Smile;
import org.xblackcat.rojac.service.converter.ITag;
import org.xblackcat.rojac.service.converter.ITagInfo;
import org.xblackcat.utils.ResourceUtils;

/**
 * <p/>
 *
 * @author xBlackCat
 */

public class SmileTag implements ITag<SmileTag> {
    private final SingleTag[] tags;

    protected SmileTag(Smile smile) {
        String[] smiles = smile.getTags();
        tags = new SingleTag[smiles.length];
        for (int i = 0; i < smiles.length; i++) {
            String tag = smiles[i];
            tags[i] = new SingleTag(
                    tag,
                    "<img border='0' src='" +
                            ResourceUtils.getFullPathToResource(smile.getPath()) +
                            "' alt='" +
                            tag +
                            "'/>",
                    smile.ordinal()
            );
        }
    }

    @SuppressWarnings("unchecked")
    public ITagInfo<SmileTag> find(String text, String lower) {
        ITagInfo<SmileTag> first = null;

        for (ITag t : tags) {
            ITagInfo<SmileTag> next = t.find(text, lower);
            if (first == null) {
                first = next;
            } else if (next != null && next.start() < first.start()) {
                first = next;
            }
        }

        return first;
    }

    public int compareTo(SmileTag o) {
        return 0;
    }
}
