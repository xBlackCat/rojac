package org.xblackcat.rojac.service.converter.tag;
/**
 *
 * @author xBlackCat
 */


import junit.framework.TestCase;
import org.xblackcat.rojac.service.converter.ITag;

public class LanguageConverterTest extends TestCase {
    public void testSimpleConvert() throws Exception {
        ITag lc = new LanguageTag("test", null, null, null, null, null, (String) null);

        {
            String input = "Test:[test]UNICODE_STRING interfaceName, dosName;\n\n// ����������� ����������\nstatus = IoRegisterDeviceInterface(\n  pdo,\n  &GUID_MY_USB_DEVICE,\n  0,\n  &interfaceName\n  );\n\n// ������� ���������� ������\nRtlInitUnicodeString(&dosName, L\"\\\\DosDevices\\\\MyUsbDevice\");\nIoCreateSymbolicLink(&dosName, &interfaceName);\n[/test]\nok";
            String output = "Test:<table width='96%'><tr><td nowrap='nowrap' class='c'><pre>UNICODE_STRING interfaceName, dosName;\n\n// ����������� ����������\nstatus = IoRegisterDeviceInterface(\n  pdo,\n  &GUID_MY_USB_DEVICE,\n  0,\n  &interfaceName\n  );\n\n// ������� ���������� ������\nRtlInitUnicodeString(&dosName, L\"\\\\DosDevices\\\\MyUsbDevice\");\nIoCreateSymbolicLink(&dosName, &interfaceName);\n</pre></td></tr></table>\nok";

            assertEquals(output, TestUtils.applyTag(TestUtils.applyTag(input, lc), lc));
        }

        // Multiple zones
        {
            String input = "[test]UNICODE_STRING interfaceName, dosName;[/test]\n\n// ����������� ����������\n[test]status = IoRegisterDeviceInterface(\n  pdo,\n  &GUID_MY_USB_DEVICE,\n  0,\n  &interfaceName\n  );[/test]\n\n// ������� ���������� ������\n[test]RtlInitUnicodeString(&dosName, L\"\\\\DosDevices\\\\MyUsbDevice\");\nIoCreateSymbolicLink(&dosName, &interfaceName);\n[/test]";
            String output = "<table width='96%'><tr><td nowrap='nowrap' class='c'><pre>UNICODE_STRING interfaceName, dosName;</pre></td></tr></table>\n\n// ����������� ����������\n<table width='96%'><tr><td nowrap='nowrap' class='c'><pre>status = IoRegisterDeviceInterface(\n  pdo,\n  &GUID_MY_USB_DEVICE,\n  0,\n  &interfaceName\n  );</pre></td></tr></table>\n\n// ������� ���������� ������\n<table width='96%'><tr><td nowrap='nowrap' class='c'><pre>RtlInitUnicodeString(&dosName, L\"\\\\DosDevices\\\\MyUsbDevice\");\nIoCreateSymbolicLink(&dosName, &interfaceName);\n</pre></td></tr></table>";

            assertEquals(output, TestUtils.applyTag(TestUtils.applyTag(TestUtils.applyTag(input, lc), lc), lc));
        }
    }

    public void testResWordsConvert() throws Exception {
        ITag lc = new LanguageTag(
                "test",
                null,
                null,
                null,
                null,
                null,
                "one",
                "two",
                "three");

        {
            String input = "[test]Something one;\nSomething two or three;\nAnother ones are two-heads.\n[/test]";
            String output = "<table width='96%'><tr><td nowrap='nowrap' class='c'><pre>Something <span class='kw'>one</span>;\nSomething <span class='kw'>two</span> or <span class='kw'>three</span>;\nAnother ones are <span class='kw'>two</span>-heads.\n</pre></td></tr></table>";

            assertEquals(output, TestUtils.applyTag(input, lc));
        }

        // Multiple zones
        {
            String input = "[test]Something one;[/test]\nSomething two or three;\n[test]\nAnother ones are two-heads.\n[/test]";
            String output = "<table width='96%'><tr><td nowrap='nowrap' class='c'><pre>Something <span class='kw'>one</span>;</pre></td></tr></table>\nSomething two or three;\n<table width='96%'><tr><td nowrap='nowrap' class='c'><pre>\nAnother ones are <span class='kw'>two</span>-heads.\n</pre></td></tr></table>";

            assertEquals(output, TestUtils.applyTag(TestUtils.applyTag(input, lc), lc));
        }

    }

    public void testComplexConvert() throws Exception {
        ITag lc = new LanguageTag(
                "test",
                "//",       // Line comment prefix
                "/*", "*/", // Quotes of block comment
                "\"'",      // String quotes
                '\\',        // Escape charaster
                "class",
                "word");

        {
            String input = "Program code:\n[test]\nclass: reserved word\n// Reserved word in comment: class\nprint \"this is a class\"; // Print class\n/*\n* end of class\n*/\nprint \"this class is named \\\"class\\\"!\";\n[/test]\nFinish.";
            String output = "Program code:\n<table width='96%'><tr><td nowrap='nowrap' class='c'><pre>\n<span class='kw'>class</span>: reserved <span class='kw'>word</span>\n<span class='com'>// Reserved word in comment: class</span>\nprint <span class='str'>\"this is a class\"</span>; <span class='com'>// Print class</span>\n<span class='com'>/*\n* end of class\n*/</span>\nprint <span class='str'>\"this class is named \\\"class\\\"!\"</span>;\n</pre></td></tr></table>\nFinish.";

            assertEquals(output, TestUtils.applyTag(input, lc));
        }

    }
}
