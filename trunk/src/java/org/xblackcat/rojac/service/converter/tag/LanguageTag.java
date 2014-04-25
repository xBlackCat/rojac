package org.xblackcat.rojac.service.converter.tag;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.xblackcat.rojac.service.converter.ITagData;
import org.xblackcat.rojac.service.converter.ITagInfo;
import org.xblackcat.utils.ResourceUtils;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author xBlackCat
 */

public class LanguageTag extends SimpleTag {
    // Constant values
    private static final String RESWORDS_PATH = "/message/reswords/";
    private static final String RESWORDS_SUFFIX = ".words";

    private static final String TAG_OPEN = "<table width='96%'><tr><td nowrap='nowrap' class='c'><pre>";
    private static final String TAG_CLOSE = "</pre></td></tr></table>";
    private static final String RESERVED_WORD_START = "<span class='kw'>";
    private static final String COMMENT_START = "<span class='com'>";
    private static final String STRING_START = "<span class='str'>";
    private static final String HIGHLIGHT_END = "</span>";

    // Patterns for processing body
    private final Pattern reservedWordsPattern;
    private final Pattern lineCommentPattern;
    private final Pattern blockCommentPattern;
    private final Pattern stringQuotesPattern;

    /**
     * Constructs a programming language tag. Tag rules:</p> highlight reserved words in the tag body. List of the
     * reserved words for correspond language reads from the {@code /message/reswords/&lt;tagName&gt;.words}
     * resource file. Reserved words in line/block comments or in strings are not hihglighted.
     *
     * @param tagName           tag name.
     * @param lineComment       symbol or sequence of symbols which starts to-line-end comment. Can be {@code null}
     *                          if the language does not supports to-line-end comments.
     * @param openBlockComment  symbol or sequence of symbols represents start of the block comment. Can be
     *                          {@code null} if the language has no block comment.
     * @param closeBlockComment symbol or sequence of symbols represents end of the block comment. Can be
     *                          {@code null} if the language shouldn't handle strings if the language.
     * @param stringBraces      set of characters whitch represent start/end of string literal.
     * @param quoteCharacter    quoting character.
     */
    protected LanguageTag(
            String tagName,
            String lineComment,
            String openBlockComment,
            String closeBlockComment,
            String stringBraces,
            Character quoteCharacter) {
        this(tagName, lineComment, openBlockComment, closeBlockComment, stringBraces, quoteCharacter, loadReservedWords(tagName));
    }

    /**
     * Constructs a programming language tag. Tag rules:</p> highlight reserved words in the tag body. List of the
     * reserved words for correspond language reads from the {@code /message/reswords/&lt;tagName&gt;.words}
     * resource file. Reserved words in line/block comments or in strings are not hihglighted.
     *
     * @param tagName           tag name.
     * @param lineComment       symbol or sequence of symbols which starts to-line-end comment. Can be {@code null}
     *                          if the language does not supports to-line-end comments.
     * @param openBlockComment  symbol or sequence of symbols represents start of the block comment. Can be
     *                          {@code null} if the language has no block comment.
     * @param closeBlockComment symbol or sequence of symbols represents end of the block comment. Can be
     *                          {@code null} if the language shouldn't handle strings if the language.
     * @param stringBraces      set of characters whitch represent start/end of string literal.
     * @param quoteCharacter    quoting character.
     * @param reservedWords
     */
    protected LanguageTag(
            String tagName,
            String lineComment,
            String openBlockComment,
            String closeBlockComment,
            String stringBraces,
            Character quoteCharacter,
            String... reservedWords) {
        super(tagName, TAG_OPEN, TAG_CLOSE);

        if (StringUtils.isEmpty(openBlockComment) != StringUtils.isEmpty(closeBlockComment)) {
            throw new IllegalArgumentException("Open and close block comment should be both set or not.");
        }

        if (ArrayUtils.isEmpty(reservedWords)) {
            reservedWordsPattern = null; // Mark - no reserved words.
        } else {
            StringBuilder resWords = new StringBuilder();

            for (String resWord : reservedWords) {
                resWords.append("|").append(Pattern.quote(resWord));
            }

            reservedWordsPattern = Pattern.compile("\\b(" + resWords.substring(1) + ")\\b");
        }

        if (StringUtils.isNotEmpty(lineComment)) {
            lineCommentPattern = Pattern.compile(Pattern.quote(lineComment) + ".*$", Pattern.MULTILINE);
        } else {
            lineCommentPattern = null;
        }

        if (StringUtils.isNotEmpty(openBlockComment)) {
            blockCommentPattern = Pattern.compile(
                    Pattern.quote(openBlockComment) + ".*?" + Pattern.quote(closeBlockComment),
                    Pattern.DOTALL
            );
        } else {
            blockCommentPattern = null;
        }

        if (StringUtils.isNotEmpty(stringBraces)) {
            // Construct string brace pattern
            String quotedStringBraces = Pattern.quote(stringBraces);
            String quotedQuoteChar = Pattern.quote(String.valueOf(quoteCharacter));

            stringQuotesPattern = Pattern.compile("([" + quotedStringBraces +
                    "])(\\1|([^" + quotedQuoteChar + "]|(" + quotedQuoteChar + "(?:\\1|" +
                    quotedQuoteChar + "|\\s)?))+?\\1)");
        } else {
            stringQuotesPattern = null;
        }
    }

