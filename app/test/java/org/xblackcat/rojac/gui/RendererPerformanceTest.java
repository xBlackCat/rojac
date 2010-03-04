package org.xblackcat.rojac.gui;

import junit.framework.TestCase;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * @author xBlackCat
 */
public class RendererPerformanceTest extends TestCase {
    public void testPerformance() {
        int iterations = 10000;

        Obj o = new Obj();

        ObjectHolder oh = null;
        int iter = 0;
        long start = System.currentTimeMillis();
        for (int i = 0; i < iterations; i++) {
            oh = new ObjectHolder(o);
            iter++;
            oh.getO().getField();
        }
        System.out.println("Create instance indirect took " + (System.currentTimeMillis() - start) + " ms.");

        iter = 0;
        Class<ObjectHolder> aClass = ObjectHolder.class;
        start = System.currentTimeMillis();
        for (int i = 0; i < iterations; i++) {
            try {
                Constructor<ObjectHolder> constructor = aClass.getConstructor(Obj.class);
                oh = constructor.newInstance(o);
            } catch (NoSuchMethodException e) {
            } catch (InstantiationException e) {
            } catch (IllegalAccessException e) {
            } catch (InvocationTargetException e) {
            }
            iter++;
            oh.getO().getField();
        }
        System.out.println("Create instance via reflection took " + (System.currentTimeMillis() - start) + " ms.");

        start = System.currentTimeMillis();
        try {
            Constructor<ObjectHolder> constructor = aClass.getConstructor(Obj.class);
            for (int i = 0; i < iterations; i++) {
                oh = constructor.newInstance(o);
                iter++;
                oh.getO().getField();
            }
        } catch (NoSuchMethodException e) {
        } catch (InstantiationException e) {
        } catch (IllegalAccessException e) {
        } catch (InvocationTargetException e) {
        }
        System.out.println("Create instance via reflection #2 took " + (System.currentTimeMillis() - start) + " ms.");

    }

}
