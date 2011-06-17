package org.xblackcat.rojac.service.options;

import org.apache.commons.lang.StringUtils;
import org.xblackcat.rojac.RojacDebugException;
import org.xblackcat.rojac.gui.OpenMessageMethod;
import org.xblackcat.rojac.gui.theme.IconPack;
import org.xblackcat.rojac.gui.view.forumlist.ForumFilterState;
import org.xblackcat.rojac.service.ServiceFactory;
import org.xblackcat.rojac.util.UIUtils;

import javax.swing.*;
import java.awt.*;
import java.lang.ref.SoftReference;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

/**
 * Class for holding application properties names.
 * <p/>
 *
 * @author Alexey
 */

@SuppressWarnings({"UnnecessaryBoxing"})
public final class Property<T> {
    private static final LocaleValueChecker localeChecker = new LocaleValueChecker();

    /**
     * Complete map of properties names to its objects.
     */
    private static final Set<Property<?>> ALL_PROPERTIES = new HashSet<Property<?>>();
    // Global development properties

    public static final Property<Boolean> ROJAC_DEBUG_MODE = createPrivate("rojac.global.debug.mode", Boolean.FALSE);
    // Application component properties
    public static final Property<Dimension> ROJAC_MAIN_FRAME_SIZE = createPrivate("rojac.main_frame.size", Dimension.class);
    public static final Property<Point> ROJAC_MAIN_FRAME_POSITION = createPrivate("rojac.main_frame.position", Point.class);

    public static final Property<Integer> ROJAC_MAIN_FRAME_STATE = createPrivate("rojac.main_frame.state", Integer.class);
    // Main GUI properties
    public static final Property<LookAndFeel> ROJAC_GUI_LOOK_AND_FEEL = create("rojac.gui.laf", LookAndFeel.class, UIUtils.getDefaultLAFClass(), new LAFValueChecker());
    public static final Property<Locale> ROJAC_GUI_LOCALE = create("rojac.gui.locale", getDefaultLocale(), localeChecker);

    // Main frame behaviour
    public static final Property<Boolean> ROJAC_MAIN_FRAME_HIDE_ON_MINIMIZE = create("rojac.behaviour.frame.hide_on_minimize", Boolean.TRUE);
    public static final Property<Boolean> ROJAC_MAIN_FRAME_HIDE_ON_CLOSE = create("rojac.behaviour.frame.hide_on_close", Boolean.FALSE);
    public static final Property<Boolean> ROJAC_MAIN_FRAME_ASK_ON_CLOSE = create("rojac.behaviour.question.on_close", Boolean.TRUE);

    public static final Property<IconPack> ROJAC_GUI_ICONPACK = create("rojac.gui.iconpack", IconPackValueChecker.DEFAULT_ICON_PACK, new IconPackValueChecker());

    // User properties (login dialog)
    public static final Property<String> RSDN_USER_NAME = createPrivate("rojac.rsdn.user.name", String.class);
    public static final Property<Password> RSDN_USER_PASSWORD = createPrivate("rojac.rsdn.user.password", Password.class);
    public static final Property<Integer> RSDN_USER_ID = createPrivate("rojac.rsdn.user.id", Integer.class);
    public static final Property<Boolean> RSDN_USER_PASSWORD_SAVE = createPrivate("rojac.rsdn.user.password.save", Boolean.FALSE);

    public static final Property<CheckUpdatesEnum> UPDATER_PERIOD = create("rojac.updater.period", CheckUpdatesEnum.EveryWeek);
    public static final Property<Long> UPDATER_LAST_CHECK = createPrivate("rojac.updater.last_check", Long.class);

    // Behaviour in opening message in tab
    public static final Property<OpenMessageMethod> OPEN_MESSAGE_BEHAVIOUR_GENERAL = create("rojac.behaviour.open_message.general", OpenMessageMethod.InForum);
    public static final Property<OpenMessageMethod> OPEN_MESSAGE_BEHAVIOUR_RECENT_TOPICS = create("rojac.behaviour.open_message.from_recent_topics", OpenMessageMethod.InThread,
            new GeneralEnumChecker<OpenMessageMethod>(OpenMessageMethod.InThread, OpenMessageMethod.InForum));

    // Progress dialog properties
    public static final Property<Boolean> DIALOGS_PROGRESS_AUTOSHOW = create("rojac.synchronizer.progress.autoshow", Boolean.TRUE);
    public static final Property<Boolean> DIALOGS_PROGRESS_AUTOHIDE = create("rojac.synchronizer.progress.autohide", Boolean.FALSE);
    public static final Property<Boolean> DIALOGS_PROGRESS_SHOW_ON_EXCEPTION = create("rojac.synchronizer.progress.show_on_exception", Boolean.TRUE);

