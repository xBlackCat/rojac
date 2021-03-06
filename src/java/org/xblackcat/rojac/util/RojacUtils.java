package org.xblackcat.rojac.util;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.SystemUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.xblackcat.rojac.RojacDebugException;
import org.xblackcat.rojac.RojacException;
import org.xblackcat.rojac.RojacInitializationException;
import org.xblackcat.rojac.data.IRSDNable;
import org.xblackcat.rojac.service.executor.IExecutor;
import org.xblackcat.rojac.service.options.CheckUpdatesEnum;
import org.xblackcat.rojac.service.options.Password;
import org.xblackcat.rojac.service.options.Property;
import org.xblackcat.utils.ResourceUtils;
import ru.rsdn.janus.ArrayOfInt;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.net.URL;
import java.security.SecureRandom;
import java.util.*;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.xblackcat.rojac.service.options.Property.RSDN_USER_PASSWORD;
import static org.xblackcat.rojac.service.options.Property.RSDN_USER_PASSWORD_SAVE;

/**
 * @author xBlackCat
 */

public final class RojacUtils {
    private static final Log log = LogFactory.getLog(RojacUtils.class);

    public static final String VERSION = "0.1";
    public static final String VERSION_MODIFIER = "";
    public static final String VERSION_STRING;
    public static final Integer REVISION_NUMBER;
    public static final GlobalExceptionHandler GLOBAL_EXCEPTION_HANDLER = new GlobalExceptionHandler();

    static {
        StringBuilder versionString = new StringBuilder("Rojac v");
        versionString.append(VERSION);
        Integer rev = null;

        if (StringUtils.isNotEmpty(VERSION_MODIFIER)) {
            versionString.append(" (");
            versionString.append(VERSION_MODIFIER);
            versionString.append(")");
        }

        try {
            Properties revInfo = new Properties();
            revInfo.load(ResourceUtils.getResourceAsStream("config/rojac.revision"));

            // Remember - we are working only with build.xml revision!
            String revNum = revInfo.getProperty("revision");
            String file = revInfo.getProperty("relative.path");

            try {
                rev = Integer.parseInt(revNum);
            } catch (NumberFormatException e) {
                // Assume that we have no revision number :)
            }

            // Now fill additional info

            int pos = 1;
            int nextPos = file.indexOf('/', pos);

            String branch = file.substring(pos, nextPos);
            switch (branch) {
                case "trunk":
                    // No additional info will be added
                    break;
                case "branches":
                    // Read branch name
                    pos = nextPos + 1;
                    versionString.append(" [");

                    nextPos = file.indexOf('/', pos);

                    if (nextPos >= 0) {
                        versionString.append(file.substring(pos, nextPos));
                    } else {
                        versionString.append(file.substring(pos));
                    }

                    versionString.append(']');
                    break;
                case "tags":
                    // Read branch name
                    pos = nextPos + 1;
                    versionString.append(" <");

                    nextPos = file.indexOf('/', pos);

                    if (nextPos >= 0) {
                        versionString.append(file.substring(pos, nextPos));
                    } else {
                        versionString.append(file.substring(pos));
                    }
                    versionString.append('>');
                    break;
            }

            versionString.append(" / rev. ");
            versionString.append(revNum);
        } catch (IOException e) {
            throw new RuntimeException("It finally happened!", e);
        } catch (MissingResourceException e) {
            // No resource is available - do not append revision number
        }
        VERSION_STRING = versionString.toString();
        REVISION_NUMBER = rev;
    }

    private RojacUtils() {
    }

    @SuppressWarnings({"unchecked"})
    public static <T> List<T> getRSDNObject(Collection<? extends IRSDNable<T>> col, Class<T> c) {
        List<T> res = new ArrayList<>(col.size());

        for (IRSDNable<T> o : col) {
            res.add(o.getRSDNObject());
        }

        return res;
    }

    @SuppressWarnings({"unchecked"})
    public static <T> T[] getRSDNObject(IRSDNable<T>... ar) {
        Class<T> c = getGenericClass(ar.getClass().getComponentType());

        List<T> res = new ArrayList<>(ar.length);

        for (IRSDNable<T> o : ar) {
            res.add(o.getRSDNObject());
        }

        T[] a = (T[]) Array.newInstance(c, ar.length);
        return res.toArray(a);
    }

