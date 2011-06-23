package org.xblackcat.rojac.i18n;

import javax.swing.*;
import java.awt.*;

/**
 * @author xBlackCat
 */

public final class JLOptionPane {
    private JLOptionPane() {
    }

    /**
     * Shows a question-message dialog requesting input from the user. The dialog uses the default frame, which usually
     * means it is centered on the screen.
     *
     * @param message the <code>Object</code> to display
     *
     * @throws java.awt.HeadlessException if <code>GraphicsEnvironment.isHeadless</code> returns <code>true</code>
     * @see java.awt.GraphicsEnvironment#isHeadless
     */
    public static String showInputDialog(Object message)
            throws HeadlessException {
        return showInputDialog(null, message);
    }

    /**
     * Shows a question-message dialog requesting input from the user, with the input value initialized to
     * <code>initialSelectionValue</code>. The dialog uses the default frame, which usually means it is centered on the
     * screen.
     *
     * @param message               the <code>Object</code> to display
     * @param initialSelectionValue the value used to initialize the input field
     *
     * @since 1.4
     */
    public static String showInputDialog(Object message, Object initialSelectionValue) {
        return showInputDialog(null, message, initialSelectionValue);
    }

    /**
     * Shows a question-message dialog requesting input from the user parented to <code>parentComponent</code>. The
     * dialog is displayed on top of the <code>Component</code>'s frame, and is usually positioned below the
     * <code>Component</code>.
     *
     * @param parentComponent the parent <code>Component</code> for the dialog
     * @param message         the <code>Object</code> to display
     *
     * @throws HeadlessException if <code>GraphicsEnvironment.isHeadless</code> returns <code>true</code>
     * @see java.awt.GraphicsEnvironment#isHeadless
     */
    public static String showInputDialog(Component parentComponent,
                                         Object message) throws HeadlessException {
        return showInputDialog(parentComponent,
                message,
                UIManager.getString("OptionPane.inputDialogTitle", LocaleControl.getInstance().getLocale()),
                JOptionPane.QUESTION_MESSAGE);
    }

    /**
     * Shows a question-message dialog requesting input from the user and parented to <code>parentComponent</code>. The
     * input value will be initialized to <code>initialSelectionValue</code>. The dialog is displayed on top of the
     * <code>Component</code>'s frame, and is usually positioned below the <code>Component</code>.
     *
     * @param parentComponent       the parent <code>Component</code> for the dialog
     * @param message               the <code>Object</code> to display
     * @param initialSelectionValue the value used to initialize the input field
     *
     * @since 1.4
     */
    public static String showInputDialog(Component parentComponent, Object message,
                                         Object initialSelectionValue) {
        return (String) showInputDialog(parentComponent,
                message,
                UIManager.getString("OptionPane.inputDialogTitle", LocaleControl.getInstance().getLocale()),
                JOptionPane.QUESTION_MESSAGE,
                null,
                null,
                initialSelectionValue);
    }

    /**
     * Shows a dialog requesting input from the user parented to <code>parentComponent</code> with the dialog having the
     * title <code>title</code> and message type <code>messageType</code>.
     *
     * @param parentComponent the parent <code>Component</code> for the dialog
     * @param message         the <code>Object</code> to display
     * @param title           the <code>String</code> to display in the dialog title bar
     * @param messageType     the type of message that is to be displayed: <code>ERROR_MESSAGE</code>,
     *                        <code>INFORMATION_MESSAGE</code>, <code>WARNING_MESSAGE</code>,
     *                        <code>QUESTION_MESSAGE</code>, or <code>PLAIN_MESSAGE</code>
     *
     * @throws HeadlessException if <code>GraphicsEnvironment.isHeadless</code> returns <code>true</code>
     * @see java.awt.GraphicsEnvironment#isHeadless
     */
    public static String showInputDialog(Component parentComponent,
                                         Object message, String title, int messageType)
            throws HeadlessException {
        return (String) showInputDialog(parentComponent, message, title, messageType, null, null, null);
    }

