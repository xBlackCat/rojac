package org.xblackcat.rojac.gui.dialog.options;

import com.toedter.calendar.JDateChooser;
import com.toedter.calendar.JSpinnerDateEditor;
import org.xblackcat.rojac.gui.component.ACancelAction;
import org.xblackcat.rojac.gui.component.AnOkAction;
import org.xblackcat.rojac.i18n.Message;
import org.xblackcat.rojac.service.options.Property;
import org.xblackcat.rojac.util.WindowsUtils;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

/**
 * 10.04.12 16:56
 *
 * @author xBlackCat
 */
class CleanSettingsDialog extends JDialog {
    private final JDateChooser chooser = new JDateChooser(new JSpinnerDateEditor());
    private final Locale locale = Property.ROJAC_GUI_LOCALE.get();

    private Long period;

    public CleanSettingsDialog(Window owner) {
        super(owner, ModalityType.DOCUMENT_MODAL);
        setTitle(Message.Dialog_CleanDb_Title.get());

        JPanel cp = new JPanel(new BorderLayout(5, 5));
        cp.setBorder(new EmptyBorder(10, 10, 10, 10));
        setContentPane(cp);

        String datePattern = ((SimpleDateFormat) DateFormat.getDateInstance(DateFormat.LONG, locale)).toLocalizedPattern();

        Calendar defTime = makeCalendar(new Date());
        defTime.add(Calendar.MONTH, -3);

        chooser.setMaxSelectableDate(new Date());
        chooser.setDate(defTime.getTime());
        chooser.setDateFormatString(datePattern);
        chooser.setLocale(locale);

        final SpinnerNumberModel topicsAge = new SpinnerNumberModel(getAge(), 0, 10 * 365, 1);

        DateTrackListener dateTrackListener = new DateTrackListener(topicsAge);
        chooser.addPropertyChangeListener("date", dateTrackListener);
        topicsAge.addChangeListener(dateTrackListener);

        JPanel datePane = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        datePane.add(new JLabel(Message.Dialog_CleanDb_Label_RemoveTopicsSince.get()));
        datePane.add(chooser);

        JPanel agePane = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
        agePane.add(new JLabel(Message.Dialog_CleanDb_Label_OlderThan.get()));
        agePane.add(new JSpinner(topicsAge));
        agePane.add(new JLabel(Message.Dialog_CleanDb_Label_Days.get()));

        cp.add(
                WindowsUtils.createColumn(
                        5,
                        5,
                        datePane,
                        agePane
                ),
                BorderLayout.NORTH
        );

        JComponent buttonsBar = WindowsUtils.createButtonsBar(
                this,
                Message.Button_Ok,
                FlowLayout.CENTER,
                new AnOkAction() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        period = chooser.getDate().getTime();

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

        pack();
        setResizable(false);
        WindowsUtils.center(this);
        setVisible(true);
    }

    private Calendar makeCalendar(Date date) {
        Calendar cal = Calendar.getInstance(locale);

        cal.setTime(date);

        cal.clear(Calendar.MILLISECOND);
        cal.clear(Calendar.SECOND);
        cal.clear(Calendar.MINUTE);
        cal.clear(Calendar.HOUR_OF_DAY);
        cal.clear(Calendar.HOUR);
        cal.clear(Calendar.AM_PM);
        return cal;
    }

    private long getAge() {
        Calendar now = makeCalendar(new Date());
        Date chosen = chooser.getDate();

        long diff = now.getTimeInMillis() - chosen.getTime();

        return TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
    }

    public Long getPeriod() {
        return period;
    }

    private class DateTrackListener implements PropertyChangeListener, ChangeListener {
        private final SpinnerNumberModel topicsAge;
        private boolean changing = false;

        public DateTrackListener(SpinnerNumberModel topicsAge) {
            this.topicsAge = topicsAge;
        }

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            if (!changing) {
                changing = true;
                topicsAge.setValue(getAge());
                changing = false;
            }
        }

        @Override
        public void stateChanged(ChangeEvent e) {
            if (!changing) {
                changing = true;
                Calendar defTime = makeCalendar(new Date());
                defTime.add(Calendar.DAY_OF_MONTH, -topicsAge.getNumber().intValue());

                chooser.setDate(defTime.getTime());
                changing = false;
            }
        }
    }
}