    @SuppressWarnings({"unchecked"})
    public static <T> Class<T> getGenericClass(Class<?> type) {
        return (Class<T>) ((ParameterizedType) type.getGenericInterfaces()[0]).getActualTypeArguments()[0];
    }


    public static String constructDebugSQL(String sql, Object... parameters) {
        String query = sql;

        for (Object value : parameters) {
            String str;
            if (value instanceof String) {
                str = "'" + Matcher.quoteReplacement(value.toString()) + "'";
            } else {
                str = Matcher.quoteReplacement(value.toString());
            }
            query = query.replaceFirst("\\?", str);
        }

        return query;
    }

    /*
    * Util methods for converting values.
    */

    /**
     * Returns a list of available locales for specified bundle.
     *
     * @param bundle base name of bundle with / as separators and without extension.
     * @return an array of available locales for the specified bundle.
     * @throws IOException if bundle list can not be obtained.
     */
    public static Locale[] localesForBundle(String bundle) throws IOException {
        return localesForBundle(bundle, false);
    }

    /**
     * Returns a list of available locales for specified bundle.
     *
     * @param bundle     base name of bundle with / as separators and without extension.
     * @param addDefault flag to specify is the default resource bundle should be associated with default locale.
     * @return an array of available locales for the specified bundle.
     * @throws IOException if bundle list can not be obtained.
     */
    public static Locale[] localesForBundle(String bundle, boolean addDefault) throws IOException {
        if (bundle == null) {
            throw new NullPointerException("Empty bundle name");
        }

        // Prepare bundle string for processing.
        while (bundle.startsWith("/")) {
            bundle = bundle.substring(1);
        }

        while (bundle.endsWith("/")) {
            bundle = bundle.substring(0, bundle.length() - 1);
        }

        if (bundle.length() == 0) {
            throw new IllegalArgumentException("Empty bundle name.");
        }

        String bundleName;

        int pos = bundle.lastIndexOf('/');
        if (pos == -1) {
            bundleName = bundle;
        } else {
            bundleName = bundle.substring(pos + 1);
        }

        Collection<Locale> locales = new HashSet<>();

        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();

        Resource[] resources = resolver.getResources("/" + bundle + "*.properties");
        Pattern localePattern = Pattern.compile(Pattern.quote(bundleName) + "(?:_(\\w{2})(?:_(\\w{2}))?)?\\.properties");
        for (Resource resource : resources) {
            Matcher m = localePattern.matcher(resource.getFilename());
            if (m.matches()) {
                if (m.group(1) == null) {
                    if (addDefault) {
                        locales.add(Locale.getDefault());
                    }
                } else if (m.group(2) == null) {
                    locales.add(new Locale(m.group(1)));
                } else {
                    locales.add(new Locale(m.group(1), m.group(2)));
                }
            } else {
                System.out.println("Something wrong.");
            }
        }

        return locales.toArray(new Locale[locales.size()]);
    }

    /**
     * Checks if a newer version of rojac is exist on google code server.
     *
     * @return {@code null} if no info should be displayed and integer (revision number) if revision is obtained.
     * @throws RojacException thrown if version can not be obtained.
     */
    public static Integer getLastBuild() throws RojacException {
        CheckUpdatesEnum period = Property.UPDATER_PERIOD.get();
        try {
            if (period.shouldCheck(Property.UPDATER_LAST_CHECK.get())) {
                // Load revision info.

                URL updateLink = new URL("http://rojac.googlecode.com/files/last-revision.txt");
                BufferedReader r = new BufferedReader(new InputStreamReader(updateLink.openStream()));

                String revision = r.readLine();

                r.close();

                return Integer.parseInt(revision.trim());
            }
        } catch (IOException e) {
            throw new RojacException("Can not read revision number.", e);
        } catch (NumberFormatException e) {
            throw new RojacException("Revision is not number.", e);
        }

        return null;
    }

    /**
     * Util class for checking if the method executed in SwingThread or not.
     *
     * @param swing control flag. If set to {@code true} the current method will check if it is executing in the
     *              EventDispatching thread. If set to {@code false} - in the non-EventDispatching thread. If
     *              condition is failed a message will be added into logs.
     * @return true if the method invoked in expected thread
     */
    public static boolean checkThread(boolean swing) {
        return checkThread(swing, RojacUtils.class);
    }

