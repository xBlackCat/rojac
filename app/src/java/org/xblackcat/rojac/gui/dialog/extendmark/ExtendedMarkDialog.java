package org.xblackcat.rojac.gui.dialog.extendmark;

import com.birosoft.liquid.LiquidLookAndFeel;
import com.toedter.calendar.JDateChooser;
import com.toedter.calendar.JSpinnerDateEditor;
import org.jdesktop.swingx.combobox.EnumComboBoxModel;
import org.xblackcat.rojac.RojacException;
import org.xblackcat.rojac.gui.component.AButtonAction;
import org.xblackcat.rojac.i18n.Messages;
import org.xblackcat.rojac.service.ServiceFactory;
import org.xblackcat.rojac.service.options.Property;
import org.xblackcat.rojac.util.UIUtils;
import org.xblackcat.rojac.util.WindowsUtils;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * @author xBlackCat
 */

public class ExtendedMarkDialog extends JDialog {
    private final JDateChooser chooser = new JDateChooser(new JSpinnerDateEditor());
    private boolean selected = false;

    private NewState readStatus;
    private DateDirection dateDirection;
    private Scope scope;
    private Scope maxScope;
    private Date selectedDate = null;

    private final EnumComboBoxModel<DateDirection> dateRangeModel = new EnumComboBoxModel<DateDirection>(DateDirection.class);
    private final EnumComboBoxModel<NewState> readStateModel = new EnumComboBoxModel<NewState>(NewState.class);
    private final EnumComboBoxModel<Scope> scopeModel = new EnumComboBoxModel<Scope>(Scope.class);

    public ExtendedMarkDialog(Window owner) {
        super(owner, ModalityType.DOCUMENT_MODAL);

        initialize();
        setResizable(false);
    }

    private void initialize() {
        JComboBox scopeSelector = new JComboBox(scopeModel);
        JComboBox dateRangeSelector = new JComboBox(dateRangeModel);
        JComboBox readStateSelector = new JComboBox(readStateModel);

        scopeSelector.setRenderer(new DescribableListRenderer() {
            @Override
            public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

                if (maxScope != null) {
                    Scope s = (Scope) value;
                    setEnabled(s.ordinal() <= maxScope.ordinal());
                }

                return this;
            }
        });
        dateRangeSelector.setRenderer(new DescribableListRenderer());
        readStateSelector.setRenderer(new DescribableListRenderer());

        scopeSelector.addActionListener(new ActionListener() {
            private Scope curr = scopeModel.getSelectedItem();

            @Override
            public void actionPerformed(ActionEvent e) {
                Scope s = scopeModel.getSelectedItem();
                if (maxScope != null && s.ordinal() > maxScope.ordinal()) {
                    scopeModel.setSelectedItem(curr);
                } else {
                    curr = s;
                }
            }
        });

        JPanel p = new JPanel(new BorderLayout(5, 5));

        final JPanel topMessage = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topMessage.add(new JLabel(Messages.Dialog_ExtMark_TopLine.get()));

        topMessage.add(scopeSelector);

        p.add(topMessage, BorderLayout.NORTH);
        JPanel center = new JPanel(new FlowLayout(FlowLayout.LEFT));
        p.add(center, BorderLayout.CENTER);

        Locale locale = Property.ROJAC_GUI_LOCALE.get();

        String datePattern = ((SimpleDateFormat) DateFormat.getDateInstance(DateFormat.LONG, locale)).toLocalizedPattern();
        String timePattern = ((SimpleDateFormat) DateFormat.getTimeInstance(DateFormat.SHORT, locale)).toLocalizedPattern();

        Date now = new Date();

        chooser.setMaxSelectableDate(now);
        chooser.setDateFormatString(datePattern);
        chooser.setLocale(locale);

        final SpinnerDateModel timeModel = new SpinnerDateModel(now, null, null, Calendar.MINUTE);
        JSpinner spinner = new JSpinner(timeModel);
        spinner.setEditor(new JSpinner.DateEditor(spinner, timePattern));

        center.add(dateRangeSelector);
        center.add(spinner);
        center.add(chooser);
        center.add(new JLabel(Messages.Dialog_ExtMark_As.get()));

        center.add(readStateSelector);

        p.add(WindowsUtils.createButtonsBar(
                this,
                Messages.Button_Ok,
                FlowLayout.RIGHT,
                new AButtonAction(Messages.Button_Ok) {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        selected = true;
                        Calendar date = Calendar.getInstance();
                        Calendar time = Calendar.getInstance();

                        date.setTime(selectedDate);
                        time.setTime(timeModel.getDate());

                        selectedDate = date.getTime();
                        readStatus = readStateModel.getSelectedItem();
                        dateDirection = dateRangeModel.getSelectedItem();
                        scope = scopeModel.getSelectedItem();

                        setVisible(false);
                    }
                },
                new AButtonAction(Messages.Button_Cancel) {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        setVisible(false);
                    }
                }
        ), BorderLayout.SOUTH);

        setContentPane(p);
        p.setBorder(new EmptyBorder(5, 5, 5, 5));
    }

    /**
     * @param messageDate
     * @param scope
     *
     * @return <code>false</code> if dialog was canceled and <code>true</code> if date was successfully selected.
     */
    public boolean selectDate(Long messageDate, Scope scope) {
        if (messageDate != null) {
            chooser.setDate(new Date(messageDate));
        }
        scopeModel.setSelectedItem(scope);
        maxScope = scope;

        pack();
        WindowsUtils.center(this, getOwner());
        setVisible(true);

        chooser.cleanup();

        return selected;
    }

    public long getSelectedDate() {
        return selectedDate.getTime();
    }

    public NewState getReadStatus() {
        return readStatus;
    }

    public DateDirection getDateDirection() {
        return dateDirection;
    }

    public Scope getScope() {
        return scope;
    }

    public static void main(String[] args) throws UnsupportedLookAndFeelException, RojacException {
        ServiceFactory.initialize();

        Property.ROJAC_GUI_LOCALE.set(new Locale("ru", "RU"));
        Property.ROJAC_GUI_LOCALE.setCache(new Locale("ru", "RU"));

        UIUtils.setLookAndFeel(new LiquidLookAndFeel());

        ExtendedMarkDialog d = new ExtendedMarkDialog(null);
        WindowsUtils.centerOnScreen(d);
        d.selectDate(null, Scope.Forum);

        System.exit(0);
    }
}
