package org.xblackcat.rojac.util;

import org.xblackcat.rojac.data.IRSDNable;
import org.xblackcat.utils.ResourceUtils;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.io.StringReader;
import java.lang.reflect.Array;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;
import java.util.MissingResourceException;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author xBlackCat
 */

public final class RojacUtils {
    public static final String VERSION = "0.1a";
    public static final String VERSION_STRING;

    static {
        StringBuilder versionString = new StringBuilder("Rojac v");
        versionString.append(VERSION);

        try {
            Pattern propNamePattern = Pattern.compile("([\\w\\s]+): (.+)");

            BufferedReader revData = new BufferedReader(new InputStreamReader(ResourceUtils.getResourceAsStream("config/rojac.revision")));
            //Prepare content to be read.
            StringBuilder newContent = new StringBuilder();
            String s;
            while ((s = revData.readLine()) != null) {
                Matcher m = propNamePattern.matcher(s);
                if (m.matches()) {
                    newContent.append(m.group(1).replace(' ', '-'));
                    newContent.append('=');
                    newContent.append(m.group(2));
                    newContent.append('\n');
                }
            }

            Properties revInfo = new Properties();
            revInfo.load(new StringReader(newContent.toString()));

            // Remember - we are working only with build.xml revision!
            String revNum = revInfo.getProperty("Revision");
            String path = revInfo.getProperty("Repository-Root");
            String file = revInfo.getProperty("URL");

            // Now fill additional info

            versionString.append(" (rev. ");
            versionString.append(revNum);

            if (file.startsWith(path)) {
                int pos = path.length() + 1;
                int nextPos = file.indexOf('/', pos);

                String branch = file.substring(pos, nextPos);
                if ("trunk".equals(branch)) {
                    // No additional info will be added
                } else if ("branches".equals(branch)) {
                    // Read branch name
                    pos = nextPos + 1;
                    nextPos = file.indexOf('/', pos);

                    versionString.append(" [");
                    versionString.append(file.substring(pos, nextPos));
                    versionString.append(']');
                }
            }
            versionString.append(')');
        } catch (IOException e) {
            throw new RuntimeException("It finally happened!", e);
        } catch (MissingResourceException e) {
            // No resource is available - do not append revision number
        }
        VERSION_STRING = versionString.toString();

    }

    private RojacUtils() {
    }

    public static <T extends Serializable> T[] getRSDNObject(IRSDNable<T>[] ar) {
        Class<T> c = (Class<T>) ((ParameterizedType) ar.getClass().getComponentType().getGenericInterfaces()[0]).getActualTypeArguments()[0];

        List<T> res = new ArrayList<T>(ar.length);

        for (IRSDNable<T> o : ar) {
            res.add(o.getRSDNObject());
        }

        T[] a = (T[]) Array.newInstance(c, ar.length);
        return res.toArray(a);
    }

    public static String construstDebugSQL(String sql, Object... params) {
        String query = sql;

        for (Object o : params) {
            String str;
            if (o instanceof String) {
                str = "'" + Matcher.quoteReplacement(o.toString()) + "'";
            } else {
                str = Matcher.quoteReplacement(o.toString());
            }
            query = query.replaceFirst("\\?", str);
        }

        return query;
    }

    /*
    * Util methods for converting values.
    */
    public static <T extends Enum<T>> T convertToEnum(Class<T> enumClass, String val) {
        if (val != null) {
            return Enum.valueOf(enumClass, val);
        } else {
            return null;
        }
    }

    public static void setLookAndFeel(LookAndFeel laf) throws UnsupportedLookAndFeelException {
        UIManager.setLookAndFeel(laf);
    }

//    public static <T, E> T[] extract(E[] arr, IExtractor<T, E> e) {
//
//    }
}