    /**
     * Util class for checking if the method executed in SwingThread or not.
     *
     * @param swing     control flag. If set to {@code true} the current method will check if it is executing in
     *                  the EventDispatching thread. If set to {@code false} - in the non-EventDispatching thread.
     *                  If condition is failed a message will be added into logs.
     * @param tillClass class object till which a stack trace should be trimmed.
     * @return true if the method invoked in expected thread
     */
    @SuppressWarnings({"ThrowableInstanceNeverThrown"})
    public static boolean checkThread(boolean swing, Class<?> tillClass) {
        boolean wrongThread = swing != EventQueue.isDispatchThread();
        if (wrongThread) {
            Throwable stack = new Throwable("Stack trace");
            StackTraceElement[] stackTrace = stack.getStackTrace();

            if (tillClass != null) {
                int shift = 0;
                while (!tillClass.getName().equals(stackTrace[shift].getClassName())) {
                    shift++;
                }

                while (tillClass.getName().equals(stackTrace[shift].getClassName())) {
                    shift++;
                }

                if (shift != stackTrace.length) {
                    stack.setStackTrace(ArrayUtils.subarray(stackTrace, shift, stackTrace.length - shift));
                }
            }

            if (log.isWarnEnabled()) {
                log.warn(
                        swing ?
                                "The method is not executed in EventQueue!" :
                                "The method is executed in EventQueue!",
                        stack
                );
            }
        }

        return !wrongThread;
    }

    public static void showExceptionDialog(Throwable e) {
        showExceptionDialog(Thread.currentThread(), e);
    }

    public static void showExceptionDialog(Thread t, Throwable e) {

        GLOBAL_EXCEPTION_HANDLER.uncaughtException(t, e);
    }

    public static File getRojacHome() {
        String userHome = SystemUtils.USER_HOME;
        if (userHome == null) {
            log.error("Can not find user home directory");
            return null;
        }

        File rojacHome = new File(userHome, ".rojac");
        // Create home directory if it not exists yet.
        if (rojacHome.isDirectory() || rojacHome.mkdirs()) {
            return rojacHome;
        }

        log.warn("Can not initialize Rojac home directory");
        return null;
    }

    public static File getLayoutFile() {
        return new File(getRojacHome(), "layout.settings");
    }

    /**
     * Fires a debug exception only if Rojac run in debug mode.
     *
     * @param message message of debug exception.
     * @throws RojacDebugException risen debug exception with correspond message
     */
    public static void fireDebugException(String message) throws RojacDebugException {
        if (!Property.ROJAC_DEBUG_MODE.get()) {
            return;
        }

        throw new RojacDebugException(message);
    }

    /**
     * Fires a debug exception only if Rojac run in debug mode.
     *
     * @param message message of debug exception.
     * @param cause   root cause of exception.
     * @throws RojacDebugException risen debug exception with correspond message and root cause
     */
    public static void fireDebugException(String message, Throwable cause) throws RojacDebugException {
        if (!Property.ROJAC_DEBUG_MODE.get()) {
            return;
        }

        throw new RojacDebugException(message, cause);
    }

    public static File getSettingsFile() {
        return new File(getRojacHome(), "config.properties");
    }

    public static File getKeyMapFile() {
        return new File(getRojacHome(), "keymap.properties");
    }

    /**
     * Hack to avoid noisily warnings during compiling project.
     *
     * @param executor executor to register
     */
    public static void registerExecutor(IExecutor executor) {
        // The code is doing the line:
        // AppContext.getAppContext().put(SwingWorker.class, executor);
        try {
            Class<?> aClass = Class.forName("sun.awt.AppContext");
            Method putMethod = aClass.getMethod("put", Object.class, Object.class);
            Method getInstanceMethod = aClass.getMethod("getAppContext");

            Object instance = getInstanceMethod.invoke(null);

            putMethod.invoke(instance, SwingWorker.class, executor);
        } catch (ClassNotFoundException e) {
            throw new RojacDebugException("sun.awt.AppContext is no longer exist", e);
        } catch (NoSuchMethodException e) {
            throw new RojacDebugException("Method not found for class", e);
        } catch (IllegalAccessException e) {
            throw new RojacDebugException("Can not access to a method", e);
        } catch (InvocationTargetException e) {
            throw new RojacDebugException("Method somehow thrown an exception", e);
        }
    }