    /**
     * Brings up an information-message dialog titled "Message".
     *
     * @param parentComponent determines the <code>Frame</code> in which the dialog is displayed; if <code>null</code>,
     *                        or if the <code>parentComponent</code> has no <code>Frame</code>, a default
     *                        <code>Frame</code> is used
     * @param message         the <code>Object</code> to display
     *
     * @throws HeadlessException if <code>GraphicsEnvironment.isHeadless</code> returns <code>true</code>
     * @see java.awt.GraphicsEnvironment#isHeadless
     */
    public static void showMessageDialog(Component parentComponent,
                                         Object message) throws HeadlessException {
        showMessageDialog(parentComponent, message,
                UIManager.getString("OptionPane.messageDialogTitle", LocaleControl.getInstance().getLocale()),
                JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Brings up a dialog that displays a message using a default icon determined by the <code>messageType</code>
     * parameter.
     *
     * @param parentComponent determines the <code>Frame</code> in which the dialog is displayed; if <code>null</code>,
     *                        or if the <code>parentComponent</code> has no <code>Frame</code>, a default
     *                        <code>Frame</code> is used
     * @param message         the <code>Object</code> to display
     * @param title           the title string for the dialog
     * @param messageType     the type of message to be displayed: <code>ERROR_MESSAGE</code>,
     *                        <code>INFORMATION_MESSAGE</code>, <code>WARNING_MESSAGE</code>,
     *                        <code>QUESTION_MESSAGE</code>, or <code>PLAIN_MESSAGE</code>
     *
     * @throws HeadlessException if <code>GraphicsEnvironment.isHeadless</code> returns <code>true</code>
     * @see java.awt.GraphicsEnvironment#isHeadless
     */
    public static void showMessageDialog(Component parentComponent,
                                         Object message, String title, int messageType)
            throws HeadlessException {
        showMessageDialog(parentComponent, message, title, messageType, null);
    }

    /**
     * Brings up a dialog displaying a message, specifying all parameters.
     *
     * @param parentComponent determines the <code>Frame</code> in which the dialog is displayed; if <code>null</code>,
     *                        or if the <code>parentComponent</code> has no <code>Frame</code>, a default
     *                        <code>Frame</code> is used
     * @param message         the <code>Object</code> to display
     * @param title           the title string for the dialog
     * @param messageType     the type of message to be displayed: <code>ERROR_MESSAGE</code>,
     *                        <code>INFORMATION_MESSAGE</code>, <code>WARNING_MESSAGE</code>,
     *                        <code>QUESTION_MESSAGE</code>, or <code>PLAIN_MESSAGE</code>
     * @param icon            an icon to display in the dialog that helps the user identify the kind of message that is
     *                        being displayed
     *
     * @throws HeadlessException if <code>GraphicsEnvironment.isHeadless</code> returns <code>true</code>
     * @see java.awt.GraphicsEnvironment#isHeadless
     */
    public static void showMessageDialog(Component parentComponent,
                                         Object message, String title, int messageType, Icon icon)
            throws HeadlessException {
        showOptionDialog(parentComponent,
                message,
                title,
                JOptionPane.DEFAULT_OPTION,
                messageType,
                icon,
                null,
                null);
    }

    /**
     * Brings up a dialog with the options <i>Yes</i>, <i>No</i> and <i>Cancel</i>; with the title, <b>Select an
     * Option</b>.
     *
     * @param parentComponent determines the <code>Frame</code> in which the dialog is displayed; if <code>null</code>,
     *                        or if the <code>parentComponent</code> has no <code>Frame</code>, a default
     *                        <code>Frame</code> is used
     * @param message         the <code>Object</code> to display
     *
     * @return an integer indicating the option selected by the user
     *
     * @throws HeadlessException if <code>GraphicsEnvironment.isHeadless</code> returns <code>true</code>
     * @see java.awt.GraphicsEnvironment#isHeadless
     */
    public static int showConfirmDialog(Component parentComponent,
                                        Object message) throws HeadlessException {
        return showConfirmDialog(parentComponent, message,
                UIManager.getString("OptionPane.titleText"),
                JOptionPane.YES_NO_CANCEL_OPTION);
    }

    /**
     * Brings up a dialog where the number of choices is determined by the <code>optionType</code> parameter.
     *
     * @param parentComponent determines the <code>Frame</code> in which the dialog is displayed; if <code>null</code>,
     *                        or if the <code>parentComponent</code> has no <code>Frame</code>, a default
     *                        <code>Frame</code> is used
     * @param message         the <code>Object</code> to display
     * @param title           the title string for the dialog
     * @param optionType      an int designating the options available on the dialog: <code>YES_NO_OPTION</code>,
     *                        <code>YES_NO_CANCEL_OPTION</code>, or <code>OK_CANCEL_OPTION</code>
     *
     * @return an int indicating the option selected by the user
     *
     * @throws HeadlessException if <code>GraphicsEnvironment.isHeadless</code> returns <code>true</code>
     * @see java.awt.GraphicsEnvironment#isHeadless
     */
    public static int showConfirmDialog(Component parentComponent,
                                        Object message, String title, int optionType)
            throws HeadlessException {
        return showConfirmDialog(parentComponent, message, title, optionType,
                JOptionPane.QUESTION_MESSAGE);
    }

    /**
     * Brings up a dialog where the number of choices is determined by the <code>optionType</code> parameter, where the
     * <code>messageType</code> parameter determines the icon to display. The <code>messageType</code> parameter is
     * primarily used to supply a default icon from the Look and Feel.
     *
     * @param parentComponent determines the <code>Frame</code> in which the dialog is displayed; if <code>null</code>,
     *                        or if the <code>parentComponent</code> has no <code>Frame</code>, a default
     *                        <code>Frame</code> is used.
     * @param message         the <code>Object</code> to display
     * @param title           the title string for the dialog
     * @param optionType      an integer designating the options available on the dialog: <code>YES_NO_OPTION</code>,
     *                        <code>YES_NO_CANCEL_OPTION</code>, or <code>OK_CANCEL_OPTION</code>
     * @param messageType     an integer designating the kind of message this is; primarily used to determine the icon
     *                        from the pluggable Look and Feel: <code>ERROR_MESSAGE</code>,
     *                        <code>INFORMATION_MESSAGE</code>, <code>WARNING_MESSAGE</code>,
     *                        <code>QUESTION_MESSAGE</code>, or <code>PLAIN_MESSAGE</code>
     *
     * @return an integer indicating the option selected by the user
     *
     * @throws HeadlessException if <code>GraphicsEnvironment.isHeadless</code> returns <code>true</code>
     * @see java.awt.GraphicsEnvironment#isHeadless
     */
    public static int showConfirmDialog(Component parentComponent,
                                        Object message, String title, int optionType, int messageType)
            throws HeadlessException {
        return showConfirmDialog(parentComponent, message, title, optionType,
                messageType, null);
    }

    /**
     * Brings up a dialog with a specified icon, where the number of choices is determined by the
     * <code>optionType</code> parameter. The <code>messageType</code> parameter is primarily used to supply a default
     * icon from the look and feel.
     *
     * @param parentComponent determines the <code>Frame</code> in which the dialog is displayed; if <code>null</code>,
     *                        or if the <code>parentComponent</code> has no <code>Frame</code>, a default
     *                        <code>Frame</code> is used
     * @param message         the Object to display
     * @param title           the title string for the dialog
     * @param optionType      an int designating the options available on the dialog: <code>YES_NO_OPTION</code>,
     *                        <code>YES_NO_CANCEL_OPTION</code>, or <code>OK_CANCEL_OPTION</code>
     * @param messageType     an int designating the kind of message this is, primarily used to determine the icon from
     *                        the pluggable Look and Feel: <code>ERROR_MESSAGE</code>, <code>INFORMATION_MESSAGE</code>,
     *                        <code>WARNING_MESSAGE</code>, <code>QUESTION_MESSAGE</code>, or
     *                        <code>PLAIN_MESSAGE</code>
     * @param icon            the icon to display in the dialog
     *
     * @return an int indicating the option selected by the user
     *
     * @throws HeadlessException if <code>GraphicsEnvironment.isHeadless</code> returns <code>true</code>
     * @see java.awt.GraphicsEnvironment#isHeadless
     */
    public static int showConfirmDialog(Component parentComponent,
                                        Object message, String title, int optionType,
                                        int messageType, Icon icon) throws HeadlessException {
        return showOptionDialog(parentComponent, message, title, optionType,
                messageType, icon, null, null);
    }

    public static int showOptionDialog(Component parentComponent,
                                       Object message, String title, int optionType, int messageType,
                                       Icon icon, Object[] options, Object initialValue)
            throws HeadlessException {
        return JOptionPane.showOptionDialog(parentComponent,
                message,
                title,
                optionType,
                messageType,
                icon,
                getDefaults(optionType),
                initialValue);
    }

    public static Object showInputDialog(Component parentComponent,
                                         Object message, String title, int messageType, Icon icon,
                                         Object[] selectionValues, Object initialSelectionValue)
            throws HeadlessException {
        return JOptionPane.showInputDialog(parentComponent,
                message,
                title,
                messageType,
                icon,
                selectionValues,
//                selectionValues == null ? getDefaults(messageType) : selectionValues,
                initialSelectionValue);
    }

    private static String[] getDefaults(int messageType) {
        switch (messageType) {
            case JOptionPane.YES_NO_OPTION:
                return getYesNoVariants();
            case JOptionPane.OK_CANCEL_OPTION:
                return getOkCancelVariants();
            case JOptionPane.YES_NO_CANCEL_OPTION:
                return getYesNoCancelVariants();
            default:
                return getOkVariants();
        }
    }

    public static String[] getYesNoVariants() {
        return new String[]{Message.Button_Yes.get(), Message.Button_No.get()};
    }

    public static String[] getYesNoCancelVariants() {
        return new String[]{Message.Button_Yes.get(), Message.Button_No.get(), Message.Button_Cancel.get()};
    }

    public static String[] getOkCancelVariants() {
        return new String[]{Message.Button_Ok.get(), Message.Button_Cancel.get()};
    }

    public static String[] getOkVariants() {
        return new String[]{Message.Button_Ok.get()};
    }
}