    // Forum view properties.
    public static final Property<Integer> VIEW_NAVIGATION_HISTORY_SIZE = create("rojac.view.history_size", 20);
    public static final Property<Long> VIEW_THREAD_AUTOSET_READ = create("rojac.view.message.read_delay", Long.valueOf(1000));
    public static final Property<Boolean> VIEW_THREAD_SET_READ_ON_SCROLL = create("rojac.view.message.read_on_scroll", Boolean.TRUE);
    public static final Property<Boolean> VIEW_THREAD_COLLAPSE_THREADS_AFTER_SYNC = create("rojac.view.thread.collapse_threads_after_sync", Boolean.FALSE);
    public static final Property<Boolean> VIEW_THREAD_COLLAPSE_THREADS_AFTER_GO2NEXT = create("rojac.view.thread.collapse_threads_after_go_to_next", Boolean.TRUE);
    public static final Property<Integer> VIEW_THREAD_TAB_TITLE_LIMIT = create("rojac.view.thread.tab_title_limit", Integer.valueOf(0));

//    public static final Property<TextStyle> VIEW_THREAD_STYLE_UNREAD_POST = create("rojac.view.styles.unread_post", TextStyle.DEFAULT);
//    public static final Property<TextStyle> VIEW_THREAD_STYLE_READ_POST = create("rojac.view.styles.read_post", TextStyle.DEFAULT);
//    public static final Property<TextStyle> VIEW_THREAD_STYLE_PARTIAL_READ_POST = create("rojac.view.styles.partial_read_post", TextStyle.DEFAULT);

    public static final Property<Integer> VIEW_RECENT_TOPIC_LIST_SIZE = create("rojac.view.recent_topics.list_size", 20);

    // Janus synchronizer properties
    public static final Property<Integer> SYNCHRONIZER_SCHEDULE_PERIOD = create("rojac.synchronizer.schedule.period", Integer.valueOf(0));
    public static final Property<Boolean> SYNCHRONIZER_SCHEDULE_AT_START = create("rojac.synchronizer.schedule.at_start", Boolean.FALSE);

    /**
     * This boolean property indicated is should be used GZip compression while retrieving information from Janus WS.
     */
    public static final Property<Boolean> SYNCHRONIZER_USE_GZIP = create("rojac.synchronizer.use_compression", Boolean.TRUE);

    public static final Property<Boolean> SYNCHRONIZER_LOAD_TOPICS_AT_ONCE = create("rojac.behaviour.load_topics_at_once", Boolean.FALSE);
    public static final Property<Boolean> SYNCHRONIZER_LOAD_USERS = create("rojac.synchronizer.load.users", Boolean.FALSE);
    public static final Property<Integer> SYNCHRONIZER_LOAD_USERS_PORTION = create("rojac.synchronizer.load.users.portion", Integer.valueOf(1000));
    public static final Property<Integer> SYNCHRONIZER_LOAD_MESSAGES_PORTION = create("rojac.synchronizer.load.messages.portion", Integer.valueOf(100));

    public static final Property<Boolean> SYNCHRONIZER_PROXY_ENABLED = create("rojac.synchronizer.proxy", Boolean.FALSE);
    public static final Property<String> SYNCHRONIZER_PROXY_HOST = create("rojac.synchronizer.proxy.host", String.class);
    public static final Property<Integer> SYNCHRONIZER_PROXY_PORT = create("rojac.synchronizer.proxy.port", Integer.class);
    public static final Property<String> SYNCHRONIZER_PROXY_USER = create("rojac.synchronizer.proxy.user", String.class);
    public static final Property<Password> SYNCHRONIZER_PROXY_PASSWORD = create("rojac.synchronizer.proxy.pass", Password.class);

    public static final Property<Boolean> SYNCHRONIZER_MARK_MY_POST_READ = create("rojac.synchronizer.messages.my_as_read", Boolean.FALSE);

    public static final Property<Boolean> TRAY_NOTIFICATION_SYNC_COMPLETE = create("rojac.behaviour.tray_notification.on_sync_complete", Boolean.TRUE);

    // Forum list view state properties.
    public static final Property<EnumSet<ForumFilterState>> VIEW_FORUM_LIST_FILTER = createPrivate("rojac.view.forum_list.filter", EnumSet.noneOf(ForumFilterState.class));

    @SuppressWarnings({"unchecked"})
    static <V> Property<V> create(String name, V defaultValue, IValueChecker<V> checker) {
        return create(name, (Class<V>) defaultValue.getClass(), defaultValue, checker);
    }

    @SuppressWarnings({"unchecked"})
    static <V> Property<V> createPrivate(String name, V defaultValue) {
        return create(false, name, (Class<V>) defaultValue.getClass(), defaultValue, null);
    }

    @SuppressWarnings({"unchecked"})
    static <V> Property<V> create(String name, V defaultValue) {
        return create(name, (Class<V>) defaultValue.getClass(), defaultValue, null);
    }

    static <V> Property<V> create(String name, Class<V> type, V defaultValue, IValueChecker<V> checker) {
        return create(true, name, type, defaultValue, checker);
    }