    private static String[] loadReservedWords(String tagName) {
        String resourceName = RESWORDS_PATH + tagName.toLowerCase() + RESWORDS_SUFFIX;

        String[] reservedWords;
        try {
            reservedWords = ResourceUtils.loadListFromResource(resourceName);
        } catch (IOException e) {
            throw new RuntimeException("Can not load reserved words for the " +
                    tagName + "(" + tagName + ") language", e);
        }
        return reservedWords;
    }

    protected ITagInfo<SimpleTag> getTagInfo(String text, String lower, final int startPos) {
        return new LanguageTagInfo(startPos, lower, text);
    }

    protected class LanguageTagInfo extends SimpleTagInfo {
        public LanguageTagInfo(int startPos, String lower, String text) {
            super(startPos, lower, text);
        }

        protected ITagData getTagData(int endPos, int startPos, String text) {
            return new LanguageTagData(startPos, endPos, text);
        }
    }

    protected class LanguageTagData extends SimpleTagData {
        public LanguageTagData(int startPos, int endPos, String text) {
            super(startPos, endPos, text);
        }

        public String getBody() {
            final String body = super.getBody();

            StringBuilder result = new StringBuilder();

            // Search for reserved worde and comments
            Matcher blockCommentMatcher = blockCommentPattern == null ? null : blockCommentPattern.matcher(body);
            Matcher lineCommentMatcher = lineCommentPattern == null ? null : lineCommentPattern.matcher(body);
            Matcher stringQuotesMatcher = stringQuotesPattern == null ? null : stringQuotesPattern.matcher(body);
            Matcher resWordsMatcher = reservedWordsPattern == null ? null : reservedWordsPattern.matcher(body);

            // Highliting the tag body
            int bodyStart = 0;
            int endMark = body.length();
            while (bodyStart < endMark) {
                int copyEnd = endMark;
                int groupEnd = endMark;
                String prefix = null;
                String group = null;

                // Priority of highlighting:
                //  - block comment
                //  - line comment
                //  - string literals
                //  - reserved words
                if (blockCommentMatcher != null && blockCommentMatcher.find(bodyStart)) {
                    // Copy anaffected string
                    copyEnd = blockCommentMatcher.start();
                    prefix = COMMENT_START;
                    group = blockCommentMatcher.group();
                    groupEnd = blockCommentMatcher.end();
                }
                if (lineCommentMatcher != null && lineCommentMatcher.find(bodyStart)) {
                    if (lineCommentMatcher.start() < copyEnd) {
                        copyEnd = lineCommentMatcher.start();
                        prefix = COMMENT_START;
                        group = lineCommentMatcher.group();
                        groupEnd = lineCommentMatcher.end();
                    }
                }
                if (stringQuotesMatcher != null && stringQuotesMatcher.find(bodyStart)) {
                    if (stringQuotesMatcher.start() < copyEnd) {
                        copyEnd = stringQuotesMatcher.start();
                        prefix = STRING_START;
                        group = stringQuotesMatcher.group();
                        groupEnd = stringQuotesMatcher.end();
                    }
                }
                if (resWordsMatcher != null && resWordsMatcher.find(bodyStart)) {
                    if (resWordsMatcher.start() < copyEnd) {
                        copyEnd = resWordsMatcher.start();
                        prefix = RESERVED_WORD_START;
                        group = resWordsMatcher.group();
                        groupEnd = resWordsMatcher.end();
                    }
                }

                result.append(body.substring(bodyStart, copyEnd));
                if (group != null) {
                    result.append(prefix);
                    result.append(group);
                    result.append(HIGHLIGHT_END);
                }
                bodyStart = groupEnd;
            }
            return result.toString();
        }
    }
}
