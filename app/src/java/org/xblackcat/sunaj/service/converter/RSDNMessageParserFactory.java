package org.xblackcat.sunaj.service.converter;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xblackcat.utils.ResourceUtils;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

/**
 * Date: 20 лют 2008
 *
 * @author xBlackCat
 */

public final class RSDNMessageParserFactory {
    private static final Log log = LogFactory.getLog(RSDNMessageParserFactory.class);

    private final IMessageParser parser;

    /**
     * Intermediate constructor for allowing unit tests
     *
     * @param tagsResource
     * @param tagRulesResource
     * @param tagGroupsResource
     *
     * @throws IOException
     */
    RSDNMessageParserFactory(String tagsResource, String tagRulesResource, String tagGroupsResource) throws IOException {

        // Read all available tags
        Map<String, ITag> allTagsMap = loadAvailableTags(tagsResource);

        final Map<String, ITag[]> tagGroups;
        if (StringUtils.isNotEmpty(tagGroupsResource)) {
            tagGroups = loadTagGroups(allTagsMap, tagGroupsResource);
        } else {
            tagGroups = Collections.EMPTY_MAP;
        }

        // Load subtags rules
        final Map<ITag, ITag[]> tags = loadTagRules(allTagsMap, tagGroups, tagRulesResource);

        parser = new RSDNMessageParser(tags);
    }

    private Map<String, ITag[]> loadTagGroups(Map<String, ITag> allTagsMap, String tagGroupsResource) throws IOException {
        final HashMap<String, ITag[]> map = new HashMap<String, ITag[]>();

        final InputStream inStream = ResourceUtils.getResourceAsStream(tagGroupsResource);
        if (inStream == null) {
            throw new IOException("Can not taggroups resource file: " + tagGroupsResource);
        }

        Properties groups = new Properties();
        groups.load(inStream);

        for (Map.Entry<Object, Object> e : groups.entrySet()) {
            String groupName = e.getKey().toString();
            final String group = e.getValue().toString();

            if (log.isTraceEnabled()) {
                log.trace("Process group " + groupName + ": " + group);
            }

            String[] subTagNames = group.split(",");
            Collection<ITag> subTags = new LinkedList<ITag>();

            for (String s : subTagNames) {
                ITag t = allTagsMap.get(s);

                if (t != null) {
                    subTags.add(t);
                } else {
                    if (log.isWarnEnabled()) {
                        log.warn("Invalid tag name: " + s + " in tag group " + groupName);
                    }
                }
            }

            map.put(groupName, subTags.toArray(new ITag[subTags.size()]));
        }

        return map;
    }

    public RSDNMessageParserFactory() throws IOException {
        this("/message/tags.properties", "/message/tagrules.properties", "/message/taggroup.properties");
    }

    private Map<String, ITag> loadAvailableTags(String tagsResource) throws IOException {
        Map<String, ITag> allTagsMap = new HashMap<String, ITag>();
        Properties allTags = new Properties();

        allTags.load(ResourceUtils.getResourceAsStream(tagsResource));
        if (log.isTraceEnabled()) {
            log.trace("Parsers list has been loaded. Total " + allTags.size() + " items.");
        }

        for (Map.Entry<Object, Object> key : allTags.entrySet()) {
            String pName = key.getKey().toString();
            String pClassName = key.getValue().toString();

            if (log.isTraceEnabled()) {
                log.trace("Loading parser " + pName + " [class = " + pClassName + "].");
            }

            ITag tag;
            if (pClassName.indexOf('#') != -1) {
                // # at line start - means enum value.
                tag = loadFromMethod(pClassName);
            } else {
                tag = loadObject(pClassName);
            }
            if (tag != null) {
                allTagsMap.put(pName, tag);
            } else {
                if (log.isWarnEnabled()) {
                    log.warn("Can not load class " + pClassName + ". Tag " + pName + " will not work.");
                }
            }
        }
        return allTagsMap;
    }