    @SuppressWarnings({"unchecked"})
    static <V> Property<V> create(boolean isPublic, String name, Class<V> type, V defaultValue, IValueChecker<V> checker) {
        if (type.isEnum() && checker == null) {
            // Use default enum checker
            checker = new GeneralEnumChecker(type);
        }

        Property<V> prop = new Property<V>(isPublic, name, type, defaultValue, checker);
        if (ALL_PROPERTIES.contains(prop)) {
            throw new RojacDebugException("Property '" + name + "' already defined.");
        }

        ALL_PROPERTIES.add(prop);
        return prop;
    }

    /**
     * Util method for create property object.
     *
     * @param name property name
     * @param type class representing property value type.
     * @param <E>  property value type
     * @return newly generated property object.
     */
    static <E> Property<E> create(String name, Class<E> type) {
        return create(name, type, null, null);
    }

    /**
     * Util method for create property object.
     *
     * @param name property name
     * @param type class representing property value type.
     * @param <E>  property value type
     * @return newly generated property object.
     */
    static <E> Property<E> createPrivate(String name, Class<E> type) {
        return create(false, name, type, null, null);
    }

    /*
     * Other util methods
     */
    private static Locale getDefaultLocale() {
        Locale locale = Locale.getDefault();
        if (localeChecker.isValueCorrect(locale)) {
            return locale;
        }

        if (!StringUtils.isEmpty(locale.getLanguage())) {
            for (Locale l : localeChecker.getPossibleValues()) {
                if (locale.getLanguage().equals(l.getLanguage())) {
                    return l;
                }
            }
        }

        return Locale.ROOT;
    }

    /**
     * Returns all properties as an array.
     *
     * @return all option properties.
     */
    public static Property<?>[] getAllProperties() {
        return ALL_PROPERTIES.toArray(new Property[ALL_PROPERTIES.size()]);
    }

    /**
     * Only public properties can be shown in Options dialog.
     */
    private final boolean pub;
    private final String name;
    private final Class<T> type;
    private final T defaultValue;
    private final IValueChecker<T> checker;

    private SoftReference<T> cache;

    private Property(boolean pub, String name, Class<T> type, T defaultValue, IValueChecker<T> checker) {
        if (name == null) throw new NullPointerException("Property name can not be null.");
        if (type == null) throw new NullPointerException("Class type can not be null.");

        this.pub = pub;
        this.name = name;
        this.type = type;
        this.checker = checker;
        this.defaultValue = defaultValue;
    }

    /**
     * Returns internal property name.
     *
     * @return internal name.
     */
    public String getName() {
        return name;
    }

    /**
     * Returns a class of the property values.
     *
     * @return class of the property value.
     */
    public Class<T> getType() {
        return type;
    }

    /**
     * Returns the property checker if any.
     *
     * @return value checker if the one exists and <code>null</code> elsevise.
     */
    public IValueChecker<T> getChecker() {
        return checker;
    }

    public boolean isPublic() {
        return pub;
    }

    /**
     * Checks if the property is set.
     *
     * @return <code>true</code> if property is initiated and <code>false</code> if default value will be used.
     */
    public boolean isSet() {
        ServiceFactory instance = ServiceFactory.getInstance();
        // If options service is not initialized - consider property is not set.
        return instance != null && instance.getOptionsService() != null && instance.getOptionsService().getProperty(this) != null;

    }

    /**
     * Returns a current value of property or default if it not set.
     *
     * @return current value of property or default one.
     */
    public T get() {
        return get(getDefault());
    }

    /**
     * Returns a current value of the property or specified value if the property is not set.
     *
     * @param defValue value to be treated as default.
     * @return value of the property.
     */
    public T get(T defValue) {
        ServiceFactory instance = ServiceFactory.getInstance();
        // For testcase purposes.
        if (instance == null) {
            return defValue;
        }

        T val = instance.getOptionsService().getProperty(this);

        return val == null ? defValue : val;
    }

    /**
     * Returns a default value of the property.
     *
     * @return default value of the property.
     */
    public T getDefault() {
        return defaultValue;
    }

    /**
     * Sets a new value to the property.
     *
     * @param val a new value.
     * @return previous value of the property or <code>null</code> if property was empty.
     */
    public T set(T val) {
        ServiceFactory instance = ServiceFactory.getInstance();
        if (instance != null && instance.getOptionsService() != null) {
            return instance.getOptionsService().setProperty(this, val);
        }

        return getDefault();
    }

    /**
     * Clears the property value and returns previous value.
     *
     * @return previous value of the property before clearing.
     */
    public T clear() {
        return ServiceFactory.getInstance().getOptionsService().setProperty(this, null);
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Property property = (Property) o;

        return name.equals(property.name);
    }

    public int hashCode() {
        return name.hashCode();
    }

    public String toString() {
        return "Property[" + name + '(' + type.getName() + ")]";
    }

    public T getCache() {
        return cache != null ? cache.get() : null;
    }

    public void setCache(T cache) {
        if (cache != null) {
            this.cache = new SoftReference<T>(cache);
        } else {
            this.cache = null;
        }
    }

}
