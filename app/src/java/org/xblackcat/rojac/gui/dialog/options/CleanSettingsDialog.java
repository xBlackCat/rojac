package org.xblackcat.rojac.gui.dialog.options;

import com.toedter.calendar.JDateChooser;
import com.toedter.calendar.JSpinnerDateEditor;
import org.xblackcat.rojac.gui.component.ACancelAction;
import org.xblackcat.rojac.gui.component.AnOkAction;
import org.xblackcat.rojac.i18n.Message;
import org.xblackcat.rojac.service.options.Property;
import org.xblackcat.rojac.util.WindowsUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * 10.04.12 16:56
 *
 * @author xBlackCat
 */
class CleanSettingsDialog extends JDialog {
    private final JDateChooser chooser = new JDateChooser(new JSpinnerDateEditor());

    private Long period;

    public CleanSettingsDialog(Window owner) {
        super(owner, ModalityType.DOCUMENT_MODAL);

        JPanel cp = new JPanel(new BorderLayout(5, 5));
        setContentPane(cp);

        Locale locale = Property.ROJAC_GUI_LOCALE.get();

        String datePattern = ((SimpleDateFormat) DateFormat.getDateInstance(DateFormat.LONG, locale)).toLocalizedPattern();

        Calendar defTime = Calendar.getInstance(locale);
        defTime.add(Calendar.MONTH, -3);

        chooser.setMaxSelectableDate(new Date());
        chooser.setDate(defTime.getTime());
        chooser.setDateFormatString(datePattern);
        chooser.setLocale(locale);

        cp.add(chooser, BorderLayout.NORTH);

        Component buttonsBar = WindowsUtils.createButtonsBar(
                this,
                Message.Button_Ok,
                FlowLayout.CENTER,
                new AnOkAction() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        period = 0l;
                        Calendar date = Calendar.getInstance();

                        date.setTime(chooser.getDate());

                        date.clear(Calendar.MILLISECOND);
                        date.clear(Calendar.SECOND);
                        date.clear(Calendar.HOUR);
                        date.clear(Calendar.MINUTE);

                        period = date.getTimeInMillis();

                        setVisible(false);
                    }
                },
                new ACancelAction() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        period = null;
                        setVisible(false);
                    }
                }
        );
        cp.add(buttonsBar, BorderLayout.SOUTH);

        WindowsUtils.setDialogMinimumWidth(this, buttonsBar);
        WindowsUtils.center(this);
        setVisible(true);
    }

    public Long getPeriod() {
        return period;
    }
}