    private ITag loadFromMethod(String className) {
        ITag tag = null;

        String[] parts = className.split("#");
        if (parts.length != 2) {
            throw new IllegalArgumentException("Can not split value " + className);
        }

        String aClassName = parts[0];
        String aMethod = parts[1];

        try {
            Object o = ResourceUtils.loadObjectOrEnum(aClassName);
            final Method method = o.getClass().getMethod(aMethod);

            if (ITag.class.isAssignableFrom(method.getReturnType())) {
                tag = (ITag) method.invoke(o);
            }

        } catch (ClassNotFoundException e) {
            if (log.isWarnEnabled()) {
                log.warn("Can not load parser class.", e);
            }
        } catch (IllegalAccessException e) {
            if (log.isWarnEnabled()) {
                log.warn("Can not get access to the class", e);
            }
        } catch (InstantiationException e) {
            if (log.isWarnEnabled()) {
                log.warn("Can not create class instance", e);
            }
        } catch (NoSuchMethodException e) {
            if (log.isWarnEnabled()) {
                log.warn("Can not obtain parser", e);
            }
        } catch (InvocationTargetException e) {
            if (log.isWarnEnabled()) {
                log.warn("Can not obtain parser", e);
            }
        }

        return tag;
    }

    private ITag loadObject(String pClassName) {
        ITag tag = null;

        try {
            Object o = ResourceUtils.loadObjectOrEnum(pClassName);
            if (o instanceof ITag) {
                tag = (ITag) o;
            }
        } catch (ClassNotFoundException e) {
            if (log.isWarnEnabled()) {
                log.warn("Can not load parser class.", e);
            }
        } catch (IllegalAccessException e) {
            if (log.isWarnEnabled()) {
                log.warn("Can not get access to the class", e);
            }
        } catch (InstantiationException e) {
            if (log.isWarnEnabled()) {
                log.warn("Can not create class instance", e);
            }
        }
        return tag;
    }

    private HashMap<ITag, ITag[]> loadTagRules(Map<String, ITag> allTagsMap, Map<String, ITag[]> tagGroups, String subTagsResource) throws IOException {
        final HashMap<ITag, ITag[]> tags = new HashMap<ITag, ITag[]>();
        Properties subTagsList = new Properties();
        subTagsList.load(ResourceUtils.getResourceAsStream(subTagsResource));

        if (log.isTraceEnabled()) {
            log.trace("Tag rules list has been loaded. Total " + subTagsList.size() + " entries.");
        }

        final Collection<ITag> allTags = allTagsMap.values();
        tags.put(null, allTags.toArray(new ITag[allTags.size()]));

        for (Map.Entry<Object, Object> e : subTagsList.entrySet()) {
            String tagName = e.getKey().toString();
            final String tagRule = e.getValue().toString();

            if (log.isTraceEnabled()) {
                log.trace("Process rule for tag " + tagName + ": " + tagRule);
            }

            String[] subTagNames = StringUtils.isNotEmpty(tagRule) ? tagRule.split(",") : ArrayUtils.EMPTY_STRING_ARRAY;

            ITag tag = allTagsMap.get(tagName);
            if (tag != null) {
                Collection<ITag> subTags = new LinkedList<ITag>();

                for (String s : subTagNames) {
                    ITag t = allTagsMap.get(s);

                    if (t != null) {
                        subTags.add(t);
                    } else {
                        ITag[] group = tagGroups.get(s);

                        if (group != null) {
                            subTags.addAll(Arrays.asList(group));
                        } else {
                            if (log.isWarnEnabled()) {
                                log.warn("Invalid tag or group name: " + s + " in rule for tag " + tagName);
                            }
                        }
                    }
                }

                tags.put(tag, subTags.toArray(new ITag[subTags.size()]));
            } else {
                if (log.isWarnEnabled()) {
                    log.warn("Invalid tag name: " + tagName);
                }
            }
        }
        return tags;
    }

    public IMessageParser getParser() {
        return parser;
    }
}
