package org.xblackcat.sunaj.service.options;

import junit.framework.TestCase;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * Date: 13 квіт 2007
 *
 * @author Alexey
 */

public class OptionsServiceTest extends TestCase {
    private static final Property<String> TEST_PROPERTY_STRING = new Property("sunaj.test.string", String.class);
    private static final Property<Boolean> TEST_PROPERTY_BOOLEAN = new Property("sunaj.test.boolean", Boolean.class);
    private static final Property<Character> TEST_PROPERTY_CHAR = new Property("sunaj.test.char", Character.class);
    private static final Property<Byte> TEST_PROPERTY_BYTE = new Property("sunaj.test.byte", Byte.class);
    private static final Property<Short> TEST_PROPERTY_SHORT = new Property("sunaj.test.short", Short.class);
    private static final Property<Integer> TEST_PROPERTY_INT = new Property("sunaj.test.int", Integer.class);
    private static final Property<Long> TEST_PROPERTY_LONG = new Property("sunaj.test.long", Long.class);
    private static final Property<Double> TEST_PROPERTY_DOUBLE = new Property("sunaj.test.double", Double.class);
    private static final Property<Float> TEST_PROPERTY_FLOAT = new Property("sunaj.test.float", Float.class);
    private static final Property<TestEnum> TEST_PROPERTY_ENUM = new Property("sunaj.test.enum", TestEnum.class);

    private IOptionsService s;

    protected void setUp() throws Exception {
        s = new TestOptionsService();
    }

    public void testStringValueProperties() {
        String value = "test string";

        assertEquals(null, s.setProperty(TEST_PROPERTY_STRING, value));

        assertEquals(value, s.getProperty(TEST_PROPERTY_STRING));

        assertEquals(value, s.setProperty(TEST_PROPERTY_STRING, "new string"));
    }

    public void testBooleanValueProperties() {
        Boolean value = Boolean.TRUE;

        assertEquals(null, s.setProperty(TEST_PROPERTY_BOOLEAN, value));

        assertEquals(value, s.getProperty(TEST_PROPERTY_BOOLEAN));

        assertEquals(value, s.setProperty(TEST_PROPERTY_BOOLEAN, Boolean.FALSE));
    }

    public void testCharValueProperties() {
        Character value = '0';

        assertEquals(null, s.setProperty(TEST_PROPERTY_CHAR, value));

        assertEquals(value, s.getProperty(TEST_PROPERTY_CHAR));

        assertEquals(value, s.setProperty(TEST_PROPERTY_CHAR, '1'));
    }

    public void testByteValueProperties() {
        Byte value = 10;

        assertEquals(null, s.setProperty(TEST_PROPERTY_BYTE, value));

        assertEquals(value, s.getProperty(TEST_PROPERTY_BYTE));

        assertEquals(value, s.setProperty(TEST_PROPERTY_BYTE, (byte) 30));
    }

    public void testShortValueProperties() {
        Short value = 10;

        assertEquals(null, s.setProperty(TEST_PROPERTY_SHORT, value));

        assertEquals(value, s.getProperty(TEST_PROPERTY_SHORT));

        assertEquals(value, s.setProperty(TEST_PROPERTY_SHORT, (short) 30));
    }

    public void testIntValueProperties() {
        Integer value = 10;

        assertEquals(null, s.setProperty(TEST_PROPERTY_INT, value));

        assertEquals(value, s.getProperty(TEST_PROPERTY_INT));

        assertEquals(value, s.setProperty(TEST_PROPERTY_INT, 30));
    }

    public void testLongValueProperties() {
        Long value = 10l;

        assertEquals(null, s.setProperty(TEST_PROPERTY_LONG, value));

        assertEquals(value, s.getProperty(TEST_PROPERTY_LONG));

        assertEquals(value, s.setProperty(TEST_PROPERTY_LONG, 30l));
    }

    public void testDoubleValueProperties() {
        Double value = 10.0000000000003;

        assertEquals(null, s.setProperty(TEST_PROPERTY_DOUBLE, value));

        assertEquals(value, s.getProperty(TEST_PROPERTY_DOUBLE));

        assertEquals(value, s.setProperty(TEST_PROPERTY_DOUBLE, -2343.4323));
    }

    public void testFloatValueProperties() {
        Float value = 104.3f;

        assertEquals(null, s.setProperty(TEST_PROPERTY_FLOAT, value));

        assertEquals(value, s.getProperty(TEST_PROPERTY_FLOAT));

        assertEquals(value, s.setProperty(TEST_PROPERTY_FLOAT, 0.234f));
    }

    public void testEnumValueProperties() {
        TestEnum value = TestEnum.ONE;

        assertEquals(null, s.setProperty(TEST_PROPERTY_ENUM, value));

        assertEquals(value, s.getProperty(TEST_PROPERTY_ENUM));

        assertEquals(value, s.setProperty(TEST_PROPERTY_ENUM, TestEnum.THREE));
    }

    private static final class TestOptionsService extends AnOptionsService {
        private static final Log log = LogFactory.getLog(TestOptionsService.class);

        private Map<String, String> cache = new HashMap<String, String>();

        protected String getProperty(String key) {
            String s = cache.get(key);
            if (log.isDebugEnabled()) {
                log.debug("Value for '" + key + "' is '" + s + '\'');
            }
            return s;
        }

        protected String setProperty(String key, String value) {
            if (log.isDebugEnabled()) {
                log.debug("Set value for '" + key + "': '" + value + '\'');
            }
            String s = cache.put(key, value);
            if (log.isDebugEnabled()) {
                log.debug("Old value for '" + key + "' is '" + s + '\'');
            }
            return s;
        }
    }

    private static enum TestEnum {
        ONE,
        TWO,
        THREE
    }
}
