package org.xblackcat.sunaj.service.converter;

/**
 * Date: 19 лют 2008
 *
 * @author xBlackCat
 */

public interface ITagData {
    /**
     * Returns start position of the tag in the text.
     *
     * @return tag start position in text.
     */
    int start();

    /**
     * Returns end position of the tag in the text.
     *
     * @return tag end position in text.
     */
    int end();

    /**
     * Returns open tag.
     *
     * @return open tag.
     */
    String getHead();

    /**
     * Returns close tag.
     *
     * @return close tag.
     */
    String getTail();

    /**
     * Returns processed body of the tag.
     *
     * @return processed body/
     */
    String getBody();
}