    public static void storeSettings() {
        ShortCutUtils.storeShortCuts();

        Password password = null;
        if (!RSDN_USER_PASSWORD_SAVE.get()) {
            password = RSDN_USER_PASSWORD.clear();
        }

        Property.getOptionsService().storeSettings();

        if (password != null) {
            RSDN_USER_PASSWORD.set(password);
        }
    }

    /**
     * Returns true if the method is invoked from 'main' Java thread.
     *
     * @return true if the method is invoked from 'main' thread.
     */
    public static boolean isMainThread() {
        return "main".equals(Thread.currentThread().getName());
    }

    public static Properties loadCoreOptions() throws RojacInitializationException {
        Properties mainProperties;
        try {
            mainProperties = ResourceUtils.loadProperties("/rojac.properties");
        } catch (IOException e) {
            throw new RojacInitializationException("rojac.properties was not found in class path", e);
        }

        String home = System.getProperty("rojac.home");
        if (StringUtils.isBlank(home)) {
            String userHome = mainProperties.getProperty("rojac.home");
            if (StringUtils.isBlank(userHome)) {
                throw new RojacInitializationException(
                        "{$rojac.home} is not defined either property in file or system property."
                );
            }

            home = ResourceUtils.putSystemProperties(userHome);
            if (log.isTraceEnabled()) {
                log.trace("{$rojac.home} is not defined. It will initialized with '" + home + "' value.");
            }
        }

        String dbHome = mainProperties.getProperty("rojac.db.home");
        if (StringUtils.isBlank(dbHome)) {
            if (log.isWarnEnabled()) {
                log.warn("{$rojac.db.home} is not defined. Assumed the same as {$rojac.home}");
            }
            mainProperties.setProperty("rojac.db.home", home);
        }
        installProperties(
                mainProperties,
                "rojac.home",
                "rojac.db.home",
                "rojac.db.host",
                "rojac.db.user",
                "rojac.db.password"
        );

        checkPath(mainProperties.getProperty("rojac.home"));
        checkPath(mainProperties.getProperty("rojac.db.home"));
        return mainProperties;
    }

    /**
     * Register specified properties in system.
     *
     * @param properties
     * @param names
     */
    public static void installProperties(Properties properties, String... names) {
        for (String name : names) {
            String value = properties.getProperty(name);
            if (StringUtils.isBlank(value)) {
                if (log.isTraceEnabled()) {
                    log.trace("Property " + name + " is not defined.");
                }
                continue;
            }

            value = ResourceUtils.putSystemProperties(value);

            if (log.isTraceEnabled()) {
                log.trace("Initialize property " + name + " with value " + value);
            }

            System.setProperty(name, value);
            properties.setProperty(name, value);
        }
    }

    public static void checkPath(String target) throws RojacInitializationException {
        File folder = new File(target);
        if (!folder.exists()) {
            if (log.isTraceEnabled()) {
                log.trace("Create folder at " + target);
            }
            if (!folder.mkdirs()) {
                throw new RojacInitializationException("Can not create a '" + folder.getAbsolutePath() + "' folder for storing Rojac configuration.");
            }
        }
        if (!folder.isDirectory()) {
            throw new RojacInitializationException("Target path '" + folder.getAbsolutePath() + "' is not a folder.");
        }
    }

    public static String generateHash() {
        SecureRandom rnd = new SecureRandom();
        byte[] str = new byte[4];
        rnd.nextBytes(str);
        return String.format("%h", new Object[]{str});
    }

    public static ArrayOfInt toIntArray(int[] messageIds) {
        ArrayOfInt arrayOfInt = new ArrayOfInt();
        List<Integer> list = arrayOfInt.getInt();
        for (int id : messageIds) {
            list.add(id);
        }
        return arrayOfInt;
    }

    private static class GlobalExceptionHandler implements Thread.UncaughtExceptionHandler {
        @Override
        public void uncaughtException(final Thread t, final Throwable e) {
            log.error("Got unhandled exception in " + t, e);

            if (EventQueue.isDispatchThread()) {
                DialogHelper.showExceptionDialog(t, e);
            } else {
                SwingUtilities.invokeLater(
                        () -> DialogHelper.showExceptionDialog(t, e)
                );
            }
        }
    }
}
